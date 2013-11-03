
import base
import logging
import MySQLdb

from utility import Status
import models.user
import models.message


class PostMessageHandler(base.BaseHandler):
    def initialize(self, db_config):
        super(PostMessageHandler, self).initialize(db_config)
        self.reqd_fields = ['username', 'password', 'topic_id', 'message']


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

        post_status = models.message.post_message(request['topic_id'], request['username'], request['message'], dbconn)

        if post_status == Status.SuccessfulPost:
            self.success(post_status)
            return
        else:
            self.error(post_status)
            return