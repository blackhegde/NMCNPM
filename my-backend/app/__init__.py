from flask import Flask
from flask_cors import CORS
from flask_sqlalchemy import SQLAlchemy
# from flask_apscheduler import APScheduler
from config import app_config
from datetime import datetime
# from .routes import setup_routes
# from dotenv import load_dotenv

app = Flask(__name__)
app.config.from_object(app_config['develop_config'])
CORS(app)  # Cho phép CORS
db = SQLAlchemy(app)
# scheduler = APScheduler()
# scheduler.init_app

# # Hàm kiểm tra streak sắp mất
# def check_streak_warnings():
#     now = datetime.now()
#     warning_time = now.replace(hour=19, minute=0, second=0, microsecond=0)
    
#     if now >= warning_time:
#         today = datetime.utcnow().date()
#         users = User.query.all()

#         for user in users:
#             streak = Streak.query.filter_by(user_id=user.id, is_active=True).first()
#             if streak:
#                 # Kiểm tra nếu người dùng không có hoạt động hôm nay
#                 last_activity = Activity.query.filter_by(user_id=user.id).order_by(Activity.end_time.desc()).first()
                
#                 if last_activity is None or last_activity.end_time.date() < today:
#                     print(f"Warning: User {user.username}'s streak is at risk of ending!")
#                     # Gửi thông báo hoặc xử lý logic cảnh báo tại đây (VD: lưu vào DB, gửi push notification)

# # Thêm job định kỳ chạy lúc 7h tối
# scheduler.add_job(id='streak_warning_job', func=check_streak_warnings, trigger='cron', hour=19, minute=0)

# scheduler.start()

# setup_routes(app)
from app import routes