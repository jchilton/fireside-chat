
from utility import Status
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
Returns True if the password matches for the given user, otherwise False

Will throw a MySQLdb.Error if something goes wrong; the caller must catch
'''
def password_matches(username, password, cursor):
    authUserSQL = "SELECT password_hash FROM users WHERE username='%s'" % username
    result = cursor.execute(authUserSQL)
    if result == 0:
        return False
    db_hash = cursor.fetchone()[0]
    return bcrypt.hashpw(password, db_hash) == db_hash

'''
Returns True if the username is at least 3 characters
False otherwise
'''
def new_user_username_check(username):
    return len(username.strip()) >= 3

'''
Returns one of:
    Status.InvalidUsername
    Status.UserCreated
    Status.CouldNotCreateUser
'''
def create_user(username, password_hash, cursor):
    createUserSQL = "INSERT INTO users (username, password_hash) VALUES ('%s', '%s')" % (username, password_hash)
    if cursor.execute(createUserSQL):
        return Status.UserCreated
    else:
        return Status.CouldNotCreateUser

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
def authenticate(username, password, dbconn, creatable=True):
    try:
        cursor = dbconn.cursor()
    except MySQLdb.Error as e:
        logging.error('error getting cursor: authenticate: ' + e[1])
        return Status.MySQLError

    password_hash = bcrypt.hashpw(password, bcrypt.gensalt())
    username = MySQLdb.escape_string(username)

    try:
        if user_exists(username, cursor):
            if password_matches(username, password, cursor):
                return Status.SuccessfulAuthentication
            else:
                return Status.UnsuccessfulAuthentication
        else:
            if not creatable:
                return Status.UserDoesNotExist
            if new_user_username_check(username):
                if new_user_password_check(password):
                    result = create_user(username, password_hash, cursor)
                    dbconn.commit()
                    return result
                else:
                    return Status.InvalidPassword
            else:
                return Status.InvalidUsername

    except MySQLdb.Error as e:
        logging.error("Mysql error in authenticate: " + e[1])
        return Status.MySQLError


'''
Returns a Status.NoSuchUser object if there is no such user, or returns a the user id if
there is a user with the given username. This function does raise MySQL errors, so catch them.
'''
def user_id_from_username(username, dbconn):
    username = MySQLdb.escape_string(username)
    cursor = dbconn.cursor()
    idSQL = "SELECT user_id FROM users WHERE username='%s'" % username
    if cursor.execute(idSQL) == 0:
        return Status.NoSuchUser()
    return cursor.fetchone()[0]