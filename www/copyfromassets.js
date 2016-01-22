/**
 * Asset2SD Phonegap Android Plugin for copying files from Assets to SD Card
 * https://github.com/gkcgautam/Asset2SD
 *
 * Available under MIT License (2008).
 *
 * Copyright (c) Gautam Chaudhary 2014
 * http://www.gautamchaudhary.com
 */
(function(cordova){

  var CopyFromAssets = function() {};

  CopyFromAssets.prototype.copyFile = function(params, successCallback, failCallback) {
    return cordova.exec(successCallback, failCallback, 'CopyFromAssets', 'copyFile', [params]);
  };

  window.copyFromAssets = new CopyFromAssets();
  
})(window.PhoneGap || window.Cordova || window.cordova);
