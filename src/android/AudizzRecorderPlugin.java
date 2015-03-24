package com.audizz.cordova.recorder;

import android.media.MediaRecorder;
import android.net.Uri;
import android.util.Log;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AudizzRecorderPlugin extends CordovaPlugin {
	HashMap<String, MediaRecorder> recorders;

	public AudizzRecorderPlugin() {
		this.recorders = new HashMap<String, MediaRecorder>();
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("start")) {
			this.start(args.getString(0));
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
			return true;
		} else if (action.equals("stop")) {
			byte[] b = this.stop(args.getString(0));
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, b));
			return true;
		} else if (action.equals("getLevel")) {
			float f = this.getLevel(args.getString(0));
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, f));
			return true;
		} else {
			return false;
		}
	}

	public void start(String id) {
		MediaRecorder recorder = new MediaRecorder();

		recorders.put(id, recorder);

		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		recorder.setOutputFile("tmp.m4a");
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
		recorder.setAudioEncodingBitRate(96000);
		recorder.setAudioSamplingRate(44100);

		try {
			recorder.prepare();
			recorder.start();
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("AudizzRecorderPlugin", "MediaRecorder Error: Can't prepare.");
			return;
		}
	}

	public byte[] stop(String id) {
		MediaRecorder recorder = recorders.get(id);
		if (recorder == null) {
			return null;
		}

		recorder.stop();
		recorder.reset();
		recorder.release();

		byte[] content = null;
		try {
			RandomAccessFile file = new RandomAccessFile("tmp.m4a", "r");
			content = new byte[(int) file.length()];
			file.read(content);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("AudizzRecorderPlugin", "RandomAccessFile Error: Can't read temp file.");
		}
		return content;
	}

	public float getLevel(String id) {
		MediaRecorder recorder = recorders.get(id);
		if (recorder == null) {
			return (float)0.0;
		}
		return (float)recorder.getMaxAmplitude();
	}
}
