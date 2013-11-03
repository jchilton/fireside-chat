import logging
import sys
import os

import tornado.httpserver
import tornado.ioloop
import tornado.web

import handlers.join
import handlers.search_topics
import handlers.get_messages
import handlers.post_message


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

db_config_dict = dict(db_config=db_config)
application = tornado.web.Application([
    (r'/join', handlers.join.JoinHandler, db_config_dict),
    (r'/search_topics', handlers.search_topics.SearchTopicsHandler, db_config_dict),
    (r'/get_messages', handlers.get_messages.GetMessagesHandler, db_config_dict),
    (r'/post_message', handlers.post_message.PostMessageHandler, db_config_dict),
    (r'/tester/(.*)', tornado.web.StaticFileHandler, {'path': os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "tester")})
])

print os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "tester")
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
