// contacts .route.js

const express = require("express");
const app = express();
const contactsRoutes = express.Router();
const connection = require("../realtime");

// Require Contacts model in our routes module
let Contacts = require("../models/contacts");

// Defined store route
contactsRoutes.route("/add").post(function (req, res) {
  let contacts = new Contacts(req.body);
  contacts
    .save()
    .then(contacts => {
      res
        .status(200)
        .json({ contacts: "contactsin added successfully" });

      const con = connection.connection();
      if (con.getUser() === contacts.email)
        con.sendEvent("added", contacts);
    })
    .catch(err => {
      res.status(400).send("unable to save to database");
    });
});

// Defined store route
contactsRoutes.route("/adds").post(function (req, res) {
  const lotOfContacts = req.body;
  console.log(lotOfContacts)
  Promise.all(lotOfContacts.map((contacts) => {
    let contact = new Contacts(contacts);
    return Contacts.update(
      { number: contact.number },
      { $setOnInsert: contact },
      { upsert: true },
      function (err, numAffected) {
        if (err) {

          res.status(400).send("unable to save to database");
          return
        }
        console.log(`Saved`);

      }
    );
  }))
    .then(() => {
      res
        .status(200)
        .json({ contacts: "contacts added successfully" });
    })
});

//  Defined update route
contactsRoutes.route("/:query").get(function (req, res) {
  const searchQuery = req.params.query

  var phoneno = /^\+?([0-9])+$/;
  console.log(searchQuery)
  if (searchQuery.match(phoneno)) {
    Contacts.find({ 
       //number: searchQuery
      number: {
        $regex : "^" + searchQuery,
      } 
     }, function (err, contactss) {
      if (!contactss) res.status(404).send("no document found");
      else {
        res.status(200).send(contactss);
      }
    });
    return;
  }

  let searchArray = []
   
  if (searchQuery) searchArray = searchQuery.split(',');
 
  else res.status(404).send("no document found");

  const newArray = searchArray.map((search) => {
    return new RegExp("^" + search)
  }) 
  console.log(searchArray)
  var reg =  "^" + searchArray
  Contacts.find({
    'name': {
      // $in: searchArray,
        $in: newArray,
        
    }
  }, function (err, contactss) {
    if (!contactss) res.status(404).send("no document found");
    else {
      console.log(contactss)
      res.status(200).send(contactss);
    }
  });

});



// Defined get data(index or listing) route
contactsRoutes.route("/").get(function (req, res) {
  Contacts.find({}, function (err, contactss) {
    if (err) {
      console.log(err);
    } else {
      res.json(contactss);
    }
  });
});

 

module.exports = contactsRoutes;
// module.exports = function(io) {
//   this.io = io;
// };
