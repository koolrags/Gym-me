
module.exports.sendmessage = function(req, res, connection) {
	var resp = {}
	resp.success = false;

	if (req.body.sender===undefined) {
		errormsg += "Reporter email undefined :";
	}

	if (req.body.receiver===undefined) {
		errormsg += "Abuser email undefined :";
	}

	if (req.body.message===undefined) {
		errormsg += "Abuser email undefined :";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var sender = connection.escape(req.body.sender);
		var receiver = connection.escape(req.body.receiver);
		var message = connection.escape(req.body.message);

		var insertQuery = "INSERT INTO user_message (sender_email,receiver_email, message) VALUES ( " + sender + " ," + receiver + " ," + message + " )";
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


module.exports.getallmessages = function(req, res, connection) {
	var resp = {}
	resp.success = false;

	if (req.body.sender===undefined) {
		errormsg += "Reporter email undefined :";
	}

	if (req.body.receiver===undefined) {
		errormsg += "Abuser email undefined :";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var sender = connection.escape(req.body.sender);
		var receiver = connection.escape(req.body.receiver);

		var getQuery = "SELECT sender_email, receiver_email, message FROM user_message WHERE sender_email IN (" + sender + "," + receiver + " ) AND receiver_email IN (" + sender + "," + receiver + " )";
		console.log(getQuery);
		connection.query(getQuery, function(err, rows, fields) {
			if (err) {
				resp.success = false;
				resp.errormsg = "db failure";
				res.end(JSON.stringify(resp));
			}
			else {
				resp.success = true;
				resp.messages = rows;
			}
			res.end(JSON.stringify(resp));
		});
	}
}

module.exports.getallchats = function(req, res, connection) {
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
		var q1 = "SELECT receiver_email AS Email from user_message WHERE sender_email =" + email + " union SELECT sender_email AS Email from user_message WHERE receiver_email =" + email;
		console.log(q1);
		connection.query(q1, function(err, rows, fields) {
			if (err) {
				resp.success = false;
				resp.errormsg = "db failure";
				res.end(JSON.stringify(resp));
			}
			else {
				resp.success = true;
				resp.chats = rows;
			}
			res.end(JSON.stringify(resp));
		});
	}
}
