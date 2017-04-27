
module.exports.login = function(req, res, connection) {
	var resp = {}
	resp.success = false;
	if (req.body.email===undefined) {
		resp.errormsg = "email undefined";
	}

	if (req.body.password===undefined) {
		resp.errormsg = "password undefined";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var email = connection.escape(req.body.email);
		var password = connection.escape(req.body.password);

		var selectQuery = "SELECT Count(Id) AS 'count' FROM Users u WHERE u.email="+email + "AND u.password=" + password;
		connection.query(selectQuery, function(err, rows, fields) {
		    console.log(selectQuery);
		    if (err) {
		        resp.success = false;
		       	resp.errormsg = "db failure";
		    	res.end(JSON.stringify(resp));
		    }
		    else {
 			    if(rows[0].count != 1) {
 			    	resp.success = false;
 			    	resp.errormsg = "Incorrect email or password";
 			    	res.end(JSON.stringify(resp));
 			    }
 			    else {
 			    		resp.success = true;
 			    		res.end(JSON.stringify(resp));
 			    }
		}
	});
		connection.release();
	}
}