package com.example.hkmusicplayerapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class

MainActivity extends AppCompatActivity implements View.OnClickListener{

    //view declaration
    TextView tvTime, tvDuration;
    SeekBar seekBarTime, seekBarVolume;
    Button btnPlay;

    MediaPlayer musicPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Hide the action bar
        //getSupportActionBar().hide();

        tvTime = findViewById(R.id.tvTime);
        tvDuration = findViewById(R.id.tvDuration);
        seekBarTime = findViewById(R.id.seekBarTime);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        btnPlay = findViewById(R.id.btnPlay);

        musicPlayer = MediaPlayer.create(this, R.raw.cherry_cherry_lady);
        musicPlayer.setLooping(true);
        musicPlayer.seekTo(0);
        musicPlayer.setVolume(0.5f, 0.5f);

        String duration = millisecondsToString(musicPlayer.getDuration());
        tvDuration.setText(duration);
        btnPlay.setOnClickListener(this);

        seekBarVolume.setProgress(50);
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress/100f;
                musicPlayer.setVolume(volume,volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarTime.setMax(musicPlayer.getDuration());
        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    musicPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(musicPlayer != null){
                    if(musicPlayer.isPlaying()){
                        try{
                            final double current = musicPlayer.getCurrentPosition();
                            //double duration = musicPlayer.getDuration();
                            //final double position = (100.0/duration) * current;
                            final String elapsedTime = millisecondsToString((int)current);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvTime.setText(elapsedTime);
                                    seekBarTime.setProgress((int)current);
                                }
                            });

                            Thread.sleep(1000);
                        } catch (InterruptedException e) {

                        }
                    }
                }
            }
        }).start();
    }
    public String millisecondsToString(int time){
        String elapsedTime = "";
        int minutes = time / 1000 / 60;
        int seconds = time / 1000 % 60;
        elapsedTime = minutes+":";
        if(seconds < 10){
            elapsedTime += "0";
        }
        elapsedTime += seconds;
        return elapsedTime;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnPlay){
            if(musicPlayer.isPlaying()){
                //Is playing
                musicPlayer.pause();
                btnPlay.setBackgroundResource(R.drawable.play);
            }else{
                //On pause
                musicPlayer.start();
                btnPlay.setBackgroundResource(R.drawable.pause);
            }
        }
    }
}