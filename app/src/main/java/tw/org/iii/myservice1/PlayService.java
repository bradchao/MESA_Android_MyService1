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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        song = intent.getStringExtra("song");
        try {
            mediaPlayer.setDataSource(song);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            //e.printStackTrace();
            Log.v("brad", e.toString());
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
