from app.models import Activity, Achievement, Streak
from datetime import datetime, timedelta
from app import db

#caculate distance, duration
def get_user_analytics(user_id):
    activities = Activity.query.filter_by(user_id=user_id).all()
    total_distance = sum(a.distance for a in activities)
    total_duration = sum(a.duration for a in activities)
    return {
        'total_distance': total_distance,
        'total_duration': total_duration
    }


#Check and update streak
def update_streak(user_id):
    today = datetime.utcnow().date()
    # datetime.datetime.now(datetime.UTC)
    yesterday = today - timedelta(days=1)
    streak = Streak.query.filter_by(user_id=user_id, is_active=True).first()

    if streak:
        if streak.end_date == yesterday:
            streak.end_date = today
        else:
            streak.is_active = False
    else:
        streak = Streak(user_id=user_id, start_date=today, end_date=today)
        db.session.add(streak)

    db.session.commit()


#Check achivement
def check_achievements(user_id):
    activities = Activity.query.filter_by(user_id=user_id).all()
    total_distance = sum(a.distance for a in activities)

    if total_distance >= 100:  # Example: 100 km achievement
        achievement = Achievement.query.filter_by(user_id=user_id, name='100 km').first()
        if not achievement:
            achievement = Achievement(user_id=user_id, name='100 km', achieved_on=datetime.utcnow())
            db.session.add(achievement)
            db.session.commit()
            return True
    return False


#Streak warning
def send_streak_warning(user_id):
    streak = Streak.query.filter_by(user_id=user_id, is_active=True).first()
    if streak and (datetime.utcnow().date() - streak.end_date).days >= 1:
        # Send notification (e.g., via Firebase Cloud Messaging)
        pass

#Achivement send
def send_achievement_notification(user_id, achievement_name):
    # Send notification (e.g., via Firebase Cloud Messaging)
    pass