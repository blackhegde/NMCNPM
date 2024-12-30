from app import app, db
import os

if __name__ == '__main__':
    #Create database table if dont exist
    with app.app_context():
        db.create_all()

    #run the flask dev server
    port = int(os.environ.get('PORT', 5000))
    app.run(host= '0.0.0.0', port=port, debug=True)