import logging
import sys

import tornado.httpserver
import tornado.ioloop
import tornado.web

import handlers.join

db_config = {}
try:
    database_config_file = open('db_config')
    db_config['host'] = database_config_file.readline()[:-1]
    db_config['port'] = database_config_file.readline()[:-1]
    db_config['username'] = database_config_file.readline()[:-1]
    db_config['password'] = database_config_file.readline()[:-1]
except IOError, e:
    logging.critical("Error reading db config: " + e)
    sys.exit(1)

application = tornado.web.Application([
    (r'/join', handlers.join.JoinHandler, dict(db_config=db_config))
])

if __name__ == '__main__':
    http_server = tornado.httpserver.HTTPServer(application)
    http_server.listen(80)
    tornado.ioloop.IOLoop.instance().start()
