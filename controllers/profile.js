var bcrypt = require('bcrypt-nodejs');


module.exports.getProfile = function(req, res, connection) {
	var resp = {};

	resp.success = false;
	errormsg = "";
	if (req.body.email===undefined) {
		errormsg += "email undefined :";
	}
	if (errormsg != "") {
		resp.errormsg = errormsg;
	}

	var email = connection.escape(req.body.email);
	var password = connection.escape(req.body.password);

	var query = "SELECT u.username, u.name, u.email, u.phone, u.address, u.tags, u.description, CONVERT(u.image USING utf8) as 'image', schedule, location, maxdistance FROM Users u WHERE u.email=" + email;
	console.log(query);
	connection.query(query, function(err, rows, fields) {
	    if (err) {
	        resp.success = false;
	       	resp.errormsg = "db entry failed";
	    }
	    else {

	    	if (rows.length == 0)
	    	{
	    		resp.success = false;
	    		resp.errormsg = "No user found.";
	    	}
	    	else
	    	{
	        	resp.success = true;
	    		resp.profile = rows;
	    	}
	    }
    	res.end(JSON.stringify(resp));
	});
}


module.exports.updateProfile = function(req, res, connection) {
	var resp = {};
	
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

	var name = connection.escape(req.body.name);
	var email = connection.escape(req.body.email);
	var password = connection.escape(req.body.password);
	var phone = connection.escape(req.body.phone);
	var address = connection.escape(req.body.address);
	var tags = connection.escape(req.body.tags);
	var description = connection.escape(req.body.description);

	var updateQuery = "UPDATE Users u SET u.name =" + name + ", u.phone =" + phone + ", u.address =" + address + ", u.tags =" + tags +  ", u.description =" + description + " WHERE u.email=" + email + " AND u.password="+password;
	console.log(updateQuery);
	connection.query(updateQuery, function(err, rows, fields) {
		resp.success = true;
		if (err) {
			resp.success = false;
			resp.errormsg = "db entry failed";
		}
		res.end(JSON.stringify(resp));
	});
}

module.exports.updateProfilePicture = function(req, res, connection) {
	var resp = {};
	resp.success = false;

	errormsg = "";
	if (req.body.email===undefined) {
		errormsg += "email undefined :";
	}
	if (req.body.password===undefined) {
		errormsg += "password undefined :";
	}
	if (req.body.image===undefined) {
		errormsg += "Image undefined :";
	}
	if (errormsg != "") {
		resp.errormsg = errormsg;
	}

	var email = connection.escape(req.body.email);
	var password = connection.escape(req.body.password);
	var image = connection.escape(req.body.image);

	var updateQuery = "UPDATE Users u SET u.image =" + image + " WHERE u.email=" + email + " AND u.password="+password;
	console.log(updateQuery);
	connection.query(updateQuery, function(err, rows, fields) {
		resp.success = true;
		if (err) {
			resp.success = false;
			resp.errormsg = "db entry failed";
		}
		res.end(JSON.stringify(resp));
	});
}


module.exports.getAllProfiles = function(req, res, connection) {
	var resp = {};

	resp.success = false;
	errormsg = "";
	if (req.body.email===undefined) {
		errormsg += "email undefined :";
	}
	if (errormsg != "") {
		resp.errormsg = errormsg;
	}

	var email = connection.escape(req.body.email);
	var password = connection.escape(req.body.password);

	var query = "SELECT u.username FROM Users u WHERE u.email NOT LIKE " + email + " AND u.username NOT IN (SELECT b.blockee FROM user_block b WHERE b.blocker = " + email + " )";
	console.log(query);
	connection.query(query, function(err, rows, fields) {
	    if (err) {
	        resp.success = false;
	       	resp.errormsg = "db entry failed";
	    }
	    else {

	    	if (rows.length == 0)
	    	{
	    		resp.success = false;
	    		resp.errormsg = "No users found.";
	    	}
	    	else
	    	{
	        	resp.success = true;
	    		resp.profiles = rows;
	    	}
	    }
    	res.end(JSON.stringify(resp));
	});
}
module.exports.addschedule = function(req, res, connection) {
	var resp = {};

	resp.success = false;
	errormsg = "";
	if (req.body.email===undefined) {
		errormsg += "email undefined :";
	}

	if (req.body.schedule===undefined) {
		errormsg += "Schedule undefined :";
	}
	if (errormsg != "") {
		resp.errormsg = errormsg;
	}

	var email = connection.escape(req.body.email);
	var schedule = connection.escape(req.body.schedule);

	var query = "UPDATE Users u SET u.schedule =" + schedule + " WHERE u.email= " + email;
	console.log(query);
	connection.query(query, function(err, rows, fields) {
		resp.success = true;
		if (err) {
			resp.success = false;
			resp.errormsg = "db entry failed";
		}
		res.end(JSON.stringify(resp));
	});
}

module.exports.addlocation = function(req, res, connection) {
	var resp = {};

	resp.success = false;
	errormsg = "";
	if (req.body.email===undefined) {
		errormsg += "email undefined :";
	}

	if (req.body.userlocation===undefined) {
		errormsg += "location undefined :";
	}

	if (errormsg != "") {
		resp.errormsg = errormsg;
	}

	var email = connection.escape(req.body.email);
	var userlocation = connection.escape(req.body.userlocation);

	var query = "UPDATE Users u SET u.location =" + userlocation + " WHERE u.email= " + email;
	console.log(query);
	connection.query(query, function(err, rows, fields) {
		resp.success = true;
		if (err) {
			resp.success = false;
			resp.errormsg = "db entry failed";
		}
		res.end(JSON.stringify(resp));
	});
}

module.exports.addmaxdistance = function(req, res, connection) {
	var resp = {};

	resp.success = false;
	errormsg = "";
	if (req.body.email===undefined) {
		errormsg += "email undefined :";
	}

	if (req.body.maxdistance===undefined) {
		errormsg += "max distance undefined :";
	}
	
	if (errormsg != "") {
		resp.errormsg = errormsg;
	}

	var email = connection.escape(req.body.email);
	var maxdistance = connection.escape(req.body.maxdistance);

	var query = "UPDATE Users u SET u.maxdistance =" + maxdistance + " WHERE u.email= " + email;
	console.log(query);
	connection.query(query, function(err, rows, fields) {
		resp.success = true;
		if (err) {
			resp.success = false;
			resp.errormsg = "db entry failed";
		}
		res.end(JSON.stringify(resp));
	});
}

module.exports.sortbyname = function(req, res, connection) {
	var resp = {}
	resp.success = false;

	if (req.body.username===undefined) {
		errormsg += "Username undefined :";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var username = connection.escape(req.body.username);
		username = username.replace("'","");
		username = username.replace("'","");
		var query = "SELECT DISTINCT name FROM users WHERE LOWER(name) LIKE UPPER('%" + username + "%')";
		console.log(query);
		connection.query(query, function(err, rows, fields) {
			if (err) {
				resp.success = false;
				resp.errormsg = "db failure";
				res.end(JSON.stringify(resp));
			}
			else {
				resp.success = true;
				resp.users = rows;
				res.end(JSON.stringify(resp));
			}
		});
	}
}

module.exports.reportabuse = function(req, res, connection) {
	var resp = {}
	resp.success = false;

	if (req.body.reporter===undefined) {
		errormsg += "Reporter email undefined :";
	}

	if (req.body.abuser===undefined) {
		errormsg += "Abuser email undefined :";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var reporter = connection.escape(req.body.reporter);
		var abuser = connection.escape(req.body.abuser);

		var insertQuery = "INSERT INTO abuse_report (reporter,abuser,status) VALUES ( " + reporter + " ," + abuser + "," + " 0)";
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
