package in.sanitization.sanitization;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.Fragments.AddressFragment;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.ToastMsg;

public class SubscriptionActivity extends AppCompatActivity{

    Activity ctx=SubscriptionActivity.this;
    Module module;
    LoadingBar loadingBar;

    ToastMsg toastMsg;
    ImageView iv_back;
    TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        initViews();

        if(savedInstanceState==null)
        {
            AddressFragment fm=new AddressFragment();
            Bundle bundle=new Bundle();
            addFragment(fm,bundle);
        }
    }

    public void addFragment(Fragment fm, Bundle args)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fm.setArguments(args);
        fragmentManager.beginTransaction()
                .add( R.id.content_frame,fm)
                .commit();
    }
    private void initViews() {
        module=new Module(ctx);
        toastMsg=new ToastMsg(ctx);
        loadingBar=new LoadingBar(ctx);
        iv_back=findViewById(R.id.iv_back);
        tv_title=findViewById(R.id.tv_title);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    public void setTitle(String title)
    {
        tv_title.setText(title);
    }


}
