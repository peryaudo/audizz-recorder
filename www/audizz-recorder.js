var utils = require('cordova/utils'),
    exec = require('cordova/exec');

function AudizzRecorder(src) {
  this.id = utils.createUUID();
  this.src = src;
}

AudizzRecorder.prototype.start = function start() {
  exec(null, null, 'AudizzRecorderPlugin', 'start', [this.id, this.src]);
};

AudizzRecorder.prototype.stop = function stop() {
  exec(null, null, 'AudizzRecorderPlugin', 'stop', [this.id]);
};

AudizzRecorder.prototype.getLevel = function getLevel(success) {
  exec(success, null, 'AudizzRecorderPlugin', 'getLevel', [this.id]);
};

module.exports = AudizzRecorder;
