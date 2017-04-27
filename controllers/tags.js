module.exports.getTags = function(req, res, connection) {
	var resp = {}
	resp.success = false;

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var selectQuery = "SELECT description FROM tags ";
		connection.query(selectQuery, function(err, rows, fields) {
			console.log("ROWS:");
			console.log(rows);
			if (err) {
				resp.success = false;
				resp.errormsg = "db failure";
				res.end(JSON.stringify(resp));
			}
			else {
				resp.success = true;
				resp.tags = rows;
				res.end(JSON.stringify(resp));
			}
		});
		connection.release();
	}
}

module.exports.addTagToUser = function(req, res, connection) {
	var resp = {}
	resp.success = false;

	if (req.body.email===undefined) {
		errormsg += "Email undefined :";
	}
	if (req.body.tag===undefined) {
		errormsg += "Tag undefined :";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var email = connection.escape(req.body.email);
		var tag = connection.escape(req.body.tag);


		var insertQuery = "INSERT INTO user_tags_join (user_email, tag_description) VALUES (" + email + "," + tag + ")";
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
module.exports.sortbytag = function(req, res, connection) {
	var resp = {}
	resp.success = false;

	if (req.body.tag===undefined) {
		errormsg += "Tag undefined :";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var tag = connection.escape(req.body.tag);


		var query = "Select user_email from user_tags_join where tag_description = " + tag;
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
		connection.release();
	}
}
module.exports.addnewtag = function(req, res, connection) {
	var resp = {}
	resp.success = false;

	if (req.body.tag===undefined) {
		errormsg += "Tag undefined :";
	}

	if (resp.errormsg!==undefined) {
		res.end(JSON.stringify(resp));
	}
	else {
		var tag = connection.escape(req.body.tag);


		var query = "INSERT INTO tags (description) values (" + tag + " )";
		console.log(query);
		connection.query(query, function(err, rows, fields) {
			if (err) {
				resp.success = false;
				resp.errormsg = "db failure";
				res.end(JSON.stringify(resp));
			}
			else {
				resp.success = true;
				res.end(JSON.stringify(resp));
			}
		});
		connection.release();
	}
}