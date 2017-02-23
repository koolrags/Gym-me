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
        PRIMARY KEY (`id`), \
    UNIQUE INDEX `id_UNIQUE` (`id` ASC), \
    UNIQUE INDEX `username_UNIQUE` (`username` ASC) \
)');

console.log("User Table Created");

connection.end();
