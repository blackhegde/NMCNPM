from flask import Flask
from flask_cors import CORS
from flask_sqlalchemy import SQLAlchemy
from config import app_config
# from .routes import setup_routes
# from dotenv import load_dotenv

app = Flask(__name__)
app.config.from_object(app_config['develop_config'])
CORS(app)  # Cho phép CORS
db = SQLAlchemy(app)

# setup_routes(app)
from app import routes