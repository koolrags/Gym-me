
module.exports.createsharedschedule = function(req, res, connection) {
	var resp = {}
	resp.success = false;

	if (req.body.email===undefined) {
		errormsg += "Email undefined :";
	}

	if (req.body.schedule===undefined) {
		errormsg += "Schedule undefined :";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var email = connection.escape(req.body.email);
		var schedule = connection.escape(req.body.schedule);

		var insertQuery = "INSERT INTO shared_schedule (user_email,schedule) VALUES ( " + email + " ," + schedule + " )";
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

module.exports.getsharedschedule = function(req, res, connection) {
	var resp = {}
	resp.success = false;

	if (req.body.email===undefined) {
		errormsg += "Email undefined :";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var email = connection.escape(req.body.email);

		var getQuery = "SELECT schedule FROM shared_schedule WHERE user_email = " + email;
		console.log(getQuery);
		connection.query(getQuery, function(err, rows, fields) {
			if (err) {
				resp.success = false;
				resp.errormsg = "db failure";
				res.end(JSON.stringify(resp));
			}
			else {
				resp.success = true;
				resp.schedule = rows;
			}
			res.end(JSON.stringify(resp));
		});
	}
}
