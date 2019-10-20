let connection = null;

class Realtime {
  constructor() {
    this._socket = null;
    this._user = null;
  }
  connect(server) {
    const io = require("socket.io")(server);

    io.on("connection", socket => {
      this._socket = socket;
      this._socket.on("connection", data => {
        console.log(data);
      });

      this._socket.on("disconnect", function() {
        console.log(socket.id, "diconnect");
      });

      console.log(`New socket connection: ${socket.handshake.query.user}`);
      this._user = socket.handshake.query.user;
    });
  }

  getUser() {
    return this._user;
  }
  sendEvent(event, data) {
    this._socket.emit(event, data);
  }

  registerEvent(event, handler) {
    this._socket.on(event, handler);
  }

  static init(server) {
    if (!connection) {
      connection = new Realtime();
      connection.connect(server);
    }
  }

  static getConnection() {
    if (!connection) {
      console.log("no active connection");
    }
    return connection;
  }
}

module.exports = {
  connect: Realtime.init,
  connection: Realtime.getConnection
};
