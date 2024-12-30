from flask import request, jsonify
from app import app, db
from app.models import Activity, Streak, Achievement, User
from datetime import datetime, timedelta


# create activity
@app.route('/api/activities', methods=['POST'])
def create_activity():
    data = request.json
    new_activity = Activity(
        user_id = data['user_id'],
        type = data['type'],
        distance = data['distance'],
        duration = data['duration'],
        average_speed = data['average_speed'],
        gps_data = data['gps_data'],
        start_time = datetime.fromisoformat(data['start_time']),
        end_time = datetime.fromisoformat(data['end_time'])
    )
    db.session.add(new_activity)
    db.session.commit()
    return jsonify({"message": "Activity created!"}), 201

# Get all activities from user
@app.route('/api/activities/<int:user_id>', methods=['GET'])
def get_activities(user_id):
    activities = Activity.query.filter_by(user_id=user_id).all()
    return jsonify([{
        'id': activity.id,
        'type': activity.type,
        'distance': activity.distance,
        'duration': activity.duration,
        'average_speed': activity.average_speed,
        'gps_data': activity.gps_data,
        'start_time': activity.start_time,
        'end_time': activity.end_time
    } for activity in activities])

#Get 50 recent activities
@app.route('/api/activities/recent', methods=['GET'])
def get_recent_activities():
    recent_activities = Activity.query.order_by(Activity.start_time.desc()).limit(50).all()

    #chuyen ket qua thanh mang
    activities_list = []
    for activity in recent_activities:
        activities_list.append({
            'id': activity.id,
            'user_id': activity.user_id,
            'type': activity.type,
            'distance': activity.distance,
            'duration': activity.duration,
            'average_speed': activity.average_speed,
            'gps_data': activity.gps_data,
            'start_time': activity.start_time,
            'end_time': activity.end_time
        })
    #chuyen ket qua thanh json
    return jsonify(activities_list)

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

#dang ky nguoi dung moi
@app.route('/api/user', methods=['POST'])
def signup():
    data = request.json
    new_user = User(
        username = data['username'],
        email = data['email'],
        avatar_type = data['avatar_type']
    )
    db.session.add(new_user)
    db.session.commit()
    return jsonify({"message": "Sign up successfully"}), 201

#lay thong tin streak cua mot user
@app.route('/api/streak/<int:user_id>', methods=['GET'])
def get_streak(user_id):
    streak = Streak.query.filter_by(Streak.user_id)
    return jsonify({
        'id': streak.id,
        'user_id': streak.user_id,
        'start_date': streak.start_date,
        'end_date': streak.end_date,
        'is_active': streak.is_active,
        'streak_days': streak.streak_days
    })

#lay bang xep hạg 50 người