


class Status(object):
    MySQLError = 1
    SuccessfulAuthentication = 2
    UnsuccessfulAuthentication = 3
    UserCreated = 4
    InvalidPassword = 5


class Config(object):
    SessionTimeout = 12 * 60 * 60       # Session timeout in seconds (12 hours)


