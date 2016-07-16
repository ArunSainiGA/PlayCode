package com.cycus.playcodeapp.Utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.File;
import java.util.Date;

/**
 * Created by Arun_Saini on 28-06-2016.
 */
public class DownloadContent {
    public void initiateDownload(Context context, int catId, int gameId, String gameName){
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Log.i("Download_url", APIs.BASE_DOWNLOAD + "?p=1:" + catId + ":0:" + gameId);
        Uri url = Uri.parse(APIs.BASE_DOWNLOAD + "?p=1:" + catId + ":0:" + gameId);
        DownloadManager.Request request = new DownloadManager.Request(url);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(true);
        request.setTitle(gameName + "_" + (new Date()).getTime() + ".apk");

        request.setDestinationInExternalPublicDir("/Download", gameName + "_" + (new Date()).getTime() + ".apk");
//        request.setDestinationInExternalFilesDir(context, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), gameName + "_" + (new Date()).getTime() + ".apk");
        long downloadRef = dm.enqueue(request);
    }
}
