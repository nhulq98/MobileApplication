package com.demo.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

// Class AlarmReceiver: Khi đồng hồ chạy tới thời gian cài đặt nó sẽ chạy vào đây để xử lý
// BroadcastReceiver : Dùng để nhận các intent từ hệ thống or trao đổi dữ liệu giữa các application vs nhau
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d("Toi trong Receiver", "xin chao");
        // Khi thèn alarmReceiver nhận đc cái j đó thì nó sẽ gửi yêu cầu qua thèn music
        Intent myIntent = new Intent(context, Music.class);
        context.startService(myIntent);
    }
}
