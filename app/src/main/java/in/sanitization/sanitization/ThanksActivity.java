package in.sanitization.sanitization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ThanksActivity extends AppCompatActivity {

    TextView tv_msg;
    Button btn_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        tv_msg=findViewById(R.id.tv_msg);
        btn_home=findViewById(R.id.btn_home);

        String msg=getIntent().getStringExtra("msg");
        tv_msg.setText(Html.fromHtml(msg));
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             go_home();
            }
        });

    }

    @Override
    public void onBackPressed() {
        go_home();
    }

    public void go_home()
    {
        Intent intent=new Intent(ThanksActivity.this,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
