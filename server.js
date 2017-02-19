var http    = require('http');
var url     = require('url');
var request = require('request');
var qs      = require('querystring');
var mysql   = require('mysql');
var express = require('express');
var bodyParser = require('body-parser');

var registration     = require('./controllers/registration');
var login            = require('./controllers/login');
var profile          = require('./controllers/profile');

console.log("Imports done");

var pool = mysql.createPool({
    connectionLimit : 50,
    host     : 'localhost',
    user     : 'root',
    password : 'manchesterisred',
    database : 'gymme',
    multipleStatements: true
});

console.log("MySQL connection pool created");

// var app = express();
// app.use(bodyParser.urlencoded({extended: true}));
// app.use(bodyParser.json());

// var ipaddress = '127.0.0.1';
// var port = 8080;

// app.get('/', function(req, res) {
//     res.end("{ success: true}");
// });
// app.post('/register', registration.register);
// app.listen(port, ipaddress, function() {
//     console.log('%s: Node server started on %s:%d ...', Date(Date.now()), ipaddress, port);
// });

var MasterApp = function() {

    var self = this;

    self.setupVariables = function() {
        self.ipaddress = '127.0.0.1';
        self.port      = 8080;
    };

    self.initializeServer = function() {
        self.app = express();
        self.app.use(express.static('./public'));
        self.app.use(bodyParser.urlencoded({extended: true}));
        self.app.use(bodyParser.json());

        self.mainRouter = express.Router();
        self.app.use('/', self.mainRouter);

        self.mainRouter.post('/login', function(req, res) {
            pool.getConnection(function(err, connection) {
                login.login(req, res, connection);       
            });
        });
        self.mainRouter.post('/register', function(req, res) {
            pool.getConnection(function(err, connection) {
                registration.register(req, res, connection);       
            });
        });
        self.mainRouter.post('/profile', function(req, res) {
            pool.getConnection(function(err, connection) {
                profile.getProfile(req, res, connection);       
            });
        });

        self.app.use(function(req, res) {
            res.status(400);
            res.render('error', {error: '404: File Not Found'});
        });
    };

    self.initialize = function() {
        self.setupVariables();
        self.initializeServer();
    };

    self.start = function() {
        self.app.listen(self.port, self.ipaddress, function() {
            console.log('%s: Node server started on %s:%d ...', Date(Date.now()), self.ipaddress, self.port);
        });
    };

}; 

var server = new MasterApp();
server.initialize();
server.start();