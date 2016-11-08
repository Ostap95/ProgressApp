package com.ostap_kozak.progress.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ostap_kozak.progress.R;
import com.ostap_kozak.progress.service.StopwatchService;

import java.util.Timer;

/**
 * Created by ostapkozak on 13/09/2016.
 */
public class RunningTask extends AppCompatActivity{

    public final static String TASK_NAME = "task_name";
    private static final String PLAY_PAUSE_BUTTON = "playPauseButton";
    private static final String TAG = "StopWatch";
    private static final int STOPWATCH_REFRESH_INTERVAL_MILLIS = 25;

    boolean doubleBackToExitPressedOnce = false;

    private ImageButton stopButton, playPauseButton;
    private Timer mTimer;
    private TextView stopWatch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_task);

        //mTimer = new Timer();

        stopButton = (ImageButton) findViewById(R.id.stop_button);
        playPauseButton = (ImageButton) findViewById(R.id.play_pause_button);

        Intent intent = getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra(TASK_NAME));

        stopWatch = (TextView) findViewById(R.id.time_textview);
        //startTime();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            int playPauseButtonImage = (int) playPauseButton.getTag();
            outState.putInt(PLAY_PAUSE_BUTTON, playPauseButtonImage);
        } catch (NullPointerException e) {
            playPauseButton.setTag(R.drawable.ic_pause_button);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int imageResource = savedInstanceState.getInt(PLAY_PAUSE_BUTTON, R.drawable.ic_pause_button);
        playPauseButton.setImageResource(imageResource);
        playPauseButton.setTag(imageResource);
    }

    public void play_pauseClick(View view) {
        if (stopWatch.isRunning()) {
            stopWatch.pause();
            playPauseButton.setImageResource(R.drawable.ic_play_button);
            playPauseButton.setTag(R.drawable.ic_play_button);
        } else {
            stopWatch.resume();
            playPauseButton.setImageResource(R.drawable.ic_pause_button);
            playPauseButton.setTag(R.drawable.ic_pause_button);
        }
        stopService(new Intent(RunningTask.this, StopwatchService.class));
        unregisterReceiver(timeReceiver);
    }

    public void stop_buttonClick(View view) {
        launchTimerService();
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click back twice to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    public void launchTimerService() {
        Intent intent = new Intent(this, StopwatchService.class);
        startService(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Register for the particular broadcast based on ACTION string
        IntentFilter filter = new IntentFilter(StopwatchService.ACTION);
        registerReceiver(timeReceiver, filter);
    }

    // Define the callback for what to do when data is received
    private BroadcastReceiver timeReceiver  = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
            if (resultCode == RESULT_OK) {
                String resultValue = intent.getStringExtra("resultValue");
                Toast.makeText(RunningTask.this, resultValue, Toast.LENGTH_SHORT).show();
            }
        }
    };

}
