package com.example.amadullahkhan.rescue1122;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView iv=(ImageView) findViewById(R.id.imageView);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );

        ParseInstallation.getCurrentInstallation().saveInBackground();

        new CountDownTimer(5000,1000)
        {
            @Override
            public void onTick(long millisUntilFinished) {
                iv.animate().alpha(1f).setDuration(3000);
            }

            @Override
            public void onFinish() {
                if(ParseUser.getCurrentUser()!=null)
                {
                    ParseUser.logOutInBackground(new LogOutCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                        }
                    });


                }

                    Intent intent = new Intent(getApplicationContext(),MainPage1.class);
                    startActivity(intent);


            }
        }.start();


    }
}
