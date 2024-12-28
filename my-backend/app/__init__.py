from flask import Flask
from flask_cors import CORS
from .routes import setup_routes
# from dotenv import load_dotenv

def create_app():
    app = Flask(__name__)
    app.config.from_object('config.Config')
    CORS(app)  # Cho phép CORS
    setup_routes(app)
    return app