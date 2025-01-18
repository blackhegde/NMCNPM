from calendar import monthrange
from flask import request, jsonify
from sqlalchemy import func
from app import app, db
from app.models import Activity, Streak, Achievement, User
from datetime import datetime, timedelta, timezone


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

@app.route('/api/streak/update/<int:user_id>', methods=['POST'])
def update_streak(user_id):
    today = datetime.now()  # Chỉ lấy ngày, không lấy giờ

    # Lấy streak hiện tại
    streak = Streak.query.filter_by(user_id=user_id).first()

    if not streak:
        # Tạo streak mới nếu chưa có
        streak = Streak(user_id=user_id, start_date=today, end_date=today, streak_days=1, is_active=True)
        db.session.add(streak)
    else:
        if streak.is_active:
            # Kiểm tra nếu streak đã kết thúc
            if streak.end_date and streak.end_date < today - timedelta(days=1):
                streak.is_active = False  # Kết thúc streak
            # Kiểm tra nếu streak vẫn hoạt động và end_date là ngày hôm qua
            elif streak.end_date == today - timedelta(days=1):
                streak.streak_days += 1  # Tăng ngày streak
                streak.end_date = today  # Cập nhật ngày kết thúc
            # Kiểm tra nếu streak đã được cập nhật trong ngày hôm nay
            elif streak.end_date == today:
                return jsonify({'message': 'Streak already updated for today'}), 200
            else:
                # Nếu end_date là None hoặc không hợp lệ, bắt đầu streak mới
                streak.start_date = today
                streak.end_date = today
                streak.streak_days = 1
        else:
            # Nếu streak không hoạt động, bắt đầu streak mới
            streak.start_date = today
            streak.end_date = today
            streak.streak_days = 1
            streak.is_active = True

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

#lay thong tin cua mot user
@app.route('/api/users/<string:username>', methods=['GET'])
def get_user(username):
    user = User.query.filter(User.username == username).first()
    return jsonify({
        'user_id': user.id,
        'username': user.username,
        'email': user.email,
        'avatar_type': user.avatar_type
    })

#lay thong tin streak cua mot user
@app.route('/api/streak/<int:user_id>', methods=['GET'])
def get_streak(user_id):
    streak = Streak.query.filter(Streak.user_id == user_id).first()
    return jsonify({
        'id': streak.id,
        'user_id': streak.user_id,
        'start_date': streak.start_date,
        'end_date': streak.end_date,
        'is_active': streak.is_active,
        'streak_days': streak.streak_days
    })

#lay bang xep hạg 50 người theo thang
@app.route('/api/leaderboard/distance/month', methods=['GET'])
def get_monthlymonthly_leaderboard() :
    
    #lay tham so month va year tu query string
    month = request.args.get('month', type=int, default=datetime.today().month)
    year = request.args.get('year', type=int, default=datetime.today().year)
    
    #xac dinh start va end month
    start_of_month = datetime(year, month, 1)
    end_of_month = datetime(year, month, monthrange(year, month)[1])
    
    #truy van tong quang duong theo user_id trong 1 thang
    leaderboard = (
         db.session.query(
            Activity.user_id,
            func.sum(Activity.distance).label('total_distance')
        )
        .filter(Activity.start_time >= start_of_month, Activity.start_time <= end_of_month)
        .group_by(Activity.user_id, Activity.type) 
        .order_by(func.sum(Activity.distance).desc()) 
        .limit(50)  
        .all()
    )
    
    #chuyen du lieu thanh danh sach json
    leaderboard_data = [
        {
            'rank' : idx + 1,
            'user_id' : row[0],
            'activity_type' : row[1],
            'total_distance' : round(row[2], 2)
        }
        for idx, row in enumerate(leaderboard)
    ]
    return jsonify({
        'month': month,
        'year': year,
        'leaderboard': leaderboard_data
    })
    
#lay top 50 streak
@app.route('/api/streak/top', methods=['GET'])
def get_top_streaks():
    top_streaks = (
        db.session.query(
            Streak.user_id,
            Streak.streak_days,
            Streak.start_date,
            Streak.end_date,
            Streak.is_active,
            User.username,
            User.avatar_type
        )
        .join(User, Streak.user_id == User.id)
        .order_by(Streak.streak_days.desc())  
        .limit(50)  
        .all()
    )

    streaks_data = [
        {
            'rank': idx + 1,  
            'user_id': row.user_id,
            'username': row.username,
            'avatar_type': row.avatar_type,
            'streak_days': row.streak_days,
            'start_date': row.start_date,
            'end_date': row.end_date,
            'is_active': row.is_active
        }
        for idx, row in enumerate(top_streaks)
    ]

    return jsonify({
        'top_streaks': streaks_data
    })

# #tong ket hang thang cua user
# @app.route('/api/monthly_summary', methods=['POST'])
# def create_monthly_summary():
#     data = request.json
#     year = data['year']
#     month = data['month']

#     users = User.query.all() #lay tat ca user
#     for user in users:
#         #lay all hd cua user trong thang do
#         start_of_month = datetime(year, month, 1)
#         end_of_month = datetime(year, month, 1) + timedelta(days=31)
#         end_of_month = end_of_month.replace(day=1) - timedelta(days=1)  # Điều chỉnh về ngày cuối tháng

#         activities = Activity.query.filter(
#             Activity.user_id == user.id,
#             Activity.start_time >= start_of_month,
#             Activity.start_time <= end_of_month
#         ).all()    
        
#         # tinh total dis, dura, so activ
#         total_distance = sum([activity.distance for activity in activities])
#         total_duration = sum([activity.duration for activity in activities])
#         total_activities = len(activities)
        
#         #tinh streak
#         streak = Streak.query.filter_by(user_id=user.id).first()
#         streak_days = streak.streak_days if streak else 0
        
#         #tao ban ghi tong ket cho user
#         monthly_summary = MonthlySummary(
#             user_id = user.id,
#             month = month,
#             total_distance=total_distance,
#             total_duration=total_duration,
#             total_activities=total_activities,
#             streak_days=streak_days
#         )
#         db.session.add(monthly_summary)
    
#     # lưu các thay đổi vào db
#     db.session.commit()
    
#     return jsonify({"message": "Monthly summaries created successfully!"}), 201

# Achievement
@app.route('/api/achievement', methods=['POST'])
def create_achievement():
    data = request.json
    user_id = data['user_id']
    achievement_type = data['achievement_type']  #streak, distance, top3
    
    #lay thoi gian hien tai
    achieved_on = datetime.now()
    
    # tao achievement cho tung loai
    if achievement_type == 'streak':
        streak = Streak.query.filter_by(user_id=user_id).first()
        if streak and streak.streak_days >= 20:  #giả sử mốc streak la 20
            achievement_name = f"Streak {streak.streak_days} days"
            description = f"Achieved a streak of {streak.streak_days} days."
            new_achievement = Achievement(user_id=user_id, name=achievement_name, achieved_on=achieved_on, description=description)
            db.session.add(new_achievement)
           
    elif achievement_type == 'distance':
        total_distance = sum([activity.distance for activity in Activity.query.filter_by(user_id=user_id).all()])
        if total_distance >= 100:
            achievement_name = "100 km Distance"
            description = f"Achieved a total distance of {total_distance} km."
            new_achievement = Achievement(user_id=user_id, name=achievement_name, achieved_on=achieved_on, description=description)
            db.session.add(new_achievement) 
            
    elif achievement_type == 'top_3':
        leaderboard = db.session.query(
            Activity.user_id,
            func.sum(Activity.distance).label('total_distance')
        ).group_by(Activity.user_id).order_by(func.sum(Activity.distance).desc()).limit(3).all()
        
        top_3_users = [row.user_id for row in leaderboard]
        if user_id in top_3_users:
            achievement_name = "Top 3 Leaderboard"
            description = f"Achieved top 3 in the leaderboard."
            new_achievement = Achievement(user_id=user_id, name=achievement_name, achieved_on=achieved_on, description=description)
            db.session.add(new_achievement)              
            
        db.session.commit()
        return jsonify({"message": "Achievement created successfully!"}), 201    