// added.js
module.exports = exports = function addedPlugin(schema, options) {
//   schema.add({ lastMod: Date });

  schema.post("save", function(next) {
    console.log("saved")
  });

};
