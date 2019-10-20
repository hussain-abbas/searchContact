// Notification.js

const mongoose = require("mongoose");
mongoose.plugin(require('../plugins/added'));
const Schema = mongoose.Schema;

// Define collection and schema for Notification
let Contacts = new Schema(
  {
    name: {
      type: String
    },
    number: {
      type: String,
      unique: true
    },
    id: {
      type: String
    },
    interest: {
      type: String
    },
    photo: {
      type: String
    },
    notes: {
      type: String
    },
    address: {
      type: String
    }
  },
  {
    collection: "contacts"
  }
);

module.exports = mongoose.model("Contacts", Contacts);
