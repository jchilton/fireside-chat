


class Status(object):
    DBError = 1
    SuccessfulAuthentication = 2
    UnsuccessfulAuthentication = 3
    UserCreated = 4
    InvalidPassword = 5
    NoSuchUser = 6
    class NoSuchUser(object):
        pass
    NoSuchTopic = 7
    class TopicCreated(object):
        def __init__(self, topic_id):
            self.topic_id = topic_id
    UserAddedToTopic = 8
    UserAlreadyInTopic = 9
    CouldNotAddUser = 10
    CouldNotIncrementUserCount = 11
    InvalidUsername = 12
    UserDoesNotExist = 13
    UserNotInTopic = 14
    MalformedTimestamp = 15
    SuccessfulPost = 16



class Config(object):
    SessionTimeout = 12 * 60 * 60       # Session timeout in seconds (12 hours)


