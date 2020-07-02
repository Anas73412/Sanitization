package in.sanitization.sanitization;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.networkconnectivity.NoInternetConnection;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;


public class ForgotActivity extends AppCompatActivity implements View.OnClickListener {

    Module module;
    private static String TAG = ForgotActivity.class.getSimpleName();
    ProgressDialog loadingBar ;
    private Button btn_continue;
    private EditText et_con_pass,et_new_pass;
    private TextView tv_email;
    String lan;
    String num="";
    SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title

        setContentView(R.layout.activity_forgot);
        num=getIntent().getStringExtra("mobile");
        et_new_pass=(EditText)findViewById(R.id.et_new_pass);
        et_con_pass=(EditText)findViewById(R.id.et_con_pass);
        loadingBar=new ProgressDialog(ForgotActivity.this);
        loadingBar.setMessage("Loading...");
        loadingBar.setCanceledOnTouchOutside(false);
        module=new Module(ForgotActivity.this);


        btn_continue =  findViewById(R.id.btnContinue);
//
        btn_continue.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnContinue) {
            String n_pass=et_new_pass.getText().toString();
            String c_pass=et_con_pass.getText().toString();
            requestPassword(num,n_pass,c_pass);
            // attemptForgot();
        }
    }

    private void requestPassword(String num, String n_pass, String c_pass) {

        if(n_pass.isEmpty())
        {
            et_new_pass.setError("enter password");
            et_new_pass.requestFocus();
        }
        else if(c_pass.isEmpty())
        {
            et_con_pass.setError("enter confim password");
            et_con_pass.requestFocus();
        }
        else if(n_pass.length()<6)
        {
            et_new_pass.setError("password too short");
            et_new_pass.requestFocus();
        }
        else if(c_pass.length()<6)
        {
            et_con_pass.setError("password too short");
            et_con_pass.requestFocus();
        }
        else
        {
            if (ConnectivityReceiver.isConnected()) {
                if (n_pass.equals(c_pass)) {
                    getForgotRequest(num, n_pass);
                } else {
                    Toast.makeText(ForgotActivity.this, "Password must be matched", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Intent intent = new Intent(ForgotActivity.this, NoInternetConnection.class);
                startActivity(intent);
            }
        }

    }

    private void getForgotRequest(String num, String n_pass) {

        loadingBar.show();
        String json_tag="json_forgot_tag";
        HashMap<String, String> map=new HashMap<>();
        map.put("mobile",num);
        map.put("password",n_pass);


        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.FORGOT_URL, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                try {
                    Log.d("updatepass",response.toString());
                    boolean responce=response.getBoolean("responce");

                    if(responce)
                    {
                       module.showToast(response.getString("data"));

                        Intent intent=new Intent(ForgotActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                       module.showToast("Password could not be updated ");

                        Intent intent=new Intent(ForgotActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    Toast.makeText(ForgotActivity.this,""+ ex.getMessage(), Toast.LENGTH_LONG).show();
                }
                //  Toast.makeText(ForgotActivity.this,""+response.toString(),Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(ForgotActivity.this,""+msg, Toast.LENGTH_LONG).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest,json_tag);
    }



}
