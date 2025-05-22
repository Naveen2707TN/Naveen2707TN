package com.voiceawin.vaplayer;

import static com.voiceawin.vaplayer.Activity.AdvanceActivity.SHARED_PREFERENCES;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.voiceawin.vaplayer.Fragment.HomeFragment;
import com.voiceawin.vaplayer.Fragment.MoreFragment;
import com.voiceawin.vaplayer.Fragment.MusicFragment;
import com.voiceawin.vaplayer.Fragment.VideoFragment;
import com.voiceawin.vaplayer.Modul.FileModul;
import com.voiceawin.vaplayer.Modul.MusicFiles;
import com.voiceawin.vaplayer.Modul.VideoFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LinearLayout home,video,music,more;
    private ImageView home_img,video_img,music_img,more_img;
    private TextView home_txt,video_txt,music_txt,more_txt;

    int Select_Tab = 1;
    String sortOrder;


    public static ArrayList<VideoFile> videoFiles = new ArrayList<>();
    public static ArrayList<MusicFiles> musicFiles = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home = findViewById(R.id.Home_Layout);
        video = findViewById(R.id.Video_Layout);
        more = findViewById(R.id.More_Layout);

        home_img = findViewById(R.id.home_img);
        video_img = findViewById(R.id.video_img);
        more_img = findViewById(R.id.more_img);

        home_txt = findViewById(R.id.home_txt);
        video_txt = findViewById(R.id.video_txt);
        more_txt = findViewById(R.id.more_txt);

        getPermission();
        getBottomNavigationBar();

        Select_Tab = 1;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, VideoFragment.class,null)
                .commit();
    }

    private void getBottomNavigationBar() {

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Select_Tab != 1){
                    video.setBackgroundResource(R.color.white);
                    more.setBackgroundResource(R.color.white);

                    video_img.setImageDrawable(getDrawable(R.drawable.music));
                    more_img.setBackgroundResource(R.drawable.menu);

                    video_txt.setVisibility(View.GONE);
                    more_txt.setVisibility(View.GONE);

                    home.setBackgroundResource(R.drawable.home_background);
                    home_img.setBackgroundResource(R.drawable.c_play);
                    home_txt.setVisibility(View.VISIBLE);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainerView, VideoFragment.class,null)
                            .commit();

                    Select_Tab = 1;
                }
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Select_Tab != 2){
                    home.setBackgroundResource(R.color.white);
                    more.setBackgroundResource(R.color.white);

                    home_img.setBackgroundResource(R.drawable.s_play);
                    more_img.setBackgroundResource(R.drawable.menu);

                    home_txt.setVisibility(View.GONE);
                    more_txt.setVisibility(View.GONE);

                    video.setBackgroundResource(R.drawable.home_background);
                    video_img.setImageDrawable(getDrawable(R.drawable.c_music));
                    video_txt.setVisibility(View.VISIBLE);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainerView, MusicFragment.class,null)
                            .commit();

                    Select_Tab = 2;
                }
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Select_Tab != 4){
                    video.setBackgroundResource(R.color.white);
                    home.setBackgroundResource(R.color.white);

                    video_img.setImageDrawable(getDrawable(R.drawable.music));
                    home_img.setBackgroundResource(R.drawable.s_play);

                    video_txt.setVisibility(View.GONE);
                    home_txt.setVisibility(View.GONE);

                    more.setBackgroundResource(R.drawable.home_background);
                    more_img.setBackgroundResource(R.drawable.c_menu);
                    more_txt.setVisibility(View.VISIBLE);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainerView, MoreFragment.class,null)
                            .commit();

                    Select_Tab = 4;
                }
            }
        });
    }

    private void getPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 27);
        }else if (ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            videoFiles = getVideoFile(this);
            musicFiles = getMusic(this);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (!Environment.isExternalStorageManager()){
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                    startActivityIfNeeded(intent,127);
                }catch (Exception e){
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    startActivityIfNeeded(intent,127);
                }
            }else {
                videoFiles = getVideoFile(this);
                musicFiles = getMusic(this);
            }
        }

    }

    public static void timer(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Timer")
                .setMessage("The timer has been set for 30 minutes")
                .setPositiveButton("Yes",(dialog, which) -> {
                    new CountDownTimer(1800000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }
                        @Override
                        public void onFinish() {
                            System.exit(0);
                        }
                    }.start();
                    Toast.makeText(context, "timer has been set", Toast.LENGTH_SHORT).show();
                }).setNegativeButton("Cancel",(dialog, which) -> {
                    dialog.dismiss();
                }).create();
        builder.show();

    }

    private ArrayList<VideoFile> getVideoFile(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        String str = sharedPreferences.getString("sort","abcd");
        ArrayList<VideoFile> tempVide = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        if (str.equals("sortName")){
            sortOrder = MediaStore.MediaColumns.DISPLAY_NAME+" ASC";
        }else if (str.equals("sortSize")){
            sortOrder = MediaStore.MediaColumns.SIZE+" DESC";
        }else{
            sortOrder = MediaStore.MediaColumns.DATE_ADDED+" DESC";
        }


        String[] projection = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.RESOLUTION,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME};

        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,sortOrder);

        if (cursor != null){
            while (cursor.moveToNext()){
                String id = cursor.getString(0);
                String resolution = cursor.getString(1);
                String path = cursor.getString(2);
                String date = cursor.getString(3);
                String size = cursor.getString(4);
                int duration = cursor.getInt(5);
                String file_name = cursor.getString(6);

                String time;
                int sec = (duration / 1000) % 60;
                int min = (duration / (1000 * 60)) % 60;
                int hrs = (duration / (1000 *60 * 60));

                if (hrs == 0){
                    time = String.valueOf(min)
                            .concat(":".concat(String.format(Locale.UK,"%02d",sec)));
                }else {
                    time = String.valueOf(hrs)
                            .concat(":".concat(String.format(Locale.UK,"%02d",min)
                                    .concat(":".concat(String.format(Locale.UK,"%02d",sec)))));
                }

                Log.e("path",path);
                Log.e("file size",size);
                Log.e("date",date);
                VideoFile videoFile = new VideoFile(id,resolution,path,date,size,time,file_name);
                tempVide.add(videoFile);
            }
            cursor.close();
        }
        return tempVide;
    }
    private ArrayList<MusicFiles> getMusic(Context context){
        ArrayList<MusicFiles> tempMusic = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DISPLAY_NAME};

        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);
        if (cursor!=null){
            while (cursor.moveToNext()){
                String id = cursor.getString(0);
                String path = cursor.getString(1);
                String date = cursor.getString(2);
                int duration = cursor.getInt(3);
                String size = cursor.getString(4);
                String name = cursor.getString(5);

                String time;
                int sec = (duration / 1000) % 60;
                int min = (duration / (1000 * 60)) % 60;
                int hrs = (duration / (1000 * 60 * 60));

                if (hrs == 0){
                    time = String.valueOf(min).concat(":".concat(String.format( Locale.UK,"%02d",sec)));
                }else {
                    time = String.valueOf(hrs).
                            concat(":".concat(String.format(Locale.UK,"%02d",min).
                                    concat(":".concat(String.format(Locale.UK,"%02d",sec)))));
                }

                MusicFiles musicFiles = new MusicFiles(id,path,date,time,size,name);
                tempMusic.add(musicFiles);
            }
            cursor.close();
        }
        return  tempMusic;
    }
}