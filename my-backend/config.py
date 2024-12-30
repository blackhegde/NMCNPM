import os

class Config:
    SQLALCHEMY_DATABASE_URI = os.getenv('DATABASE_URL') or "postgresql://habit_track_postgresql_user:dX8NMIBq8B54TDEwqGfLmDL2poDbZySD@dpg-ctnaoulumphs73c4nsh0-a.singapore-postgres.render.com/habit_track_postgresql"
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    SCHEDULER_API_ENABLED = True

app_config = {
    'develop_config': Config
} 