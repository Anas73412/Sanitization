package in.sanitization.sanitization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.Map;

import in.sanitization.sanitization.networkconnectivity.NoInternetConnection;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.Session_management;

public class SplashActivity extends AppCompatActivity {

    Session_management session_management;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session_management=new Session_management(SplashActivity.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Thread background = new Thread() {
            public void run() {
                try {

                    sleep(2 * 1000);
                    if (ConnectivityReceiver.isConnected())
                    {
                    if(session_management.isLoggedIn())
                    {
                        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }
                    else
                    {
                        Intent intent = new Intent(SplashActivity.this, IntroductionActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }}
                    else
                    {
                        Intent intent = new Intent(SplashActivity.this, NoInternetConnection.class);
                        startActivity(intent);
                    }



                } catch (Exception e) {
                }
            }
        };

        background.start();

    }
}
