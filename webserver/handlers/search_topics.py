
import base
import logging
import json
import MySQLdb

from utility import Status


class SearchTopicsHandler(base.BaseHandler):
    def initialize(self, db_config):
        super(SearchTopicsHandler, self).initialize(db_config)

        self.reqd_fields = ['prefix']


    def process(self, request, dbconn):
        logging.info("searching for topics")

        searchSQL = "select distinct name from topics where topics.name like '%s' order by name asc"
        searchSQL %= MySQLdb.escape_string(request['prefix']) + '%'

        logging.debug('search sql: %s' % searchSQL)
        results = []
        try:
            cursor = dbconn.cursor()
            num = cursor.execute(searchSQL)
            for i in range(num):
                results.append(cursor.fetchone()[0])
        except MySQLdb.Error as e:
            logging.error("MySQL error in topic search: " + e[1])
            self.error(Status.DBError)
            return

        response = {}
        response['data'] = results

        self.write(json.dumps(response))
        self.finish()
        return