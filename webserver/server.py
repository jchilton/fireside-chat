import logging
import sys

import tornado.httpserver
import tornado.ioloop
import tornado.web

import handlers.join
import handlers.search_topics


db_config = {}
try:
    database_config_file = open('db_config')
    db_config['host'] = database_config_file.readline()[:-1]
    db_config['port'] = int(database_config_file.readline()[:-1])
    db_config['username'] = database_config_file.readline()[:-1]
    db_config['password'] = database_config_file.readline()[:-1]
    db_config['database'] = database_config_file.readline()[:-1]
except IOError, e:
    logging.critical("Error reading db config: " + str(e))
    sys.exit(1)
except ValueError:
    logging.critical("Need numeric port")
    sys.exit(1)

application = tornado.web.Application([
    (r'/join', handlers.join.JoinHandler, dict(db_config=db_config)),
    (r'/search_topics', handlers.search_topics.SearchTopicsHandler, dict(db_config=db_config))
])

if __name__ == '__main__':
    logging.basicConfig(level=logging.DEBUG)

    http_server = tornado.httpserver.HTTPServer(application)
    try:
        http_server.listen(80)
        logging.info("Started up!")
    except Exception as e:
        logging.critical("startup error: " + str(e))
        sys.exit(1)
    tornado.ioloop.IOLoop.instance().start()
