
import base
import logging
import json
import models.user
import models.topic

from utility import Status


class JoinHandler(base.BaseHandler):
    def initialize(self, db_config):
        super(JoinHandler, self).initialize(db_config)

        self.reqd_fields = ['username', 'password', 'topic', 'oneonone']


    def process(self, request, dbconn):
        logging.info("trying to authenticate")
        auth_status = models.user.authenticate(request['username'], request['password'], dbconn)

        if auth_status != Status.SuccessfulAuthentication and auth_status != Status.UserCreated:
            self.error(auth_status)
            return

        #Otherwise the login was successful
        # If there is an open topic, do nothing right now
        topic_to_join = models.topic.is_topic_open(request['topic'], request['oneonone'], dbconn)
        if topic_to_join:
            pass
        # If there's not, then create a new one
        else:
            create_topic_status = models.topic.create_topic(request['topic'], request['oneonone'], dbconn)
            if type(create_topic_status) != Status.TopicCreated:
                self.error(create_topic_status)
                return
            topic_to_join = create_topic_status.topic_id
        # Then add the user to the topic
        logging.debug('%s %s' % (request['username'], topic_to_join))
        add_user_status = models.topic.add_user(request['username'], topic_to_join, dbconn)
        if add_user_status == Status.UserAddedToTopic or add_user_status == Status.UserAlreadyInTopic:
            dbconn.commit()
            self.success(add_user_status, {'topic_id': topic_to_join})
            return
        else:
            dbconn.rollback()
            self.error(add_user_status)
            return




