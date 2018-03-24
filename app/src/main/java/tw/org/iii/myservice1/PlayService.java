package tw.org.iii.myservice1;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PlayService extends Service {
    private MediaPlayer mediaPlayer;
    private String song;
    private boolean isPrepared;
    private Timer timer;

    public PlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        timer = new Timer();
        timer.schedule(new MyTask(), 0, 500);
        mediaPlayer = new MediaPlayer();
        isPrepared = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        song = intent.getStringExtra("song");
        boolean isPlay = intent.getBooleanExtra("play", false);
        int seekTo = intent.getIntExtra("seekTo", -1);

        if (!isPrepared){
            try{
                mediaPlayer.setDataSource(song);
                mediaPlayer.prepare();
                isPrepared = true;


                int len = mediaPlayer.getDuration();
                //Log.v("brad", "len = " + len);

                Intent it = new Intent("brad");
                it.putExtra("len", len);
                sendBroadcast(it);


            }catch (Exception e){
            }
        }

        if (isPrepared && seekTo >= 0){
            mediaPlayer.seekTo(seekTo);
        }else {
            if (isPrepared && isPlay) {
                mediaPlayer.start();
            } else {
                mediaPlayer.pause();
            }
        }


        return super.onStartCommand(intent, flags, startId);
    }

    private class MyTask extends TimerTask {
        @Override
        public void run() {
            if (mediaPlayer!=null && mediaPlayer.isPlaying()) {
                int now = mediaPlayer.getCurrentPosition();
                //Log.v("brad", "now = " + now);

                Intent it = new Intent("brad");
                it.putExtra("now", now);
                sendBroadcast(it);

            }
        }
    }


    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (timer != null){
            timer.cancel();
            timer.purge();
            timer = null;
        }

        super.onDestroy();
    }

}
