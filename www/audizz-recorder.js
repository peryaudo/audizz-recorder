function AudizzRecorder(src) {
  this.id = utils.createUUID();
  this.src = src;
}

AudizzRecorder.prototype.start = function start() {
  cordova.exec(null, null, 'AudizzRecorderPlugin', 'start', [this.id, this.src]);
};

AudizzRecorder.prototype.stop = function stop() {
  cordova.exec(null, null, 'AudizzRecorderPlugin', 'stop', [this.id]);
};

AudizzRecorder.prototype.getLevel = function getLevel(success) {
  cordova.exec(success, null, 'AudizzRecorderPlugin', 'getLevel', [this.id]);
};

module.exports = AudizzRecorder;
