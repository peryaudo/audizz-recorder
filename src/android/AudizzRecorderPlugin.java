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

public class AudizzRecorderPlugin extends CordovaPlugin {
	HashMap<String, MediaRecorder> recorders;

	public AudizzRecorderPlugin() {
		this.recorders = new HashMap<String, MediaRecorder>();
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("start")) {
			this.start(args.getString(0), args.getString(1));
		} else if (action.equals("stop")) {
			this.stop(args.getString(0));
		} else if (action.equals("getLevel")) {
			float f = this.getLevel(args.getString(0));
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, f));
			return true;
		} else {
			return false;
		}

		callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, ""));

		return true;
	}

	public void start(String id, String file) {
		String fileUriStr;

		try {
			fileUriStr = webView.getResourceApi().remapUri(Uri.parse(file)).toString();
		} catch (IllegalArgumentException e) {
			fileUriStr = file;
		}

		if (fileUriStr.startsWith("file://")) {
			fileUriStr = Uri.parse(fileUriStr).getPath();
		}

		MediaRecorder recorder = new MediaRecorder();

		recorders.put(id, recorder);

		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		recorder.setOutputFile(fileUriStr);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
		recorder.setAudioEncodingBitRate(96000);
		recorder.setAudioSamplingRate(44100);

		try {
			recorder.prepare();
		} catch (IOException e) {
			Log.d("AudizzRecorderPlugin", "MediaRecorder Error: Can't prepare.");
			return;
		}

		recorder.start();
	}

	public void stop(String id) {
		MediaRecorder recorder = recorders.get(id);
		if (recorder == null) {
			return;
		}

		recorder.stop();
		recorder.release();
	}

	public float getLevel(String id) {
		MediaRecorder recorder = recorders.get(id);
		if (recorder == null) {
			return (float)0.0;
		}
		return (float)recorder.getMaxAmplitude();
	}
}
