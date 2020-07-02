package in.sanitization.sanitization.networkconnectivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import in.sanitization.sanitization.R;
import in.sanitization.sanitization.SplashActivity;


public class NoInternetConnection extends AppCompatActivity {
  Button mCheckConnection;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.actvity_no_internet_connection);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
    mCheckConnection = (Button) findViewById(R.id.no_internet_connection);
    mCheckConnection.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (NetworkConnection.connectionChecking(getApplicationContext())) {
          Intent intent = new Intent(NoInternetConnection.this, SplashActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                  Intent.FLAG_ACTIVITY_CLEAR_TASK |
                  Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent);
          finish();
        } else {
          Toast.makeText(getApplicationContext(),"Check your connection.Try again!", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }
}
