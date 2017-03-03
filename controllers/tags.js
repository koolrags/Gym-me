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
		}
	});
	}
}

module.exports.addTagToUser = function(req, res, connection) {
	var resp = {}
	resp.success = false;

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
	}
}