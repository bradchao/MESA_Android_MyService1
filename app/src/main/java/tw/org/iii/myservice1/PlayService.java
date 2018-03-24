package tw.org.iii.myservice1;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class PlayService extends Service {
    private MediaPlayer mediaPlayer;
    private String song;
    private boolean isPrepared;

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

        mediaPlayer = new MediaPlayer();
        isPrepared = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        song = intent.getStringExtra("song");
        boolean isPlay = intent.getBooleanExtra("play", false);

        if (!isPrepared){
            try{
                mediaPlayer.setDataSource(song);
                mediaPlayer.prepare();
                isPrepared = true;
            }catch (Exception e){
            }
        }

        if (isPrepared && isPlay){
            mediaPlayer.start();
        }else{
            mediaPlayer.pause();
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        super.onDestroy();
    }

}
