package tw.org.iii.myservice1;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private File sdroot;
    private String songFile;
    private DiscreteSeekBar seekBar;
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    0);
        }else{
            init();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
        }else{
            finish();
        }
    }

    private void init(){
//        sdroot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
//        Log.v("brad", sdroot.getAbsolutePath());
//
//        File[] musics = sdroot.listFiles();
//        for (File music : musics){
//            Log.v("brad", music.getAbsolutePath());
//        }



        String[] fileds = {
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM
                };

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri,
                null,null,null,null);
        while (cursor.moveToNext()){
            String data =
                    cursor.getString(
                            cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

            String title =
                    cursor.getString(
                            cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));

            String artist =
                    cursor.getString(
                            cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

            Log.v("brad", title +":"+artist+":"+data);

            songFile = data;

        }

        seekBar = findViewById(R.id.seekBar);
        seekBar.setMin(0);
        seekBar.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                return value / 1000;
            }
        });
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value,
                                          boolean fromUser) {
                if (fromUser){
                    Log.v("brad", "value:" + value);

                    Intent it = new Intent(MainActivity.this, PlayService.class);
                    it.putExtra("seekTo", value);
                    startService(it);


                }

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                //Log.v("brad", "onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                //Log.v("brad", "onStopTrackingTouch");
            }
        });




        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("brad");
        registerReceiver(myReceiver,filter);


    }

    @Override
    public void finish() {
        unregisterReceiver(myReceiver);
        super.finish();
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int len = intent.getIntExtra("len", -1);
            int now = intent.getIntExtra("now", -1);
            if (len >= 0){
                seekBar.setMax(len);
                Log.v("brad", "receive:" + len);
            }
            if (now >= 0){
                seekBar.setProgress(now);
            }

        }
    }



    public void test1(View view) {
        Intent it = new Intent(this, PlayService.class);
        it.putExtra("song", songFile);
        it.putExtra("play", true);
        startService(it);
    }
    public void test2(View view) {
        Intent it = new Intent(this, PlayService.class);
        it.putExtra("play", false);
        startService(it);
    }
    public void test3(View view) {
        Intent it = new Intent(this, PlayService.class);
        stopService(it);
    }
}
