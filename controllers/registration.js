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
			phone
			address
			usertags
			description
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
			error messages:s
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
	if (req.body.phone===undefined) {
		errormsg += "Phone undefined :";
	}
	if (req.body.address===undefined) {
		errormsg += "Address undefined :";
	}
	if (req.body.tags===undefined) {
		errormsg += "Tags undefined :";
	}
	if (req.body.description===undefined) {
		errormsg += "Description undefined :";
	}
	if (errormsg != "") {
		resp.errormsg = errormsg;
	}

	var selectQuery = "SELECT Count(Id) AS 'count' FROM Users u WHERE u.email=" + connection.escape(req.body.email);
	console.log(selectQuery);
	connection.query(selectQuery, function(err, rows, fields) {
		console.log("ROWS:");
		console.log(rows);
		if (err) {
			resp.success = false;
			console.log("Here");
			resp.errormsg = "db failure";
			res.end(JSON.stringify(resp));
		}
		else {
			if(rows[0].count != 0) {
				resp.success = false;
				resp.errormsg = "Email is already in use.";
				res.end(JSON.stringify(resp));
			}
		}
	});

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var name = connection.escape(req.body.name);
		var email = connection.escape(req.body.email);
		var password = connection.escape(req.body.password);
		var phone = connection.escape(req.body.phone);
		var address = connection.escape(req.body.address);
		var tags = connection.escape(req.body.tags);
		var description = connection.escape(req.body.description);

		var insertQuery = "INSERT INTO Users ( username, password, name, email, phone, address, tags, description) values  (" + email + "," + password + "," + name  + "," + email  + "," + phone + "," + address + "," + tags + "," + description + ")";
		console.log(insertQuery);
		connection.query(insertQuery, function(err, rows, fields) {
			resp.success = true;
			if (err) {
				resp.success = false;
				resp.errormsg = "db entry failed";
			}
			res.end(JSON.stringify(resp));
		});
		connection.release();
	}
}