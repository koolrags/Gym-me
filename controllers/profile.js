var bcrypt = require('bcrypt-nodejs');


/*
	Function to get user details.

	# post params:
		required:
			user
			password
		optional:
			[none]
	
	# response structures:
		{
			success: true
			profile: [
    			{
			      "id": 8,
			      "username": "dsds",
			      "name": null,
			      "email": "dssadsa",
			      "phone": "IN",
			      "address": 12345,
			      "tags": 8722221111
			    }
			]
		}
		----------------------
		{
			success: false,
			errormsg: "error messages defined below"
		}
			error messages:
				* db entry failed
*/
module.exports.getProfile = function(req, res, connection) {
	var resp = {};
	resp.success = false;

	var email = connection.escape(req.body.email);
	var password = connection.escape(req.body.password);

	var query = "SELECT u.username, u.name, u.email, u.phone, u.address, u.tags, u.description, CONVERT(u.image USING utf8) as 'image' FROM Users u WHERE u.email=" + email + " AND u.password="+password;
	connection.query(query, function(err, rows, fields) {
	    if (err) {
	        resp.success = false;
	       	resp.errormsg = "db entry failed";
	    }
	    else {

	    	if (rows.length == 0)
	    	{
	    		resp.success = false;
	    		resp.errormsg = "Incorrect email or password";
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


/*
	Function to get user details.

	# post params:
		required:
			user
			password
			name
			phone
			address
			tags
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
				* db entry failed
*/

module.exports.updateProfile = function(req, res, connection) {
	var resp = {};
	resp.success = false;

	var name = connection.escape(req.body.name);
	var email = connection.escape(req.body.email);
	var password = connection.escape(req.body.password);
	var phone = connection.escape(req.body.phone);
	var address = connection.escape(req.body.address);
	var tags = connection.escape(req.body.tags);
	var description = connection.escape(req.body.description);

	var updateQuery = "UPDATE Users u SET u.name =" + name + ", u.phone =" + phone + ", u.address =" + address + ", u.tags =" + tags +  ", u.description =" + description + " WHERE u.email=" + email + " AND u.password="+password;
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

	var email = connection.escape(req.body.email);
	var password = connection.escape(req.body.password);
	var image = connection.escape(req.body.image);

	var updateQuery = "UPDATE Users u SET u.image =" + image + " WHERE u.email=" + email + " AND u.password="+password;
	connection.query(updateQuery, function(err, rows, fields) {
		resp.success = true;
		if (err) {
			resp.success = false;
			resp.errormsg = "db entry failed";
		}
		res.end(JSON.stringify(resp));
	});
}

