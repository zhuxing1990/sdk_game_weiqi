package com.vunke.sdk_game_weiqi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.vunke.sdk_game_weiqi.util.Utils;

/**
 * Created by zhuxi on 2019/8/12.
 */

public class HomeReceiver  extends BroadcastReceiver{
    private static final String TAG = "HomeReceiver";
    private String HOME_KEY ="homekey";
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent!=null&&!TextUtils.isEmpty(intent.getAction())){
            String action = intent.getAction();
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)){
                Log.i(TAG, "onReceive: getAction:"+ Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                 if (intent.hasExtra("reason")){
                    String reason = intent.getStringExtra("reason");
                    if (HOME_KEY.equals(reason)){
                        Utils.SendBroadcastHomeKey(context);
                    }
                 }
            }
        }
    }
}
