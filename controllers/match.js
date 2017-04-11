
module.exports.getallwaiting = function(req, res, connection) {
	var resp = {}
	resp.success = false;
	if (req.body.email===undefined) {
		resp.errormsg = "email undefined";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var email = connection.escape(req.body.email);

		var selectQuery = "SELECT sender_email AS 'email' FROM user_join u WHERE u.receiver_email="+email + "AND u.status= 0";
		console.log(selectQuery);
		connection.query(selectQuery, function(err, rows, fields) {
			if (err) {
				resp.success = false;
				resp.errormsg = "db failure";
				res.end(JSON.stringify(resp));
			}
			else {
				resp.success = true;
				resp.profile = rows;
			}
			res.end(JSON.stringify(resp));
		});
	}
}

module.exports.acceptmatch = function(req, res, connection) {
	var resp = {}
	resp.success = false;
	if (req.body.sender===undefined) {
		resp.errormsg = "sender email undefined";
	}

	if (req.body.receiver===undefined) {
		resp.errormsg = "receiver email undefined";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var sender = connection.escape(req.body.sender);
		var receiver = connection.escape(req.body.receiver);

		var updateQuery = "UPDATE user_join u SET u.status = 1 WHERE u.sender_email=" + sender + " AND u.receiver_email="+receiver;
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
}

module.exports.declinematch = function(req, res, connection) {
	var resp = {}
	resp.success = false;
	if (req.body.sender===undefined) {
		resp.errormsg = "sender email undefined";
	}

	if (req.body.receiver===undefined) {
		resp.errormsg = "receiver email undefined";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var sender = connection.escape(req.body.sender);
		var receiver = connection.escape(req.body.receiver);

		var updateQuery = "UPDATE user_join u SET u.status = -1 WHERE u.sender_email=" + sender + " AND u.receiver_email="+receiver;
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
}

module.exports.sendmatch = function(req, res, connection) {
	var resp = {}
	resp.success = false;
	if (req.body.sender===undefined) {
		resp.errormsg = "sender email undefined";
	}

	if (req.body.receiver===undefined) {
		resp.errormsg = "receiver email undefined";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var sender = connection.escape(req.body.sender);
		var receiver = connection.escape(req.body.receiver);

		var insertQuery = "INSERT INTO user_join (sender_email,receiver_email,status) VALUES ( " + sender + " ," + receiver + "," + " 0)";
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

module.exports.unmatch = function(req, res, connection) {
	var resp = {}
	resp.success = false;
	if (req.body.sender===undefined) {
		resp.errormsg = "sender email undefined";
	}

	if (req.body.receiver===undefined) {
		resp.errormsg = "receiver email undefined";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var sender = connection.escape(req.body.sender);
		var receiver = connection.escape(req.body.receiver);
		var deleteQuery = "DELETE FROM user_join WHERE ( sender_email=" + sender + " AND receiver_email=" + receiver + ") OR ( sender_email=" + receiver + " AND receiver_email= " + sender + " ) AND status = 1";
		console.log(deleteQuery);
		connection.query(deleteQuery, function(err, rows, fields) {
			if (err) {
				resp.success = false;
				resp.errormsg = "db failure";
				res.end(JSON.stringify(resp));
			}
			else {
				resp.success = true;
			}
			res.end(JSON.stringify(resp));
		});
	}
}

module.exports.allmatches = function(req, res, connection) {
	var resp = {}
	resp.success = false;
	if (req.body.email===undefined) {
		resp.errormsg = "email undefined";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var email = connection.escape(req.body.email);
		var q1 = "SELECT receiver_email AS Email from USER_JOIN WHERE sender_email =" + email + " AND status = 1 union SELECT sender_email AS Email from USER_JOIN WHERE receiver_email =" + email + " AND status = 1";
		console.log(q1);
		connection.query(q1, function(err, rows, fields) {
			if (err) {
				resp.success = false;
				resp.errormsg = "db failure";
				res.end(JSON.stringify(resp));
			}
			else {
				resp.success = true;
				resp.matches = rows;
			}
			res.end(JSON.stringify(resp));
		});
	}
}

module.exports.block = function(req, res, connection) {
	var resp = {}
	resp.success = false;
	if (req.body.blocker===undefined) {
		resp.errormsg = "Blocker email undefined";
	}

	if (req.body.blockee===undefined) {
		resp.errormsg = "Blockee email undefined";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var blocker = connection.escape(req.body.blocker);
		var blockee = connection.escape(req.body.blockee);
		var insertQuery = "INSERT INTO user_block (blocker,blockee) VALUES ( " + blocker + " ," + blockee + " )";
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

module.exports.unblock = function(req, res, connection) {
	var resp = {}
	resp.success = false;
	if (req.body.blocker===undefined) {
		resp.errormsg = "Blocker email undefined";
	}

	if (req.body.blockee===undefined) {
		resp.errormsg = "Blockee email undefined";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var blocker = connection.escape(req.body.blocker);
		var blockee = connection.escape(req.body.blockee);
		var deleteQuery = "DELETE FROM user_block WHERE blocker = " + blocker + " AND blockee = " + blockee;
		console.log(deleteQuery);
		connection.query(deleteQuery, function(err, rows, fields) {
			resp.success = true;
			if (err) {
				resp.success = false;
				resp.errormsg = "db entry failed";
			}
		res.end(JSON.stringify(resp));
		});
	}
}

module.exports.getallblocked = function(req, res, connection) {
	var resp = {}
	resp.success = false;
	if (req.body.blocker===undefined) {
		resp.errormsg = "Blocker email undefined";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var blocker = connection.escape(req.body.blocker);
		var searchQuery = "SELECT blockee FROM user_block WHERE blocker = " + blocker;
		console.log(searchQuery);
		connection.query(searchQuery, function(err, rows, fields) {
		if (err) {
			resp.success = false;
			resp.errormsg = "db failure";
			res.end(JSON.stringify(resp));
		}
		else {
			resp.success = true;
			resp.blocks = rows;
		}
		res.end(JSON.stringify(resp));
		});
	}
}
