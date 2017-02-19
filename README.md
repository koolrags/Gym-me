# Gym-me

Backend Setup Instructions:

1. Install MYSQL, NODE.JS.
2. While installing MYSQL, set the username as 'root' and password as 'manchesterisred'. This will avoid merge conflicts.
3. Start the MYSQL server after installation. (Mysql.server start)
4. From the terminal, run db.js from the database folder. (node db.js). This will create the database and all required tables.
5. From the terminal, run server.js from the root folder. This will start the server.
6. Using POSTMAN, you can hit the following endpoints: 1. Register 2. Login 3. Profile.
7. Look at the requirements for all three controllers before hitting the endpoint. The requirements are documented in the individual files, along with the expected output.
