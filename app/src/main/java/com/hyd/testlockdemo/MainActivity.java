package com.hyd.testlockdemo;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler.sendEmptyMessageDelayed(1, 10000);
        initService();
        initButtonReceiver();
    }

    private void initService() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public ButtonBroadcastReceiver bReceiver;
    public void initButtonReceiver(){
        bReceiver = new ButtonBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BUTTON);
        registerReceiver(bReceiver, intentFilter);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            wakeUpAndUnlock(MainActivity.this);
            handler.sendEmptyMessageDelayed(1, 10000);
            return false;
        }
    });

    public void wakeUpAndUnlock(Context context) {
        System.out.println("wakeUpAndUnlock 1");
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        System.out.println("wakeUpAndUnlock 2");
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        System.out.println("wakeUpAndUnlock 3");

            if(km.inKeyguardRestrictedInputMode()){
                System.out.println("wakeUpAndUnlock 3.1");
                KeyguardManager.KeyguardLock keyguard = km.newKeyguardLock(getLocalClassName());
                System.out.println("wakeUpAndUnlock 3.2");
                keyguard.disableKeyguard();
                System.out.println("wakeUpAndUnlock 3.3");
            }
        //解锁
//        kl.disableKeyguard();
        System.out.println("wakeUpAndUnlock 4");
        //获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        System.out.println("wakeUpAndUnlock 5");
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        System.out.println("wakeUpAndUnlock 6");
        //点亮屏幕
        wl.acquire();
        System.out.println("wakeUpAndUnlock 7");

        //初始化键盘锁，可以锁定或解开键盘锁
        KeyguardManager.KeyguardLock mKeyguardLock = km.newKeyguardLock("");
        //禁用显示键盘锁定
        mKeyguardLock.disableKeyguard();
        //释放
//        wl.release();
        System.out.println("wakeUpAndUnlock 8");
    }

    public void onClick(View view){
        showButtonNotify();
    }

    public NotificationManager mNotificationManager;
    public final static String ACTION_BUTTON = "com.notifications.intent.action.ButtonClick";
    public final static String INTENT_BUTTONID_TAG = "ButtonId";
    public final static int BUTTON_ID_1 = 1;
    public final static int BUTTON_ID_2 = 2;
    public final static int BUTTON_ID_3 = 3;
    public final static int BUTTON_ID_4 = 4;
    /**
     * 带按钮的通知栏
     */
    public void showButtonNotify(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.view_custom_button);
//        mRemoteViews.setImageViewResource(R.id.tv_1, R.mipmap.ic_launcher);
//        mRemoteViews.setTextViewText(R.id.tv_1, "周杰伦");

        //点击的事件处理
        Intent buttonIntent = new Intent(ACTION_BUTTON);
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_ID_1);
        //这里加了广播，所及INTENT的必须用getBroadcast方法
        PendingIntent intent_1 = PendingIntent.getBroadcast(this, 1, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.ll_option1, intent_1);

        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_ID_2);
        PendingIntent intent_2 = PendingIntent.getBroadcast(this, 2, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.ll_option2, intent_2);

        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_ID_3);
        PendingIntent intent_3 = PendingIntent.getBroadcast(this, 3, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.ll_option3, intent_3);

        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_ID_4);
        PendingIntent intent_4 = PendingIntent.getBroadcast(this, 4, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.ll_option4, intent_4);

        mBuilder.setContent(mRemoteViews)
                .setContentIntent(getDefalutIntent(Notification.FLAG_ONGOING_EVENT))
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setTicker("正在播放")
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher);
        Notification notify = mBuilder.build();
        notify.flags = Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(1998, notify);

    }

    public PendingIntent getDefalutIntent(int flags){
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }

    public class ButtonBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if(action.equals(ACTION_BUTTON)){
                int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
                switch (buttonId) {
                    case BUTTON_ID_1:
                        Toast.makeText(getApplicationContext(), "BUTTON_ID_1", Toast.LENGTH_SHORT).show();
                        break;
                    case BUTTON_ID_2:
//                        showButtonNotify();
                        //TODO 更新按钮
                        Toast.makeText(getApplicationContext(), "BUTTON_ID_2", Toast.LENGTH_SHORT).show();
                        break;
                    case BUTTON_ID_3:
                        Toast.makeText(getApplicationContext(), "BUTTON_ID_3", Toast.LENGTH_SHORT).show();
                        break;
                    case BUTTON_ID_4:
                        Toast.makeText(getApplicationContext(), "BUTTON_ID_4", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        mNotificationManager.cancel(1998);
        if(bReceiver != null){
            unregisterReceiver(bReceiver);
        }
        super.onDestroy();
    }
}
