// user.route.js

const express = require("express");
const app = express();
const userRoutes = express.Router();

let User = require("../models/user");

// user register
userRoutes.route("/add").post(function(req, res) {
  let user = new User(req.body);
  console.log(user.body)
  user
    .save()
    .then(user => {
      console.log(user)
      res.status(200).json({ user: "user in added successfully" });
    })
    .catch(err => {
      if (err.message.indexOf("duplicate key error") !== -1)
        res.status(403).send("user already exists");
      else res.status(400).send("unable to save to database");
    });
});
 
module.exports = userRoutes;
