package com.cycus.playcodeapp.Broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cycus.playcodeapp.EventBeans.ConnectionStatus;
import de.greenrobot.event.EventBus;

/**
 * Created by Arun_Saini on 15-06-2016.
 */
public class BroadcastConnectionCheck extends BroadcastReceiver {
    EventBus bus= EventBus.getDefault();
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectionStatus stat= new ConnectionStatus();
        ConnectivityManager conn= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo= conn.getActiveNetworkInfo();
        if(netInfo!=null){
            if(netInfo.isConnected() && netInfo.isAvailable())
                stat.setConnected(true);
            else if(netInfo.isConnected() && !netInfo.isAvailable())
                stat.setConnected(false);

        }else
            stat.setConnected(false);
        bus.post(stat);
    }
}
