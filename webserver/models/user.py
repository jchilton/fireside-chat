
import utility
import logging
import MySQLdb
import bcrypt


'''
Returns boolean whether the password can be used for a new user. This might someday check that the password
is alphanumeric with symbols and between X and Y characters, but for now only checks that the password
is at least 8 characters long.
'''
def new_user_password_check(password):
    return len(password) > 7

'''
Returns the number of rows with the given username (should be 1 or 0)

Will throw a MySQLdb.Error if something goes wrong; the caller must catch
'''
def user_exists(username, cursor):
    userExistsSQL = "SELECT user_id FROM users WHERE username='%s'" % username
    return cursor.execute(userExistsSQL)

'''
Returns the number of rows with the given username and password hash (should be 1 or 0)

Will throw a MySQLdb.Error if something goes wrong; the caller must catch
'''
def password_matches(username, password, cursor):
    authUserSQL = "SELECT password_hash FROM users WHERE username='%s'" % username
    result = cursor.execute(authUserSQL)
    if result == 0:
        return False
    db_hash = cursor.fetchone()[0]
    return bcrypt.hashpw(password, db_hash) == db_hash

def create_user(username, password_hash, cursor):
    createUserSQL = "INSERT INTO users (username, password_hash) VALUES ('%s', '%s')" % (username, password_hash)
    return cursor.execute(createUserSQL)

'''
Tries to authenticate a user with the given username and password, and if the username does not exist, will create
the new user with the given password.

Possible return values:
    MySQLError
    SuccessfulAuthentication
    UnsuccessfulAuthentication
    UserCreated
    InvalidPassword
'''
def authenticate(username, password, dbconn):
    try:
        cursor = dbconn.cursor()
    except MySQLdb.Error:
        return utility.Status.MySQLError

    password_hash = bcrypt.hashpw(password, bcrypt.gensalt())
    username = MySQLdb.escape_string(username)

    try:
        if user_exists(username, cursor):
            logging.info("loggin' in %s with %s (hashed %s)" % (username, password, password_hash))
            if password_matches(username, password, cursor):
                return utility.Status.SuccessfulAuthentication
            else:
                return utility.Status.UnsuccessfulAuthentication
        else:
            if new_user_password_check(password):
                create_user(username, password_hash, cursor)
                dbconn.commit()
                return utility.Status.UserCreated
            else:
                return utility.Status.InvalidPassword

    except MySQLdb.Error as e:
        logging.error("Mysql error in authenticate: " + e[1])
        return utility.Status.MySQLError


