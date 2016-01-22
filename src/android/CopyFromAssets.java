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
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
		Log.d(TAG, "Plugin Called");
		this.callbackContext = callbackContext;
		
			
			if (action.equals("copyFile")) {
				try {
					JSONObject obj = args.getJSONObject(0);
					String copyFileName = obj.getString("copyFileName");
					String saveAsFileName = obj.getString("saveAsFileName");
					Log.i("copyFileName",copyFileName);
					Log.i("saveAsFileName",saveAsFileName);
					this.copyFile(copyFileName, saveAsFileName);
					Log.i("success","!!! aseet copied");
					callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
					return true;
				 } catch (Exception e) {
				 	 e.printStackTrace();
				 	  Log.i("Error","!!! Error occurred while copying file");
				 	  callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
				 	  return false;
				 }
			}
			
			
			
			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
			return false;
		
	}
	public String copyFile(String copyFileName, String saveAsFileName) throws IOException {
		
		File externalPath = Environment.getExternalStorageDirectory();	
		String appName =  this.cordova.getActivity().getApplicationContext().getPackageName();
		File destination_file = new File( externalPath + addLeadingSlash("/Android/data/" + appName+'/'+saveAsFileName) );
		File destination_dir = destination_file.getParentFile();
		String destination_file_path = destination_file.getPath();
		String destination_file_name = destination_file.getName();
		
		if(destination_file_name.length()<=0){
			throw new IOException("Destination file name is missing");
		}
		
		createDir(destination_dir);
		copyAssetFile(copyFileName, saveAsFileName, destination_file_path);
		
		return destination_file_path;
	}


	/**
     * Copies asset file bytes to destination path
     */
	public void copyAssetFile(String copyFileName,  String destinationFilePath) throws IOException{
		
		
		//InputStream in =  getClass().getResourceAsStream("/assets/"+file_name);
		InputStream in = this.cordova.getActivity().getApplicationContext().getAssets().open(copyFileName);
		OutputStream out = new FileOutputStream(destinationFilePath);
		
		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len; while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
		in.close(); out.close();
	}
	/**
     * Create a directory for the provided File object
     */
	public void createDir(File dir) throws IOException {
		if (dir.exists()){
			if(!dir.isDirectory()) {
				throw new IOException("Can't create directory, a file is in the way");
			}
		} else {
			dir.mkdirs();
			if (!dir.isDirectory()) {
				throw new IOException("Unable to create directory");
			}
		}
	}
	

	// Adds a leading slash to path if it doesn't exist
	public String addLeadingSlash(String path){
		if(path.charAt(0)!='/'){
		    path = "/" + path;
		}
		return path;
	}
	
	
 	


}
