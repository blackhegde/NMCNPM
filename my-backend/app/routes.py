from flask import request, jsonify
from app import app, db
from app.models import Activity, Streak, Achievement
from datetime import datetime, timedelta


# create activity
@app.route('/api/activities', methods=['POST'])
def create_activity():
    data = request.json
    new_activity = Activity(
        user_id=data['user_id'],
        type=data['type'],
        distance=data['distance'],
        duration=data['duration'],
        start_time=datetime.fromisoformat(data['start_time']),
        end_time=datetime.fromisoformat(data['end_time'])
    )
    db.session.add(new_activity)
    db.session.commit()
    return jsonify({"message": "Activity created!"}), 201

# Get all activities
@app.route('/api/activities/<int:user_id>', methods=['GET'])
def get_activities(user_id):
    activities = Activity.query.filter_by(user_id=user_id).all()
    return jsonify([{
        'id': a.id,
        'type': a.type,
        'distance': a.distance,
        'duration': a.duration,
        'start_time': a.start_time.isoformat(),
        'end_time': a.end_time.isoformat()
    } for a in activities])

#update streak
@app.route('/api/streak', methods=['POST'])
def update_streak():
    data = request.json
    user_id = data['user_id']
    today = datetime.now(datetime.UTC)

    #lay streak hien tai
    streak = Streak.query.filter_by(user_id=user_id).first()
    if not streak:
        #tao moi neu chua co
        streak = Streak(user_id=user_id, start_date=today, streak_days=1)
        db.session.add(streak)
    else:
        #cap nhat streak hien tai
        if streak.is_active:
            if streak.end_date and streak.end_date.date() < today - timedelta(days=1):
                streak.is_active = False  # Kết thúc streak
            elif streak.end_date and streak.end_date.date() == today - timedelta(days=1):
                streak.streak_days += 1  # Tăng ngày streak
                streak.end_date = today
            else:
                return jsonify({'message': 'Streak already updated for today'}), 200
    db.session.commit()
    return jsonify({'message': 'Streak updated successfully', 'streak_days': streak.streak_days}), 200