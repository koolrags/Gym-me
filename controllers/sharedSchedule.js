
module.exports.createsharedschedule = function(req, res, connection) {
	var resp = {}
	resp.success = false;

	if (req.body.user1===undefined) {
		errormsg += "User 1 undefined :";
	}

	if (req.body.user2===undefined) {
		errormsg += "User 2 undefined :";
	}

	if (req.body.schedule===undefined) {
		errormsg += "Schedule undefined :";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var user1 = connection.escape(req.body.user1);
		var user2 = connection.escape(req.body.user2);
		var schedule = connection.escape(req.body.schedule);

		var insertQuery = "INSERT INTO shared_schedule (user_1,user_2,schedule) VALUES ( " + user1 + " ," + user2 +" ," + schedule + " )";
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

module.exports.getsharedschedule = function(req, res, connection) {
	var resp = {}
	resp.success = false;

	if (req.body.user1===undefined) {
		errormsg += "User 1 undefined :";
	}

	if (req.body.user2===undefined) {
		errormsg += "User 2 undefined :";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var user1 = connection.escape(req.body.user1);
		var user2 = connection.escape(req.body.user2);
		var getQuery = "SELECT schedule FROM shared_schedule WHERE user_1 = " + user1 + " AND user_2 = " + user2 + " OR user_1 = " + user2 + " AND user_2 = " + user1;
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
		connection.release();
	}
}

module.exports.editsharedschedule = function(req, res, connection) {
	var resp = {}
	resp.success = false;

	if (req.body.user1===undefined) {
		errormsg += "User 1 undefined :";
	}

	if (req.body.user2===undefined) {
		errormsg += "User 2 undefined :";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var user1 = connection.escape(req.body.user1);
		var user2 = connection.escape(req.body.user2);
		var schedule = connection.escape(req.body.schedule);

		var updateQuery = "UPDATE shared_schedule SET schedule =" + schedule + " WHERE user_1 = " + user1 + " AND user_2 = " + user2 + " OR user_1 = " + user2 + " AND user_2 = " + user1;
		console.log(updateQuery);
		connection.query(updateQuery, function(err, rows, fields) {
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
