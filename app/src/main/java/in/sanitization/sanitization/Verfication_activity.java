package in.sanitization.sanitization;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import in.sanitization.sanitization.Fragments.SendOtpFragment;
import in.sanitization.sanitization.util.LoadingBar;

public class Verfication_activity extends AppCompatActivity {
  TextView back ;

  String type="";
  Activity activity = Verfication_activity.this;
LoadingBar loadingBar ;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_verfication_activity);

    loadingBar=new LoadingBar(activity);
    back = findViewById( R.id.txt_back );
    back.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    } );
    type=getIntent().getStringExtra("type");
   Fragment fm=new SendOtpFragment();
//    Bundle bundle=new Bundle();
//    bundle.putString("type",type);
//    fm.setArguments(bundle);
   FragmentManager fragmentManager =  getSupportFragmentManager();
    fragmentManager.beginTransaction().add(R.id.varify_container, fm)
            .commit();


  }


}
