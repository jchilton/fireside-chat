import MySQLdb
import logging

import user
from utility import Status

'''
Returns a topic id if there is a chat that matches the given parameters, otherwise False.
Handles database errors, returning utility.Status.DBError when they occur
'''
def is_topic_open(topic_name, oneonone, dbconn):
    try:
        cursor = dbconn.cursor()
    except MySQLdb.Error:
        return Status.DBError

    topic = MySQLdb.escape_string(topic_name)
    if oneonone != 'Y':
        oneonone = 'N'
    findSQL = "SELECT topic_id, num_users FROM topics WHERE name='%s' AND oneonone='%s'" % (topic_name, oneonone)

    num = cursor.execute(findSQL)
    # If there are no matches for this type of chat (topic name, oneonone status), return False
    if num == 0:
        return False

    result = cursor.fetchone()
    # There are matches. If it's a one-on-one, is there still room?
    if oneonone == 'Y':
        if result[1] == 1:
            return result[0]
        else:
            return False
    # Not one on one. There's always room
    else:
        return result[0]


'''
Create a topic with given name and oneonone status.

Returns either a Status.TopicCreated or Status.DBError
'''
def create_topic(topic_name, oneonone, dbconn):
    try:
        cursor = dbconn.cursor()
    except MySQLdb.Error as e:
        logging.error("mysql error on create topic: " + e[1])
        return Status.DBError

    topic_name = MySQLdb.escape_string(topic_name)
    if oneonone != 'Y':
        oneonone = 'N'
    createSQL = "INSERT INTO topics (name, num_users, oneonone) VALUES('%s', 0, '%s')" % (topic_name, oneonone)
    try:
        cursor.execute(createSQL)
        return Status.TopicCreated(cursor.lastrowid)
    except MySQLdb.Error as e:
        logging.error("MySQL error in create topic: " + e[1])
        return Status.DBError


'''
Adds a user to a topic. This function will return Status.UserAdded, and will return something from Status on failure,
so the possible return values are:
   a Status.NoSuchUser object
   Status.CouldNotAddUser
   Status.CouldNotIncrementUserCount
   Status.UserAddedToTopic

Note that True will be returned and no database change made if the user is already in the topic.

This function will increment the count of users in the topics table and add a row to the topic_users table
'''
def add_user(username, topic_id, dbconn):
    logging.info('Adding user %s to topic %s' % (username, topic_id))
    try:
        cursor = dbconn.cursor()
        user_id = user.user_id_from_username(username, dbconn)
    except MySQLdb.Error as e:
        logging.error("MySQL error in add_user 1: " + e[1])
        return Status.DBError
    if type(user_id) == Status.NoSuchUser:
        return user_id
    findSQL = "SELECT * FROM topic_users WHERE user_id='%s' AND topic_id='%s'" % (user_id, topic_id)

    try:
        result = cursor.execute(findSQL)
    except MySQLdb.Error as e:
        logging.error("MySQL error in add_user 2: " + e[1])
        return Status.DBError

    if result == 1:
        logging.info('User already in topic')
        return Status.UserAlreadyInTopic

    insertSQL = "INSERT INTO topic_users (topic_id, user_id) VALUES('%s', '%s')" % (topic_id, user_id)
    try:
        result = cursor.execute(insertSQL)
    except MySQLdb.Error as e:
        logging.error("MySQL error in add_user 3: " + e[1])
        return Status.DBError

    if result != 1:
        logging.error("Found an oddity in add_user: couldn't insert!")
        return Status.CouldNotAddUser

    updateSQL = "UPDATE topics SET num_users=(num_users+1) where topic_id='%s'" % topic_id
    try:
        result = cursor.execute(updateSQL)
        logging.debug('just ran %s: %s' % (updateSQL, result))
    except MySQLdb.Error as e:
        logging.error("MySQL error in add_user 4: "  + e[1])
        return Status.CouldNotIncrementUserCount

    logging.info('User added to topic')
    return Status.UserAddedToTopic
