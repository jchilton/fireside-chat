CREATE TABLE `fireside`.`users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password_hash` VARCHAR(60) NOT NULL,
  PRIMARY KEY (`user_id`));


CREATE TABLE `fireside`.`topics` (
  `topic_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `num_users` INT NOT NULL,
  `oneonone` ENUM('Y','N') NULL,
  PRIMARY KEY (`topic_id`));



CREATE TABLE `fireside`.`messages` (
  `message_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `topic_id` INT NOT NULL,
  `message` VARCHAR(2000) NOT NULL,
  `timestamp` DATETIME NOT NULL,
  PRIMARY KEY (`message_id`));

CREATE TABLE `fireside`.`topic_users` (
  `topic_id` INT NOT NULL,
  `user_id` INT NOT NULL);

-- SELECT T.name, U.username FROM topic_users TU INNER JOIN topics T on TU.topic_id=T.topic_id INNER JOIN users U on TU.user_id=U.user_id;
