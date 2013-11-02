
import json
import logging

import tornado.web

import MySQLdb

class BaseHandler(tornado.web.RequestHandler):
    def initialize(self, db_config):
        self.db_config = db_config


    def bad_request(self):
        self.clear()
        self.set_status(400)
        self.finish()
        return

    def db_error(self):
        self.clear()
        self.finish('{"error": 1}')
        return

    def get(self):
        self.bad_request()

    # Parse the request for all required fields and get a database connection
    # Then hand these off to the worker function
    def post(self):
        try:
            request = json.loads(self.request.body)
        except ValueError:
            logging.info("Bad request: " + self.request.body[:50])
        for field in self.reqd_fields:
            if not hasattr(request, field):
                self.bad_request()

        dbc = self.db_config
        try:
            conn = MySQLdb.connect(host=dbc['host'],
                               port=dbc['port'],
                               user=dbc['username'],
                               passwd=dbc['password'],
                               db=dbc['database'])
        except MySQLDB.Error as e:
            logging.critical("MySQL connxn error: " + e[1])
            self.db_error()

        self.process(request, conn)
