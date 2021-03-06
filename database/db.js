var mysql = require('mysql');
var details = require('./details');

var connection = mysql.createConnection(details.connection);

connection.query('DROP DATABASE ' + details.database);
connection.query('CREATE DATABASE ' + details.database);

console.log("Database Created");

connection.query('\
CREATE TABLE `' + details.database + '`.`' + details.users_table + '` ( \
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT, \
    `username` VARCHAR(40) NOT NULL, \
    `password` CHAR(60) NOT NULL, \
    `name` VARCHAR(50) NOT NULL, \
    `email` VARCHAR(50) NOT NULL, \
    `phone` VARCHAR(10), \
    `address` VARCHAR(50), \
    `tags` VARCHAR(50), \
    `description` VARCHAR(50), \
    `image` MEDIUMBLOB, \
    `schedule` VARCHAR(5000), \
    `location` VARCHAR(5000), \
    `maxdistance` VARCHAR(50), \
        PRIMARY KEY (`id`), \
    UNIQUE INDEX `id_UNIQUE` (`id` ASC), \
    UNIQUE INDEX `username_UNIQUE` (`username` ASC) \
)');

console.log("User Table Created");

connection.query('\
CREATE TABLE `' + details.database + '`.`' + details.tags_table + '` ( \
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT, \
    `description` VARCHAR(40) NOT NULL, \
        PRIMARY KEY (`id`), \
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) \
)');

console.log("Tags Table Created");

connection.query('\
CREATE TABLE `' + details.database + '`.`' + details.tags_join_table + '` ( \
    `user_email` VARCHAR(50) NOT NULL, \
    `tag_description` VARCHAR(50) NOT NULL \
)');

console.log("User-Tag-Join Table Created");

var insertTag = "INSERT INTO gymme.Tags (description) values ('Power-lifter')";
connection.query(insertTag);
var insertTag = "INSERT INTO gymme.Tags (description) values ('Pro')";
connection.query(insertTag);
var insertTag = "INSERT INTO gymme.Tags (description) values ('Beginner')";
connection.query(insertTag);
var insertTag = "INSERT INTO gymme.Tags (description) values ('Trainer')";
connection.query(insertTag);

console.log("Sample Tags inserted");

connection.query('\
CREATE TABLE `' + details.database + '`.`' + details.user_join + '` ( \
    `sender_email` VARCHAR(50) NOT NULL, \
    `receiver_email` VARCHAR(50) NOT NULL, \
    `status` INT NOT NULL \
)');

console.log("User-Join Table Created");

connection.query('\
CREATE TABLE `' + details.database + '`.`' + details.user_block + '` ( \
    `blocker` VARCHAR(50) NOT NULL, \
    `blockee` VARCHAR(50) NOT NULL \
)');

console.log("User-Block Table Created");

connection.query('\
CREATE TABLE `' + details.database + '`.`' + details.abuse_report + '` ( \
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT, \
    `reporter` VARCHAR(50) NOT NULL, \
    `abuser` VARCHAR(50) NOT NULL, \
    `status` INT NOT NULL, \
        PRIMARY KEY (`id`), \
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) \
)');

console.log("Abuse-Report Table Created");

connection.query('\
CREATE TABLE `' + details.database + '`.`' + details.user_message + '` ( \
    `sender_email` VARCHAR(50) NOT NULL, \
    `receiver_email` VARCHAR(50) NOT NULL, \
    `message` LONGTEXT \
)');

console.log("User_Message Table Created");

connection.query('\
CREATE TABLE `' + details.database + '`.`' + details.shared_schedule + '` ( \
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT, \
    `user_1` VARCHAR(50) NOT NULL, \
    `user_2` VARCHAR(50) NOT NULL, \
    `schedule` VARCHAR(5000) NOT NULL, \
        PRIMARY KEY (`id`), \
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) \
)');

console.log("shared schedule Table Created");

connection.query('\
CREATE TABLE `' + details.database + '`.`' + details.user_goals + '` ( \
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT, \
    `user_email` VARCHAR(50) NOT NULL, \
    `current` VARCHAR(5000) NOT NULL, \
    `finished` VARCHAR(5000) NOT NULL, \
        PRIMARY KEY (`id`), \
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) \
)');

console.log("User Goals Table Created");

connection.end();
