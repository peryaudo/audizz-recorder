var utils = require('cordova/utils'),
    exec = require('cordova/exec');

function AudizzRecorder() {
  this.id = utils.createUUID();
}

AudizzRecorder.prototype.start = function start() {
  exec(null, null, 'AudizzRecorderPlugin', 'start', [this.id]);
};

AudizzRecorder.prototype.stop = function stop(success) {
  exec(success, null, 'AudizzRecorderPlugin', 'stop', [this.id]);
};

AudizzRecorder.prototype.getLevel = function getLevel(success) {
  exec(success, null, 'AudizzRecorderPlugin', 'getLevel', [this.id]);
};

module.exports = AudizzRecorder;
