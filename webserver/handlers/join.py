
import base
import logging
import json
import models.user

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

        


