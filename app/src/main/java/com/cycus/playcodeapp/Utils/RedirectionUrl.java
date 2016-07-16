package com.cycus.playcodeapp.Utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by Arun_Saini on 28-06-2016.
 */
public class RedirectionUrl extends AsyncTask<String, Void, String> {
    boolean redirect = false;
    public AsyncResponse delegate;

    public interface AsyncResponse {
        public void processResponse(String result);
    }

    public RedirectionUrl(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    public String getRedirect(String strUrl) {
        String redirectUrl = strUrl;
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_SEE_OTHER) {
                    redirect = true;
                }
            }

            if (redirect) {
                redirectUrl = conn.getHeaderField("Location");
            }
            Log.i("RedirectionURL: ", redirectUrl);

        } catch (MalformedURLException ex) {

        } catch (IOException e) {
            e.printStackTrace();
        }
        return redirectUrl;
    }

    @Override
    protected String doInBackground(String... params) {
        String redirect = getRedirect(params[0]);
        return redirect;
    }

    @Override
    protected void onPostExecute(String s) {
        delegate.processResponse(s);
    }
}
