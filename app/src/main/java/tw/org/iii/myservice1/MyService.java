package tw.org.iii.myservice1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private Timer timer;
    private int i;
    private boolean isStart;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            if (isStart) {
                i++;
                Log.v("brad", " i = " + i);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("brad", "onCreate");
        timer = new Timer();
        timer.schedule(new MyTask(), 0, 1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("brad", "onStartCommand");

        isStart = intent.getBooleanExtra("isStart", false);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (timer != null){
            timer.cancel();
            timer.purge();
            timer = null;
        }
        super.onDestroy();
        Log.v("brad", "onDestroy");
    }
}
