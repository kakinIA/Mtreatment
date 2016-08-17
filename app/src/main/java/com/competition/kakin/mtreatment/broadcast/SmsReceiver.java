package com.competition.kakin.mtreatment.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by kakin on 2016/8/5.
 */
public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage msg = null;
        if (bundle != null){
            Object[] smsobj = (Object[]) bundle.get("pdus");
            for (Object obj : smsobj){
                msg = SmsMessage.createFromPdu((byte[]) obj);
                System.out.println("时间："+ msg.getTimestampMillis() + "\n" +
                                    "内容:" + msg.getMessageBody() + "\n" +
                                    "地址：" + msg.getDisplayOriginatingAddress());
            }
        }
    }
}
