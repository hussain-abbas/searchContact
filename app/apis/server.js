// server.js

const express = require("express"),
  path = require("path"),
  bodyParser = require("body-parser"),
  cors = require("cors"),
  mongoose = require("mongoose"),
  config = require("./DB"),
  realtime = require('./realtime')


const userRoute = require("./routes/user.route");
const notificationRoute = require("./routes/notification.route");

mongoose.Promise = global.Promise;
mongoose.connect(config.DB, { useNewUrlParser: true }).then(
  () => {
    console.log("Database is connected");
  },
  err => {
    console.log("Can not connect to the database" + err);
  }
);


  

const app = express();

app.use(bodyParser.json());
app.use(cors());
app.use("/user", userRoute);
app.use("/contact", notificationRoute);
const port = process.env.PORT || 4000;




//socket.io for realtime notification pushing
const http = require("http").createServer(app);
realtime.connect(http)

http.listen(5000);

const server = app.listen(port, function() {
  console.log("Listening on port " + port);
});
