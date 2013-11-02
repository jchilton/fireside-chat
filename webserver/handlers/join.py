
import base

class JoinHandler(base.BaseHandler):
    def initialize(self):
        self.reqd_fields = ['username', 'password', 'topic', 'oneonone']


    def process(self, request):
        self.bad_request()

