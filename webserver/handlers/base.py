
import json
import logging

import tornado.web

import MySQLdb

class BaseHandler(tornado.web.RequestHandler):
    def initialize(self, db_config):
        self.db_config = db_config

    def error(self, errorcode):
        logging.info("error: " + str(errorcode))
        self.clear()
        self.set_header("Content-Type", "application/json")
        self.write('{"error": %s}' % str(errorcode))
        self.finish()


    def bad_request(self):
        logging.info("bad request")
        self.clear()
        self.set_status(400)
        self.finish()

    def success(self, statuscode):
        logging.info("success")
        self.clear()
        self.set_status(200)
        self.set_header("Content-Type", "application/json")
        self.write('{"success": %s}' % int(statuscode))
        self.finish()

    def db_error(self):
        self.error(1)

    def get(self):
        self.bad_request()

    # Parse the request for all required fields and get a database connection
    # Then hand these off to the worker function
    def post(self):
        try:
            request = json.loads(self.request.body)
            print self.request.body
            print request
        except ValueError:
            logging.info("Bad request: " + self.request.body[:50])
        for field in self.reqd_fields:
            if field not in request:
                self.bad_request()
                return

        dbc = self.db_config
        try:
            conn = MySQLdb.connect(host=dbc['host'],
                               port=dbc['port'],
                               user=dbc['username'],
                               passwd=dbc['password'],
                               db=dbc['database'])
        except MySQLdb.Error as e:
            logging.error("MySQL connxn error: " + e[1])
            self.db_error()
            return

        self.process(request, conn)
