var bcrypt = require('bcrypt-nodejs');


/*
	Function to get user details.

	# post params:
		required:
			user
			password
			token
		optional:
			[none]
	
	# response structures:
		{
			success: true
			profile: [
    			{
			      "id": 8,
			      "address_line1": "dsds",
			      "address_line2": null,
			      "address_city": "dssadsa",
			      "address_state": "IN",
			      "address_zip": 12345,
			      "phone": 8722221111
			    },
			    {
			      "id": 9,
			      "address_line1": "saSas",
			      "address_line2": null,
			      "address_city": "DSADSADAD",
			      "address_state": "NC",
			      "address_zip": 12342,
			      "phone": 4442221111
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

	var query = "SELECT u.username, u.name, u.email, u.phone, u.address FROM Users u WHERE u.email=" + email + " AND u.password="+password;
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
	Function to update user contact.

	# post params:
		required:
			email
			token
			
			IF CONTACT_ID is NOT defined:
				required:
					address_line1
					address_city
					address_state
					address_zip
					address_phone
				optional
					address_line2
			
			IF CONTACT_ID is defined:
				required:
					[none]
				
				optional:
					address_line1
					address_city
					address_state
					address_zip
					address_phone
					address_line2
				
		optional:
			contact_id
				* To be provided if updating and not creating.
	
	# response structures:
		{
			#1 If contact_id is NOT provided: Inserting new address.
				success: true
				message = "Address inserted successfully";
			
			#2 If contact_id is provided: 
				1.	
					message: "Nothing to update";
						* No update parameters provided.
				2. 
					success: true
					message: "Profile updated successfully";
		}
		----------------------
		{
			success: false,
			errormsg: "error messages defined below"
		}
			error messages:
			#1 IF CONTACT_ID is NOT defined:
				* address_line1 undefined :
				* address_city undefined :
				* address_state undefined :
				* address_zip undefined :
				* phone undefined
				* db failure: on writing Contact
				* db failure: on connecting User to Contact
			
			#2 IF CONTACT_ID is defined:
				* db failure: on verifying contact
				* Cannot modify contact, probably belongs to other user
				* db failure: on updating contact
				
*/
/*module.exports.updateProfile = function(connection, post, user_id, callback) {
	var resp = {}
	resp.success = false;
	user = connection.escape(user_id);

	if (post.contact_id == undefined) {
	// To insert:

		errormsg = "";
		if (post.address_line1==undefined) {
			errormsg += "address_line1 undefined :";
		}
		else {
			addr1 = connection.escape(post.address_line1);
		}
		if (post.address_line2==undefined) {
			addr2 = "NULL";
		}
		else {
			addr2 = connection.escape(post.address_line2);
		}
		if (post.address_city==undefined) {
			errormsg += "address_city undefined :";
		}
		else {
			city = connection.escape(post.address_city)
		}
		if (post.address_state==undefined) {
			errormsg += "address_state undefined :";
		}
		else {
			address_state = connection.escape(post.address_state)
		}
		if (post.address_zip==undefined) {
			errormsg += "address_zip undefined :";
		}
		else {
			address_zip = connection.escape(post.address_zip);
		}
		if (post.phone==undefined) {
			errormsg += "phone undefined";
		}
		else {
			phone = connection.escape(post.phone);
		}
		if (errormsg !== "") {
			resp.errormsg = errormsg;
			callback(resp);
		}

		var insertQuery = "INSERT INTO Contact SET address_line1="+addr1+", address_line2="+addr2+", address_city="+city+", address_state="+address_state+", address_zip="+address_zip+", phone="+phone;
		connection.query(insertQuery, function(i_err, i_result) {
		    if (i_err) {
		        resp.success = false;
		       	resp.errormsg = "db failure: on writing Contact";
		       	callback(resp);
		    }
		    else {
		    	c_id = connection.escape(i_result.insertId);
		    	var connectQuery = "INSERT INTO User_Contact SET User_id="+user+", Contact_id="+c_id;
		    	connection.query(connectQuery, function(c_err, c_result){
		    		if (c_err) {
		    			resp.success = false;
		    			resp.message = "db failure: on connecting User to Contact";
		    		}
		    		else {
		    			resp.success = true;
		    			resp.message = "Address inserted successfully";
		    		}
	    			callback(resp);
		    	});
		    }
		});
	}

	else {
	// To update:

		updateSet = "";
		if (post.address_line1 !== undefined) {
			updateSet += "address_line1="+connection.escape(post.address_line1)+", ";
		}
		if (post.address_line2 !== undefined) {
			updateSet += "address_line2="+connection.escape(post.address_line2)+", ";
		}
		if (post.address_state !== undefined) {
			updateSet += "address_state="+connection.escape(post.address_state)+", ";
		}
		if (post.address_city !== undefined) {
			updateSet += "address_city="+connection.escape(post.address_city)+", ";
		}
		if (post.address_zip !== undefined) {
			updateSet += "address_zip="+connection.escape(post.address_zip)+", ";
		}
		if (post.phone !== undefined) {
			updateSet += "phone="+connection.escape(post.phone)+", ";
		}
		if (updateSet == "") {
			resp.message = "Nothing to update";
			callback(resp);
		}
		else {
			updateSet = updateSet.substring(0, updateSet.length - 2);
			c_id = connection.escape(post.contact_id);
			verifyQuery = "SELECT * FROM User_Contact WHERE User_id="+user+" AND Contact_id="+c_id;
			connection.query(verifyQuery, function(v_err, v_rows, v_fields){
				if (v_err) {
					resp.success = false;
					resp.errormsg = "db failure: on verifying contact";
					callback(resp);
				}
				else {
					if (v_rows.length==0) {
						resp.success = false;
						resp.message = "Cannot modify contact, probably belongs to other user";
						callback(resp);
					}
					else {
						updateQuery = "UPDATE Contact SET "+updateSet+" WHERE id="+c_id;
						console.log(updateQuery);
						connection.query(updateQuery, function(u_err, u_result){
							if (u_err) {
								resp.success = false;
								resp.errormsg = "db failure: on updating contact";
							}
							else {
								resp.success = true;
								resp.message = "Profile updated successfully";
							}
							callback(resp);
						});
					}
				}
			});
		}
	}
}

/*
	Function to delete a user contact.

	# post params:
		required:
			email
			token
			contact_id
		optional:
			[none]
	
	# response structures:
		{
			success: true
			message: "Contact deleted"
		}
		----------------------
		{
			success: false,
			errormsg: "error messages defined below"
		}
			error messages:
				* contact_id undefined :
				* Cannot modify contact, probably belongs to other user
				* db entry failed
*/
/*module.exports.deleteContact = function(connection, post, user_id, callback) {
	u_id = connection.escape(user_id);

	var resp = {}
	if (post.contact_id == undefined) {
		resp.success = false;
		resp.errormsg = "contact_id undefined :";
		callback(resp);
	}
	else {
		c_id = connection.escape(post.contact_id);
		verifyQuery = "SELECT * FROM User_Contact WHERE User_id="+u_id+" AND Contact_id="+c_id;
		connection.query(verifyQuery, function(v_err, v_rows, v_fields) {
			if (v_err) {
				resp.success = false;
				resp.errormsg = "db failure: on verifying contact";
				callback(resp);
			}
			else {
				if (v_rows.length==0) {
					resp.success = false;
					resp.message = "Cannot modify contact, probably belongs to other user";
					callback(resp);
				}
				else {
					var delQuery = "DELETE FROM User_Contact WHERE Contact_id="+c_id+" AND User_id="+u_id;
					connection.query(delQuery, function(d1_err, d1_result) {
					    if (d1_err) {
					        resp.success = false;
					       	resp.errormsg = "db entry failed";
					    }
					    else {
					    	resp.success = true;
					    	resp.message = "Contact deleted";
					    }
				    	callback(resp);
					});
				}
			}
		});
	}
}*/