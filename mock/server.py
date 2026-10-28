#!/usr/bin/env python3
"""API didáctica VORA. Python 3.9+, biblioteca estándar, sin credenciales."""
import argparse
import json
import math
import os
import time
import unicodedata
from datetime import date, timedelta
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
from urllib.parse import parse_qs, unquote, urlsplit

ROOT = Path(__file__).resolve().parent
DOCS = ROOT / "docs"
DATA = json.loads((ROOT / "fixtures.json").read_text(encoding="utf-8"))
STATIC_TYPES = {
    ".html": "text/html; charset=utf-8",
    ".json": "application/json; charset=utf-8",
    ".css": "text/css; charset=utf-8",
    ".js": "text/javascript; charset=utf-8",
    ".png": "image/png",
    ".txt": "text/plain; charset=utf-8",
}


class ApiError(Exception):
    def __init__(self, status, code, message):
        self.status, self.code, self.message = status, code, message


def error(status, code, message):
    return status, {"error": {"code": code, "message": message}}


def fail(message):
    raise ApiError(400, "INVALID_REQUEST", message)


def normalized(value):
    return "".join(c for c in unicodedata.normalize("NFD", value.lower())
                   if unicodedata.category(c) != "Mn")


def read_date(value):
    try:
        if not isinstance(value, str) or len(value) != 10:
            raise ValueError()
        return date.fromisoformat(value)
    except (ValueError, TypeError):
        fail("La fecha debe ser válida y usar YYYY-MM-DD.")


def number(value, name, low=0, high=1000000):
    if isinstance(value, bool) or not isinstance(value, (int, float)):
        fail(f"{name} debe ser numérico.")
    if not math.isfinite(value) or not low <= value <= high:
        fail(f"{name} debe estar entre {low} y {high}.")
    return value


def text_field(body, key, maximum=2000):
    value = body.get(key)
    if not isinstance(value, str) or not value.strip() or len(value) > maximum:
        fail(f"{key} debe ser un texto de 1 a {maximum} caracteres.")
    return value.strip()


def query_int(query, key, default, low, high):
    try:
        result = int(query.get(key, [str(default)])[0])
    except (TypeError, ValueError):
        fail(f"{key} debe ser entero.")
    if not low <= result <= high:
        fail(f"{key} debe estar entre {low} y {high}.")
    return result


def static_response(path):
    """Página de práctica, contrato OpenAPI y Swagger UI. None si no es estático."""
    path = unquote(path)
    if path == "/":
        return 200, (ROOT / "index.html").read_bytes(), "text/html; charset=utf-8"
    if path == "/openapi.json":
        return 200, (ROOT / "openapi.json").read_bytes(), "application/json; charset=utf-8"
    if path in {"/docs", "/docs/", "/swagger", "/swagger/"}:
        return 200, (DOCS / "index.html").read_bytes(), "text/html; charset=utf-8"
    if not path.startswith("/docs/"):
        return None
    name = path.removeprefix("/docs/")
    if not name or "/" in name or "\\" in name or name.startswith("."):
        return None
    file = (DOCS / name).resolve()
    if file.parent != DOCS.resolve() or not file.is_file():
        return None
    return 200, file.read_bytes(), STATIC_TYPES.get(file.suffix, "application/octet-stream")


def respond(method, target, body=None, scenario="normal"):
    """Mismo contrato para el servidor, OpenAPI y ejemplos de Postman."""
    parsed = urlsplit(target)
    path, query = parsed.path, parse_qs(parsed.query, keep_blank_values=True)
    try:
        if scenario not in {"normal", "empty", "error", "malformed"}:
            fail("X-Mock-Scenario: normal, empty, error o malformed.")
        routes = {"/health": "GET", "/v1/workouts": "GET", "/v1/plans/weekly": "GET",
                  "/v1/foods": "GET", "/v1/coach/suggestions": "GET",
                  "/v1/coach/messages": "POST", "/v1/nutrition/analyses": "POST",
                  "/v1/progress/weekly-review": "POST", "/v1/activity/daily": "GET"}
        expected = "GET" if path.startswith("/v1/workouts/") else routes.get(path)
        if expected is None:
            raise ApiError(404, "NOT_FOUND", "La ruta no existe.")
        if expected != method:
            raise ApiError(405, "METHOD_NOT_ALLOWED", f"Usar {expected}.")
        if scenario == "error":
            raise ApiError(503, "SERVICE_UNAVAILABLE", "Servicio simulado no disponible.")
        if scenario == "malformed":
            return 200, b'{"data": INVALID_JSON'
        if method == "POST" and not isinstance(body, dict):
            fail("Se requiere un objeto JSON.")
        if path == "/health":
            return 200, {"status": "ok", "service": "vora-mock", "version": "1.0"}
        if path == "/v1/workouts":
            level = query.get("level", [None])[0]
            if level is not None and level not in {"beginner", "intermediate", "advanced"}:
                fail("level: beginner, intermediate o advanced.")
            items = [{k: w[k] for k in ("id", "name", "level", "durationMinutes", "exerciseCount")}
                     for w in DATA["workouts"] if level is None or w["level"] == level]
            return 200, {"data": [] if scenario == "empty" else items}
        if path.startswith("/v1/workouts/"):
            ident = path.removeprefix("/v1/workouts/")
            item = next((w for w in DATA["workouts"] if w["id"] == ident), None)
            if item is None:
                raise ApiError(404, "WORKOUT_NOT_FOUND", "Entrenamiento no encontrado.")
            return 200, {"data": item}
        if path == "/v1/plans/weekly":
            start = read_date(query.get("startDate", ["2026-10-05"])[0])
            if start > date.max - timedelta(days=6):
                fail("startDate debe permitir representar los siete días del plan.")
            ids = ["w01", None, "w02", None, "w03", None, None]
            days = [{"date": (start + timedelta(days=i)).isoformat(), "workoutId": ident,
                     "type": "workout" if ident else "recovery"} for i, ident in enumerate(ids)]
            return 200, {"data": {"startDate": start.isoformat(),
                                  "days": [] if scenario == "empty" else days}}
        if path == "/v1/foods":
            page = query_int(query, "page", 1, 1, 10000)
            size = query_int(query, "pageSize", 10, 1, 50)
            q = normalized(query.get("q", [""])[0])
            items = [f for f in DATA["foods"] if q in normalized(f["name"])]
            if scenario == "empty":
                items = []
            return 200, {"data": items[(page-1)*size:page*size],
                         "meta": {"page": page, "pageSize": size, "total": len(items)}}
        if path == "/v1/coach/suggestions":
            return 200, {"data": [] if scenario == "empty" else DATA["suggestions"]}
        if path == "/v1/coach/messages":
            message = normalized(text_field(body, "text"))
            context = body.get("context", {})
            if not isinstance(context, dict):
                fail("context debe ser un objeto.")
            style = context.get("coachingStyle", "balanced")
            if style not in {"supportive", "balanced", "direct"}:
                fail("coachingStyle: supportive, balanced o direct.")
            kind = "general"
            if any(word in message for word in ("entren", "workout", "cansad")):
                kind = "workout"
            elif any(word in message for word in ("cen", "comid", "vegetar", "desayun")):
                kind = "nutrition"
            elif any(word in message for word in ("progres", "objetiv", "semana")):
                kind = "progress"
            answer = DATA["coachReplies"][kind]
            prefix = {"supportive": "Vamos paso a paso. ", "balanced": "", "direct": "Siguiente paso: "}[style]
            return 200, {"data": {"role": "assistant", "text": prefix + answer,
                                  "topic": kind, "isSimulated": True}}
        if path == "/v1/nutrition/analyses":
            source = body.get("source")
            if source not in {"text", "photo"}:
                fail("source debe ser text o photo.")
            preset = body.get("preset", "breakfast")
            if preset not in DATA["analyses"]:
                fail("preset debe ser breakfast, lunch o vegetarian.")
            if source == "text":
                text_field(body, "text")
            # Para photo se elige un fixture; no se sube ni interpreta una foto.
            items = []
            for entry in DATA["analyses"][preset]:
                food = next(f for f in DATA["foods"] if f["id"] == entry["foodId"])
                items.append({"foodId": food["id"], "name": food["name"], "grams": entry["grams"],
                              "nutritionPer100g": food["nutritionPer100g"]})
            totals = {key: round(sum(i["nutritionPer100g"][key]*i["grams"]/100 for i in items), 2)
                      for key in ("kcal", "proteinG", "carbsG", "fatG")}
            return 200, {"data": {"source": source, "preset": preset, "isSimulated": True,
                                  "requiresConfirmation": True, "items": items, "totals": totals}}
        if path == "/v1/progress/weekly-review":
            start = read_date(text_field(body, "startDate", 10))
            summary = body.get("summary")
            if not isinstance(summary, dict):
                fail("summary debe ser un objeto.")
            sessions = number(summary.get("completedSessions"), "completedSessions", 0, 100)
            if int(sessions) != sessions:
                fail("completedSessions debe ser entero.")
            volume = number(summary.get("volumeKg"), "volumeKg")
            avg = number(summary.get("averageKcal"), "averageKcal", 0, 20000)
            return 200, {"data": {"startDate": start.isoformat(), "isSimulated": True,
                "summary": summary,
                "text": f"Registraste {int(sessions)} sesiones, {volume:g} kg de volumen y un promedio de {avg:g} kcal por día. Revisá tus registros y planificá la próxima semana.",
                "recommendations": ["Revisar las sesiones completadas.", "Completar los días sin registros."]}}
        if path == "/v1/activity/daily":
            day = read_date(query.get("date", ["2026-10-06"])[0])
            return 200, {"data": {"date": day.isoformat(), "steps": 6200, "distanceKm": 4.2,
                                  "activeKcal": 280, "isSimulated": True}}
    except ApiError as exc:
        return error(exc.status, exc.code, exc.message)


class Handler(BaseHTTPRequestHandler):
    server_version = "VoraMock/1.0"

    def send(self, status, payload, content_type="application/json; charset=utf-8"):
        raw = payload if isinstance(payload, bytes) else json.dumps(payload, ensure_ascii=False, allow_nan=False).encode("utf-8")
        self.send_response(status)
        self.send_header("Content-Type", content_type)
        self.send_header("Content-Length", str(len(raw)))
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Cache-Control", "no-store")
        self.end_headers()
        try:
            self.wfile.write(raw)
        except (BrokenPipeError, ConnectionResetError):
            pass  # El cliente canceló o venció su timeout.

    def handle_request(self, method):
        if method == "GET":
            static = static_response(urlsplit(self.path).path)
            if static is not None:
                return self.send(*static)
        try:
            delay = int(self.headers.get("X-Mock-Delay-Ms", "0"))
            if not 0 <= delay <= 10000:
                fail("X-Mock-Delay-Ms debe estar entre 0 y 10000.")
            body = None
            if method == "POST":
                if self.headers.get("Content-Type", "").split(";")[0].strip().lower() != "application/json":
                    raise ApiError(415, "UNSUPPORTED_MEDIA_TYPE", "Usar Content-Type application/json.")
                length = int(self.headers.get("Content-Length", "0"))
                if not 0 < length <= 65536:
                    fail("Enviar un cuerpo JSON de hasta 64 KiB.")
                body = json.loads(self.rfile.read(length).decode("utf-8"),
                                  parse_constant=lambda _: fail("JSON no admite NaN ni Infinity."))
            time.sleep(delay/1000)
            status, payload = respond(method, self.path, body, self.headers.get("X-Mock-Scenario", "normal"))
        except ApiError as exc:
            status, payload = error(exc.status, exc.code, exc.message)
        except (ValueError, UnicodeDecodeError):
            status, payload = error(400, "INVALID_REQUEST", "JSON, longitud o demora inválidos.")
        self.send(status, payload)

    def do_GET(self):
        self.handle_request("GET")

    def do_POST(self):
        self.handle_request("POST")

    def do_OPTIONS(self):
        self.send_response(204)
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        self.send_header("Access-Control-Allow-Headers", "Content-Type, X-Mock-Scenario, X-Mock-Delay-Ms")
        self.send_header("Content-Length", "0")
        self.end_headers()


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--host", default=os.environ.get("HOST", "127.0.0.1"))
    parser.add_argument("--port", type=int, default=int(os.environ.get("PORT", "8000")))
    args = parser.parse_args()
    print(f"VORA Mock en http://{args.host}:{args.port}/ — Ctrl+C para detener.", flush=True)
    server = ThreadingHTTPServer((args.host, args.port), Handler)
    try:
        server.serve_forever()
    except KeyboardInterrupt:
        pass
    finally:
        server.server_close()
