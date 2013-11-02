import json
import logging
import tornado.httpserver
import tornado.ioloop
import tornado.web

import MySQLdb

class BaseHandler(tornado.web.RequestHandler):
    def bad_request(self):
        self.clear()
        self.set_status(400)
        self.finish()
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
        


        self.process(request)




class JoinHandler(tornado.web.RequestHandler):
    def process(self, request):
