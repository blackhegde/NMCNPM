from app import app, db

if __name__ == '__main__':
    #Create database table if dont exist
    with app.app_context():
        db.create_all()

    #run the flask dev server
    app.run(debug=True)