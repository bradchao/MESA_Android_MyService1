package tw.org.iii.myservice1;

import android.Manifest;
import android.content.Intent;
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

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private File sdroot;
    private String songFile;

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
