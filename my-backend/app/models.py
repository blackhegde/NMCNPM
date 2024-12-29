from app import db

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(80), unique=True, nullable=False)
    email = db.Column(db.String(120), unique=True, nullable=False)
    password_hash = db.Column(db.String(128), nullable=False)
    avatar_type = db.Column(db.Integer)
    # 1-N activity
    activities = db.relationship('Activity', backref='user', lazy=True)
    # 1-1 streak
    streak = db.relationship('Streak', backref='user', uselist=False, lazy=True)
    # 1-N achivement
    achievements = db.relationship('Achievement', backref='user', lazy=True)
    # 1-N ranking
    rank = db.relationship('Ranking', backref='user', lazy=True)

class Activity(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    type = db.Column(db.String(50), nullable=False)
    distance = db.Column(db.Float, nullable=False)
    duration = db.Column(db.Integer, nullable=False)  # in seconds
    average_speed = db.Column(db.Float)
    gps_data = db.Column(db.JSON)
    start_time = db.Column(db.DateTime, nullable=False)
    end_time = db.Column(db.DateTime, nullable=False)

class Streak(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False, unique=True) # Dam bao 1-1 voi user
    start_date = db.Column(db.DateTime, nullable=False)
    end_date = db.Column(db.DateTime, nullable=True)
    is_active = db.Column(db.Boolean, default=True)
    streak_days = db.Column(db.Integer, nullable=False, default=0)

class Achievement(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    name = db.Column(db.String(100), nullable=False)
    achieved_on = db.Column(db.DateTime, nullable=False)
    description = db.Column(db.Text)

class Ranking(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    rank_type = db.Column(db.String(50), nullable=False)
    position = db.Column(db.Integer)
