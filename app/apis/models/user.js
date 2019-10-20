// User.js

const mongoose = require('mongoose');
const Schema = mongoose.Schema;

// Define collection and schema for Business
let User = new Schema({
  name: {
    type: String
  },
  number: {
    type: String,
    unique: true 
    
  },
}, {
  collection: 'users'
});

module.exports = mongoose.model('User', User);