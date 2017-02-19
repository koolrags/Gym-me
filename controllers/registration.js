var bcrypt = require('bcrypt-nodejs');
var qs      = require('querystring');
var mysql   = require('mysql');

/*
	Function to register new user.

	# post params:
		required:
			name
			email
			password
		optional:
			[none]
	
	# response structures:
		{
			success: true
		}
		----------------------
		{
			success: false,
			errormsg: "error messages defined below"
		}
			error messages:
				* Hashing error
					salt generation fails
					actual hashing fails
				* db entry failed
					possibly duplicate entry for email
					actual db connection failure 

*/
module.exports.register = function(req, res, connection) {
	var resp = {};
	console.log(req.body);

	resp.success = false;
	errormsg = "";
	if (req.body.name===undefined) {
		errormsg += "name undefined :";
	}
	if (req.body.email===undefined) {
		errormsg += "email undefined :";
	}
	if (req.body.password===undefined) {
		errormsg += "password undefined :";
	}
	if (errormsg != "") {
		resp.errormsg = errormsg;
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		console.log("Here");
		var name = connection.escape(req.body.name);
		var email = connection.escape(req.body.email);
		var password = connection.escape(req.body.password);

		var insertQuery = "INSERT INTO Users ( username, password, name, email, phone, address) values  (" + email + "," + password + "," + name  + "," + email  + "," + "''" + "," + "''" + ")";
		console.log(insertQuery);
		connection.query(insertQuery, function(err, rows, fields) {
			resp.success = true;
			if (err) {
				resp.success = false;
				resp.errormsg = "db entry failed";
			}
			res.end(JSON.stringify(resp));
		});
	}
}