"""Pruebas HTTP del mock. Ejecutar: python3 mock/test_server.py"""
import importlib.util
import json
import threading
import time
import unittest
from pathlib import Path
from urllib.error import HTTPError
from urllib.request import Request, urlopen
from http.server import ThreadingHTTPServer

spec = importlib.util.spec_from_file_location('vora_server', Path(__file__).with_name('server.py'))
module = importlib.util.module_from_spec(spec)
spec.loader.exec_module(module)

class QuietHandler(module.Handler):
    def log_message(self, *_):
        pass

class MockApiTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.server = ThreadingHTTPServer(('127.0.0.1', 0), QuietHandler)
        cls.base = f'http://127.0.0.1:{cls.server.server_port}'
        cls.thread = threading.Thread(target=cls.server.serve_forever, daemon=True)
        cls.thread.start()

    @classmethod
    def tearDownClass(cls):
        cls.server.shutdown()
        cls.server.server_close()
        cls.thread.join()

    def call(self, path, body=None, headers=None, method=None, raw=None):
        headers = dict(headers or {})
        if body is not None:
            raw = json.dumps(body).encode()
            headers.setdefault('Content-Type', 'application/json')
        req = Request(self.base + path, data=raw, headers=headers, method=method)
        try:
            res = urlopen(req, timeout=3)
        except HTTPError as exc:
            res = exc
        with res:
            return res.status, res.read()

    def getjson(self, *args, **kwargs):
        code, raw = self.call(*args, **kwargs)
        return code, json.loads(raw)

    def test_health_and_web(self):
        self.assertEqual(self.getjson('/health')[1]['status'], 'ok')
        code, raw = self.call('/')
        self.assertEqual(code, 200)
        self.assertIn(b'VORA Mock API', raw)

    def test_workout_list_detail_links(self):
        code, data = self.getjson('/v1/workouts')
        self.assertEqual(code, 200)
        self.assertEqual(len(data['data']), 3)
        for entry in data['data']:
            code, detail = self.getjson('/v1/workouts/' + entry['id'])
            self.assertEqual(code, 200)
            self.assertEqual(detail['data']['id'], entry['id'])
            self.assertEqual(len(detail['data']['exercises']), entry['exerciseCount'])

    def test_food_search_pagination_and_accent(self):
        code, data = self.getjson('/v1/foods?q=platano')
        self.assertEqual(data['data'], [])
        code, data = self.getjson('/v1/foods?q=POLLO')
        self.assertEqual(data['data'][0]['id'], 'f05')
        code, data = self.getjson('/v1/foods?page=2&pageSize=5')
        self.assertEqual(code, 200)
        self.assertEqual(len(data['data']), 5)
        self.assertEqual(data['meta']['total'], 12)
        self.assertEqual(self.getjson('/v1/foods?pageSize=0')[0], 400)

    def test_empty_error_and_bad_json(self):
        self.assertEqual(self.getjson('/v1/workouts', headers={'X-Mock-Scenario':'empty'})[1]['data'], [])
        self.assertEqual(self.getjson('/v1/workouts', headers={'X-Mock-Scenario':'error'})[0], 503)
        code, raw = self.call('/v1/workouts', headers={'X-Mock-Scenario':'malformed'})
        self.assertEqual(code, 200)
        with self.assertRaises(json.JSONDecodeError):
            json.loads(raw)
        self.assertEqual(self.getjson('/v1/workouts',headers={'X-Mock-Scenario':'unknown'})[0],400)

    def test_delay(self):
        before = time.monotonic()
        self.assertEqual(self.call('/health',headers={'X-Mock-Delay-Ms':'100'})[0],200)
        self.assertGreaterEqual(time.monotonic()-before, 0.09)
        self.assertEqual(self.call('/health',headers={'X-Mock-Delay-Ms':'10001'})[0],400)

    def test_missing_resource_and_methods(self):
        self.assertEqual(self.call('/v1/workouts/missing')[0],404)
        self.assertEqual(self.call('/missing')[0],404)
        self.assertEqual(self.call('/v1/coach/messages')[0],405)
        self.assertEqual(self.call('/v1/workouts',body={})[0],405)

    def test_coach_and_invalid_inputs(self):
        code, data = self.getjson('/v1/coach/messages',body={'text':'entrenamiento','context':{'coachingStyle':'direct'}})
        self.assertEqual(code,200)
        self.assertEqual(data['data']['topic'],'workout')
        self.assertTrue(data['data']['isSimulated'])
        for body in ({'text':''},{'text':'   '},{'text':1},{'text':'ok','context':[]}, {'text':'ok','context':{'coachingStyle':'bad'}}):
            self.assertEqual(self.call('/v1/coach/messages',body=body)[0],400)

    def test_invalid_json_and_content_type(self):
        self.assertEqual(self.call('/v1/coach/messages',raw=b'{bad',headers={'Content-Type':'application/json'})[0],400)
        self.assertEqual(self.call('/v1/coach/messages',raw=b'{}',headers={'Content-Type':'text/plain'})[0],415)
        self.assertEqual(self.call('/v1/coach/messages',raw=b'[]',headers={'Content-Type':'application/json'})[0],400)

    def test_analysis_confirmation_and_totals(self):
        code, data = self.getjson('/v1/nutrition/analyses',body={'source':'photo','preset':'breakfast'})
        self.assertEqual(code,200)
        self.assertTrue(data['data']['requiresConfirmation'])
        self.assertAlmostEqual(data['data']['totals']['kcal'],366.6)
        self.assertEqual(len(data['data']['items']),3)
        self.assertEqual(self.call('/v1/nutrition/analyses',body={'source':'text'})[0],400)
        self.assertEqual(self.call('/v1/nutrition/analyses',body={'source':'photo','preset':'bad'})[0],400)

    def test_weekly_dates_and_validation(self):
        code, data = self.getjson('/v1/plans/weekly?startDate=2026-12-28')
        self.assertEqual(code,200)
        self.assertEqual(data['data']['days'][-1]['date'],'2027-01-03')
        self.assertEqual(self.call('/v1/plans/weekly?startDate=bad')[0],400)
        self.assertEqual(self.call('/v1/plans/weekly?startDate=9999-12-31')[0],400)
        self.assertEqual(self.getjson('/v1/activity/daily?date=2026-10-06')[1]['data']['date'],'2026-10-06')

    def test_review_uses_input_and_rejects_bad_numbers(self):
        body={'startDate':'2026-10-05','summary':{'completedSessions':2,'volumeKg':900,'averageKcal':1800}}
        code, data = self.getjson('/v1/progress/weekly-review',body=body)
        self.assertEqual(code,200)
        self.assertIn('2 sesiones',data['data']['text'])
        for value in (-1,1.5,True,'2',float('nan')):
            body['summary']['completedSessions']=value
            self.assertEqual(self.call('/v1/progress/weekly-review',body=body)[0],400)

    def test_cors_preflight(self):
        self.assertEqual(self.call('/v1/workouts',method='OPTIONS')[0],204)

if __name__ == '__main__':
    unittest.main(verbosity=2)
