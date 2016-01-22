/**
 * Asset2SD Phonegap Android Plugin for copying files from Assets to SD Card
 * https://github.com/gkcgautam/Asset2SD
 *
 * Available under MIT License (2008).
 *
 * Copyright (c) Gautam Chaudhary 2014
 * http://www.gautamchaudhary.com
 */
 
package com.tiltandco.copyfromassets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.DownloadListener;

public class CopyFromAssets extends CordovaPlugin {
	private static final String TAG = CopyFromAssets.class.getSimpleName();

	private CallbackContext callbackContext = null;

	
	@Override
	public boolean execute(String action, String fileName, CallbackContext callbackContext) {
		Log.d(TAG, "Plugin Called");
		this.callbackContext = callbackContext;
		

			
			if (action.equals("copyFile")) {



				 try {
					// create the application directory   
					String appName =  this.cordova.getActivity().getApplicationContext().getPackageName();
					Log.i("appName",appName);
					File folder = new File(Environment.getExternalStorageDirectory()+"/Android/data/" + appName);
					boolean success = true;
					if (!folder.exists()) {
						Log.i("folder","does not exits");
						success = folder.mkdir();
					} else {
						Log.i("folder","folder exits");
					}
					if (success) {
						// Do something on success
						Log.i("success","!!! aseet copied");
						callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
					} else {
					// Do something else on failure 
						 Log.i("Error","!!! Error occurred while copying file");
						callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
					}

					this.copyAssets(fileName);
		        } catch (Exception e) {
		          e.printStackTrace();
		        }
			}
			
			
			
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
			return false;
		
	}
	
	
 	private void copyAssets(String filename) {
		AssetManager assetManager = this.cordova.getActivity().getApplicationContext().getAssets();
		String appName =  this.cordova.getActivity().getApplicationContext().getPackageName();
		InputStream in = null;
		OutputStream out = null;
		try {
		  in = assetManager.open(filename);
		  File outFile = new File(Environment.getExternalStorageDirectory()+"/Android/data/" + appName,
		      filename);
		  out = new FileOutputStream(outFile);
		  copyFile(in, out);
		  in.close();
		  in = null;
		  out.flush();
		  out.close();
		  out = null;
		} catch (IOException e) {
		  Log.e("tag", "Failed to copy asset file: " + filename, e);
		}
 	}

  	private void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while ((read = in.read(buffer)) != -1) {
	      out.write(buffer, 0, read);
	    }
  	}
}
