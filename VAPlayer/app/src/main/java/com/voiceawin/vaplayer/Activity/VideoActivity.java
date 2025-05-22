package com.voiceawin.vaplayer.Activity;

import static com.google.android.exoplayer2.Player.REPEAT_MODE_OFF;
import static com.voiceawin.vaplayer.Adapter.VideoAdapter.readableFileSize;
import static com.voiceawin.vaplayer.MainActivity.timer;
import static com.voiceawin.vaplayer.MainActivity.videoFiles;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.PictureInPictureParams;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.voiceawin.vaplayer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class VideoActivity extends AppCompatActivity {

    PlayerView playerView;
    SimpleExoPlayer exoPlayer;
    int position,device_width;
    private int swidth;
    private Point size2;
    private Display display;
    TextView name,file_name,file_location,file_size,file_duration,resolution,text;
    boolean click = false;
    private View view;
    public ImageView sub_Title, nightMode, mute, pop, pause1, rotate, show, lock, unlock, speed,pause2,arrow,close,audio_track,fit,detail,fasticon;
    ConcatenatingMediaSource concatenatingMediaSource;
    private TextView nightmode,timer,speed_txt,audio,fit_txt,mute_txt;
    public ImageView back,gest;
    DialogProperties dialogProperties;
    FilePickerDialog filePickerDialog;
    Uri uri_Subtitle;
    View view1;
    boolean volume, isCrossCheck, pause, click1;
    ConstraintLayout constraintLayout,constraintLayout1,constraintLayout12;
    PictureInPictureParams.Builder pip;
    PlaybackParameters parameters,parameters1;
    private CardView cardView,card_details;
    float speed2;
    Gestures gestures;
    AudioManager audioManager;
    SeekBar seekBar;
    DefaultTrackSelector trackSelector;
    MappingTrackSelector.MappedTrackInfo mappedTrackInfo;
    boolean isEnable=true,isLeft,isRight,check;

    String[] permission = {"android.permission.READ_MEDIA_AUDIO","android.permission.READ_MEDIA_IMAGES","android.permission.READ_MEDIA_VIDEO"};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_video);
        playerView = findViewById(R.id.player_view);
        sub_Title = findViewById(R.id.subTitle);
        nightMode = findViewById(R.id.nightMode);
        view1 = findViewById(R.id.view2);
        mute = findViewById(R.id.imageView4);
        pop = findViewById(R.id.pop_up);
        back = findViewById(R.id.ios_back);
        pause1 = findViewById(R.id.pause);
        rotate = findViewById(R.id.imageView7);
        cardView = findViewById(R.id.card_item);
        show = findViewById(R.id.imageView13);
        lock = findViewById(R.id.imageView8);
        unlock = findViewById(R.id.unlock_bar);
        constraintLayout = findViewById(R.id.main_layout);
        speed = findViewById(R.id.speed_up);
        pause2 = findViewById(R.id.pause_again);
        constraintLayout1 = findViewById(R.id.cl123);
        constraintLayout12 = findViewById(R.id.cl12);
        text = findViewById(R.id.text_det);
        nightmode= findViewById(R.id.textView20);
        timer= findViewById(R.id.textView22);
        speed_txt=findViewById(R.id.textView23);
        audio= findViewById(R.id.textView26);
        fit_txt=findViewById(R.id.textView24);
        mute_txt = findViewById(R.id.textView27);
        gest = findViewById(R.id.gest);

        detail = findViewById(R.id.detail);
        fasticon = findViewById(R.id.fast_det);
        audio_track = findViewById(R.id.imageView5);
        card_details = findViewById(R.id.card_detail);
        file_name = findViewById(R.id.video_title);
        file_location = findViewById(R.id.video_location);
        file_size = findViewById(R.id.video_size);
        file_duration = findViewById(R.id.video_duration);
        resolution = findViewById(R.id.video_resolution);
        arrow = findViewById(R.id.imageView15);
        close = findViewById(R.id.close);
        fit = findViewById(R.id.imageView11);

        display = getWindowManager().getDefaultDisplay();
        size2 = new Point();
        display.getSize(size2);
        swidth = size2.x;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        device_width = displayMetrics.widthPixels;

        gestures = new Gestures(constraintLayout1);

        trackSelector = new DefaultTrackSelector(this);
        mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();

        seekBar = findViewById(R.id.seekBar);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        seekBar.setMax(maxVol);
        seekBar.setProgress(curVol);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //file_location.setText(videoFiles.get(position).getPath());

        dialogProperties = new DialogProperties();
        filePickerDialog = new FilePickerDialog(VideoActivity.this);
        filePickerDialog.setTitle("Select a subTitle File");
        filePickerDialog.setPositiveBtnName("OK");
        filePickerDialog.setNegativeBtnName("Cancel");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pip = new PictureInPictureParams.Builder();
        }

        position = getIntent().getIntExtra("pos", -1);
        String file = getIntent().getStringExtra("name");
        String v_size = getIntent().getStringExtra("size");
        String locat = getIntent().getStringExtra("path");
        file_location.setText(locat);
        String can = readableFileSize(Long.parseLong(v_size));
        file_size.setText(can);
        String duartion = getIntent().getStringExtra("duration");
        String res = getIntent().getStringExtra("res");

        name = findViewById(R.id.file_name_txt);
        name.setText(file);
        file_name.setText(file);
        file_duration.setText(duartion);
        resolution.setText(res);
        getDetails(this);
        getPlayer();
    }

    private void getDetails(Context context) {

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click1 == false) {
                    click1 = true;
                    cardView.setVisibility(View.VISIBLE);
                    card_details.setVisibility(View.GONE);
                } else {
                    click1 = false;
                    cardView.setVisibility(View.GONE);
                }
            }
        });
        fit_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check == false){
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                    fit.setImageDrawable(getDrawable(R.drawable.baseline_fullscreen_24));
                    fit_txt.setText("Full Screen");
                    check = true;
                }else if (check == true){
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                    fit.setImageDrawable(getDrawable(R.drawable.baseline_fit_screen_24));
                    fit_txt.setText("Fit Screen");
                    check = false;
                }
            }
        });
        fit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check == false){
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                    fit.setImageDrawable(getDrawable(R.drawable.baseline_fullscreen_24));
                    fit_txt.setText("Full Screen");
                    check = true;
                }else if (check == true){
                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                    fit.setImageDrawable(getDrawable(R.drawable.baseline_fit_screen_24));
                    fit_txt.setText("Fit Screen");
                    check = false;
                }            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> audioTrack = new ArrayList<>();
                for (int i =0; i<=exoPlayer.getCurrentTrackGroups().length; i++){
                    try {
                        if (exoPlayer.getCurrentTrackGroups().get(i).getFormat(0).selectionFlags == C.SELECTION_FLAG_DEFAULT){
                            audioTrack.add(new Locale(exoPlayer.getCurrentTrackGroups().get(i).getFormat(0).language.toString()).getDisplayLanguage());
                        }
                    }catch (Exception e){

                    }

                }
                CharSequence[] temp = audioTrack.toArray(new CharSequence[audioTrack.size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select Audio Language");
                builder.setMessage("we are working on that so please wait for that feature");
                builder.setItems(temp,(dialog, which) ->{
                    Toast.makeText(context, audioTrack.get(which) +"", Toast.LENGTH_SHORT).show();
                    trackSelector.setParameters(trackSelector.buildUponParameters().setPreferredAudioLanguage(audioTrack.get(which)));
                } );
                builder.create();
                builder.show();
            }
        });

        audio_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> audioTrack = new ArrayList<>();
                for (int i =0; i<=exoPlayer.getCurrentTrackGroups().length; i++){
                    try {
                        if (exoPlayer.getCurrentTrackGroups().get(i).getFormat(0).selectionFlags == C.SELECTION_FLAG_DEFAULT){
                            audioTrack.add(new Locale(exoPlayer.getCurrentTrackGroups().get(i).getFormat(0).language.toString()).getDisplayLanguage());
                        }
                    }catch (Exception e){

                    }

                }
                CharSequence[] temp = audioTrack.toArray(new CharSequence[audioTrack.size()]);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select Audio Language");
                builder.setMessage("we are working on that so please wait for that feature");
                builder.setItems(temp,(dialog, which) ->{
                    Toast.makeText(context, audioTrack.get(which) +"", Toast.LENGTH_SHORT).show();
                    trackSelector.setParameters(trackSelector.buildUponParameters().setPreferredAudioLanguage(audioTrack.get(which)));
                } );
                builder.create();
                builder.show();
            }
        });

        nightmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click == false) {
                    click = true;
                    view1.setVisibility(View.VISIBLE);
                    nightmode.setText("Night mode ON");
                } else {
                    click = false;
                    view1.setVisibility(View.GONE);
                    nightmode.setText("Night mode OFF");
                }
            }
        });

        nightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click == false) {
                    click = true;
                    view1.setVisibility(View.VISIBLE);
                    nightmode.setText("Night mode ON");
                } else {
                    click = false;
                    view1.setVisibility(View.GONE);
                    nightmode.setText("Night mode OFF");
                }
            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_details.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.GONE);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_details.setVisibility(View.GONE);
            }
        });

        pause1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pause == false) {
                    pause = true;
                    exoPlayer.pause();
                    pause1.setImageDrawable(getDrawable(R.drawable.baseline_play_arrow_24));
                } else {
                    pause = false;
                    exoPlayer.play();
                    pause1.setImageDrawable(getDrawable(com.google.android.exoplayer2.R.drawable.exo_icon_pause));
                }
            }
        });

        pause2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pause == false) {
                    pause = true;
                    exoPlayer.pause();
                    pause1.setImageDrawable(getDrawable(R.drawable.baseline_play_arrow_24));
                } else {
                    pause = false;
                    exoPlayer.play();
                    pause1.setImageDrawable(getDrawable(com.google.android.exoplayer2.R.drawable.exo_icon_pause));
                }
            }
        });

        sub_Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    requestPermissions(permission,21);
                }else {
                    Toast.makeText(context, "we need permission", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(context, "subTitle", Toast.LENGTH_SHORT).show();
                dialogProperties.selection_mode = DialogConfigs.SINGLE_MODE;
                dialogProperties.extensions = new String[]{".srt"};
                dialogProperties.root = new File("/storage/emulator/0");
                filePickerDialog.setProperties(dialogProperties);
                filePickerDialog.show();
                filePickerDialog.setDialogSelectionListener(new DialogSelectionListener() {
                    @Override
                    public void onSelectedFilePaths(String[] files) {
                        for (String path : files) {
                            File file = new File(path);
                            uri_Subtitle = Uri.parse(file.getAbsolutePath().toString());
                        }
                        getPlayer_subTile(uri_Subtitle);
                    }
                });
            }
        });

        mute_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (volume == false) {
                    volume = true;
                    exoPlayer.setVolume(0);
                    mute_txt.setText("Mute");
                    mute.setImageDrawable(getDrawable(R.drawable.twotone_volume_off_24));
                } else {
                    volume = false;
                    exoPlayer.setVolume(200);
                    mute_txt.setText("Un Mute");
                    mute.setImageDrawable(getDrawable(R.drawable.baseline_volume_up_24));
                }
            }
        });

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (volume == false) {
                    volume = true;
                    exoPlayer.setVolume(0);
                    mute_txt.setText("Mute");
                    mute.setImageDrawable(getDrawable(R.drawable.twotone_volume_off_24));
                } else {
                    volume = false;
                    exoPlayer.setVolume(100);
                    mute_txt.setText("Un Mute");
                    mute.setImageDrawable(getDrawable(R.drawable.baseline_volume_up_24));
                }
            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Rational rational = new Rational(16, 9);
                    pip.setAspectRatio(rational);
                    enterPictureInPictureMode(pip.build());
                } else {
                    Log.wtf("not oreo", "yes");
                }
            }
        });

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer(context);
            }
        });

        pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer(context);
            }
        });

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    arrow.setVisibility(View.VISIBLE);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    arrow.setVisibility(View.INVISIBLE);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });

        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout1.setVisibility(View.GONE);
                unlock.setVisibility(View.VISIBLE);
            }
        });

        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout1.setVisibility(View.VISIBLE);
                unlock.setVisibility(View.GONE);
            }
        });

        speed_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select Playback Speed")
                        .setPositiveButton("OK", null);
                String[] items = {"0.25x", "0.5x", "1.0x Normal speed", "1.5x", "2x"};
                int checkItem = -1;
                builder.setSingleChoiceItems(items, checkItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                speed2 = 0.25f;
                                parameters = new PlaybackParameters(speed2);
                                exoPlayer.setPlaybackParameters(parameters);
                                break;
                            case 1:
                                speed2 = 0.5f;
                                parameters = new PlaybackParameters(speed2);
                                exoPlayer.setPlaybackParameters(parameters);
                                break;
                            case 2:
                                speed2 = 1.0f;
                                parameters = new PlaybackParameters(speed2);
                                exoPlayer.setPlaybackParameters(parameters);
                                break;
                            case 3:
                                speed2 = 1.5f;
                                parameters = new PlaybackParameters(speed2);
                                exoPlayer.setPlaybackParameters(parameters);
                                break;
                            case 4:
                                speed2 = 2.0f;
                                parameters = new PlaybackParameters(speed2);
                                exoPlayer.setPlaybackParameters(parameters);
                                break;
                            default:
                                break;
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select Playback Speed")
                        .setPositiveButton("OK", null);
                String[] items = {"0.25x", "0.5x", "1.0x Normal speed", "1.5x", "2x"};
                int checkItem = -1;
                builder.setSingleChoiceItems(items, checkItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                speed2 = 0.25f;
                                parameters = new PlaybackParameters(speed2);
                                exoPlayer.setPlaybackParameters(parameters);
                                break;
                            case 1:
                                speed2 = 0.5f;
                                parameters = new PlaybackParameters(speed2);
                                exoPlayer.setPlaybackParameters(parameters);
                                break;
                            case 2:
                                speed2 = 1.0f;
                                parameters = new PlaybackParameters(speed2);
                                exoPlayer.setPlaybackParameters(parameters);
                                break;
                            case 3:
                                speed2 = 1.5f;
                                parameters = new PlaybackParameters(speed2);
                                exoPlayer.setPlaybackParameters(parameters);
                                break;
                            case 4:
                                speed2 = 2.0f;
                                parameters = new PlaybackParameters(speed2);
                                exoPlayer.setPlaybackParameters(parameters);
                                break;
                            default:
                                break;
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void getPlayer_subTile(Uri uri_sub) {
        long old_position = exoPlayer.getCurrentPosition();
        exoPlayer.stop(true);
        trackSelector = new DefaultTrackSelector(this);

        String locat = getIntent().getStringExtra("path");
        Uri uri = Uri.parse(locat);

        exoPlayer = new SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector).build();
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "app"));
        concatenatingMediaSource = new ConcatenatingMediaSource();

        for (int i = 0; i <= videoFiles.size(); i++) {
            new File(String.valueOf(videoFiles.size()));
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(String.valueOf(uri)));
            Format text = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP, Format.NO_VALUE, "app");
            MediaSource sub = new SingleSampleMediaSource.Factory(dataSourceFactory).setTreatLoadErrorsAsEndOfStream(true)
                    .createMediaSource(Uri.parse(String.valueOf(uri_sub)), text, C.TIME_UNSET);
            MergingMediaSource mergingMediaSource = new MergingMediaSource(mediaSource, sub);
            concatenatingMediaSource.addMediaSource(mergingMediaSource);
        }
        playerView.setPlayer(exoPlayer);
        playerView.setKeepScreenOn(true);
        exoPlayer.setRepeatMode(REPEAT_MODE_OFF);
        exoPlayer.setPlaybackParameters(parameters);
        exoPlayer.prepare(concatenatingMediaSource);
        exoPlayer.seekTo(position, old_position);
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                Player.Listener.super.onPlaybackStateChanged(state);
                if (state == exoPlayer.STATE_ENDED){
                    exoPlayer.pause();
                }
            }
        });
        playerError();
    }

    private void getPlayer() {
        String locat = getIntent().getStringExtra("path");
        Uri uri = Uri.parse(locat);
        trackSelector = new DefaultTrackSelector(this);

        exoPlayer = new SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector).build();
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "app"));
        concatenatingMediaSource = new ConcatenatingMediaSource();

        for (int i = 0; i <= videoFiles.size(); i++) {
            new File(String.valueOf(videoFiles.size()));
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(String.valueOf(uri)));
            concatenatingMediaSource.addMediaSource(mediaSource);
        }
        playerView.setPlayer(exoPlayer);
        playerView.setKeepScreenOn(true);
        exoPlayer.setRepeatMode(REPEAT_MODE_OFF);
        exoPlayer.setPlaybackParameters(parameters);
        exoPlayer.prepare(concatenatingMediaSource);
        exoPlayer.seekTo(position, C.TIME_UNSET);
        exoPlayer.setRepeatMode(REPEAT_MODE_OFF);
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                Player.Listener.super.onPlaybackStateChanged(state);
                if (state == exoPlayer.STATE_ENDED){
                    exoPlayer.release();
                }
            }
        });
        playerError();
        playerStop();
    }

    private void playerStop() {

    }

    private void playerError() {
        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
            }
        });
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.getPlaybackState();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exoPlayer.setPlayWhenReady(false);
        exoPlayer.getPlaybackState();
        exoPlayer.getPlaybackState();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPause() {
        super.onPause();
        exoPlayer.setPlayWhenReady(false);
        exoPlayer.getPlaybackState();
        if (isInPictureInPictureMode()) {
            exoPlayer.setPlayWhenReady(true);
        } else {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.getPlaybackState();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.getPlaybackState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.getPlaybackState();
    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        isCrossCheck = isInPictureInPictureMode;
        if (isInPictureInPictureMode) {
            playerView.hideController();
        } else {
            playerView.showController();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isCrossCheck) {
            exoPlayer.release();
            finish();
        }
    }
    public class Gestures implements View.OnTouchListener{
        GestureDetector gestureDetector;
        Gestures(View view){
            GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener(){
                @Override
                public void onLongPress(@NonNull MotionEvent e) {
                    if (e.getAction() == MotionEvent.ACTION_DOWN){
                        float speed = 2.0f;
                        parameters1 = new PlaybackParameters(speed);
                        exoPlayer.setPlaybackParameters(parameters1);
                        fasticon.setVisibility(View.VISIBLE);
                        text.setVisibility(View.VISIBLE);
                        detail.setVisibility(View.VISIBLE);
                    } else if (e.getAction() == MotionEvent.ACTION_UP)
                    {
                        float speed = 1.0f;
                        parameters1 = new PlaybackParameters(speed);
                        exoPlayer.setPlaybackParameters(parameters1);
                        fasticon.setVisibility(View.GONE);
                        text.setVisibility(View.GONE);
                        detail.setVisibility(View.GONE);
                    }
                    super.onLongPress(e);
                }

                @Override
                public boolean onDown(@NonNull MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
                    if (isEnable){
                        HideController();
                        isEnable = false;
                    }else {
                        ShowController();
                        isEnable = true;
                    }
                    cardView.setVisibility(View.GONE);
                    return super.onSingleTapConfirmed(e);
                }
                @Override
                public boolean onDoubleTap(@NonNull MotionEvent e) {
                    if (e.getX() > (swidth / 2)){
                        isLeft = false;
                        isRight = true;
                        exoPlayer.seekTo(exoPlayer.getCurrentPosition()+10000);
                    }else if (e.getX() < (swidth / 2)){
                        isLeft = true;
                        isRight = false;
                        exoPlayer.seekTo(exoPlayer.getCurrentPosition()-10000);
                    }
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            };
            gestureDetector = new GestureDetector(listener);
            view.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP)
            {
                float speed = 1.0f;
                parameters1 = new PlaybackParameters(speed);
                exoPlayer.setPlaybackParameters(parameters1);
                fasticon.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
                detail.setVisibility(View.GONE);
            }
            return gestureDetector.onTouchEvent(event);
        }
    }

    private void HideController() {
        constraintLayout.setVisibility(View.GONE);
        final Window window = this.getWindow();
        if (window == null){
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final View de = window.getDecorView();
        if (de!= null){
            int ui = de.getSystemUiVisibility();
            if(Build.VERSION.SDK_INT >= 14){
                ui |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if(Build.VERSION.SDK_INT >= 16){
                ui |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if(Build.VERSION.SDK_INT >= 19){
                ui |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            de.setSystemUiVisibility(ui);
        }
    }

    private void ShowController() {
        constraintLayout.setVisibility(View.VISIBLE);
        final Window window = this.getWindow();
        if (window == null){
            return;
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        final View de = window.getDecorView();
        if (de!= null){
            int ui = de.getSystemUiVisibility();
            if(Build.VERSION.SDK_INT >= 14){
                ui |= ~View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            if(Build.VERSION.SDK_INT >= 16){
                ui |= ~View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            if(Build.VERSION.SDK_INT >= 19){
                ui |= ~View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
            de.setSystemUiVisibility(ui);
        }
    }

}