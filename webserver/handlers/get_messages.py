
import base
import logging
import json
import MySQLdb

from utility import Status
import models.user
import models.message


class GetMessagesHandler(base.BaseHandler):
    def initialize(self, db_config):
        super(GetMessagesHandler, self).initialize(db_config)
        self.reqd_fields = ['username', 'password', 'topic_id', 'timestamp']


    def process(self, request, dbconn):
        logging.info("getting messages")

        auth_status = models.user.authenticate(request['username'], request['password'], dbconn, creatable=False)
        if auth_status != Status.SuccessfulAuthentication:
            self.error(auth_status)
            return

        user_in_topic_status = models.topic.user_in_topic(request['username'], request['topic_id'], dbconn)

        if user_in_topic_status != Status.UserAlreadyInTopic:
            self.error(user_in_topic_status)
            return

        messages = models.message.get_messages(request['topic_id'], request['timestamp'], dbconn)

        if type(messages) == list:
            response = {}
            response['data'] = messages
            self.write(json.dumps(response))
            self.finish()
            return
        else:
            self.error(messages)
            return