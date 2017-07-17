package com.example.dragonsage.mp3player;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity{

    static MediaPlayer mediaPlayer;
    Uri uri;
    ArrayList<File> mySongs;
    int position;
    Button buPlay, buFF, buFB, buNext, buPrevious;
    SeekBar sb;
    Thread updateSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        buPlay = (Button)findViewById(R.id.buPlay);
        buFF = (Button)findViewById(R.id.buFF);
        buFB = (Button)findViewById(R.id.buFB);
        buNext = (Button)findViewById(R.id.buNext);
        buPrevious = (Button)findViewById(R.id.buPrevious);
        sb  =(SeekBar)findViewById(R.id.seekBar);

        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songlist");
        position = bundle.getInt("pos",0);

        uri = Uri.parse(mySongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this,uri);
        mediaPlayer.start();
        sb.setMax(mediaPlayer.getDuration());

        updateSeekbar = new Thread(){
            @Override
            public void run() {

                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;

                while (currentPosition<totalDuration){
                    try {

                        sleep(500);

                        currentPosition = mediaPlayer.getCurrentPosition();
                        sb.setProgress(currentPosition);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        updateSeekbar.start();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    public void buPlay(View view) {

        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }else {
            mediaPlayer.start();
            buPlay.setText("||");
        }
    }

    public void buFF(View view) {
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+5000);
    }

    public void buFB(View view) {
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-5000);
    }

    public void buPrevious(View view) {
        mediaPlayer.stop();
        mediaPlayer.release();

        position = (position<=0) ? position = mySongs.size()-1 : position-1;

        uri = Uri.parse(mySongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this,uri);
        mediaPlayer.start();
        sb.setMax(mediaPlayer.getDuration());
    }

    public void buNext(View view) {
        mediaPlayer.stop();
        mediaPlayer.release();

        position = (position>=mySongs.size()-1)? position = 0: position+1;
        uri = Uri.parse(mySongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this,uri);
        mediaPlayer.start();
        sb.setMax(mediaPlayer.getDuration());
    }
}
