import MySQLdb
import logging

import time

from utility import Status
import user

def get_messages(topic_id, timestamp, dbconn):
    try:
        cursor = dbconn.cursor()
    except MySQLdb.Error:
        return Status.DBError

    try:
        time.strptime(timestamp, '%Y-%m-%d %H:%M:%S')
    except ValueError:
        logging.warn('get_messages bad timestamp')
        return Status.MalformedTimestamp

    messageSQL = "SELECT U.username, M.message, M.timestamp FROM messages M INNER JOIN \
                    users U ON M.user_id=U.user_id WHERE M.topic_id='%s' AND M.timestamp >= '%s'" % (topic_id, timestamp)

    try:
        num = cursor.execute(messageSQL)
    except MySQLdb.Error as e:
        logging.error("MySQL error in get_messages: " + e[1])
        return Status.DBError

    messages = []
    for i in range(num):
        result = cursor.fetchone()
        messages.append({'username': result[0], 'message': result[1], 'timestamp': str(result[2])})

    return messages


'''
Before calling this method, you should authenticate the user and verify that the user is in the topic.
'''
def post_message(topic_id, username, message, dbconn):
    logging.info('posting message on topic %s from user %s' % (topic_id, username))
    username = MySQLdb.escape_string(username)
    try:
        cursor = dbconn.cursor()
        user_id = user.user_id_from_username(username, dbconn)
    except MySQLdb.Error as e:
        logging.error("MySQL error in add_user 1: " + e[1])
        return Status.DBError

    messageSQL = "INSERT INTO messages(user_id, topic_id, message, timestamp) VALUES ('%s', '%s', '%s', NOW())"
    messageSQL %= (user_id, topic_id, MySQLdb.escape_string(message))

    try:
        cursor.execute(messageSQL)
        return Status.SuccessfulPost
    except MySQLdb.Error as e:
        logging.error("MySQL error on message post: " + e[1])
        return Status.DBError