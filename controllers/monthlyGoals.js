
module.exports.createmonthlygoal = function(req, res, connection) {
	var resp = {}
	resp.success = false;

	if (req.body.email===undefined) {
		errormsg += "Email undefined :";
	}

	if (req.body.currentgoal===undefined) {
		errormsg += "current goal undefined :";
	}

	if (req.body.completegoal===undefined) {
		errormsg += "complete goal undefined :";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var email = connection.escape(req.body.email);
		var currentgoal = connection.escape(req.body.currentgoal);
		var completegoal = connection.escape(req.body.completegoal);

		var insertQuery = "INSERT INTO user_goals (user_email,current,finished) VALUES ( " + email + " ," + currentgoal + " ," + completegoal + " )";
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

module.exports.getmonthlygoal = function(req, res, connection) {
	var resp = {}
	resp.success = false;

	if (req.body.email===undefined) {
		errormsg += "Email undefined :";
	}

	else {
		var email = connection.escape(req.body.email);

		var getQuery = "SELECT current, finished FROM user_goals WHERE user_email = " + email;
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

module.exports.editmonthlygoal = function(req, res, connection) {
	var resp = {}
	resp.success = false;

	if (req.body.email===undefined) {
		errormsg += "Email undefined :";
	}

	if (req.body.currentgoal===undefined) {
		errormsg += "current goal undefined :";
	}

	if (req.body.completegoal===undefined) {
		errormsg += "complete goal undefined :";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var email = connection.escape(req.body.email);
		var currentgoal = connection.escape(req.body.currentgoal);
		var completegoal = connection.escape(req.body.completegoal);

		var updateQuery = "UPDATE user_goals SET current =" + currentgoal + " , finished = " + completegoal + " WHERE user_email=" + email;
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
