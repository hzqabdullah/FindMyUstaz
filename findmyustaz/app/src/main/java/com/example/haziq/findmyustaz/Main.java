package com.example.haziq.findmyustaz;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class Main extends AppCompatActivity {

    public static int SPLASH_TIME_OUT = 4000;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        new Thread(new Runnable() {
            public void run() {
                doWork();
                startApp();
                finish();
            }
        }).start();
    }

    private void doWork()
    {
        for(int progress = 0; progress <= 100; progress += 20)
        {
            try
            {
                Thread.sleep(1000);
                progressBar.setProgress(progress);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void startApp()
    {
        Intent intent = new Intent(Main.this, Login.class);
        startActivity(intent);
    }
}
