package in.sanitization.sanitization;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.Session_management;

import static in.sanitization.sanitization.Config.BaseUrl.LOGIN;
import static in.sanitization.sanitization.Config.BaseUrl.SIGN_UP;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_mobile,et_pass;
    Button btn_login;
    TextView tv_create;
    Module module;
    Session_management session_management;
    Activity ctx=LoginActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initViews();
    }

    private void initViews() {
        et_mobile=findViewById(R.id.et_mobile);
        et_pass=findViewById(R.id.et_pass);
        btn_login=findViewById(R.id.btn_login);
        tv_create=findViewById(R.id.tv_create);
        module=new Module(ctx);
        session_management =new Session_management(ctx);
        btn_login.setOnClickListener(this);
        tv_create.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() ==  R.id.btn_login)
        {
            String number=et_mobile.getText().toString();
            String pass=et_pass.getText().toString();
            if(number.isEmpty())
            {
                et_mobile.setError("Enter Mobile number");
                et_mobile.requestFocus();
            }
            else if(number.length()!=10)
            {
                et_mobile.setError("Invalid Mobile number");
                et_mobile.requestFocus();
            }
            else if (pass.isEmpty())
            {
                et_pass.setError("Enter Password");
                et_pass.requestFocus();
            }
            else if(pass.length()<5)
            {
                et_pass.setError("Minimum 6 charactes allow");
                et_pass.requestFocus();
            }
            else
            {
                loginUser(number,pass);
                Intent intent=new Intent(ctx,HomeActivity.class);
                startActivity(intent);
            }

        }
        else if(v.getId() == R.id.tv_create)
        {
            Intent intent=new Intent(ctx,Verfication_activity.class);
            startActivity(intent);
        }
    }

    private void loginUser(String number, String pass) {
        HashMap<String,String> params=new HashMap<>();
        params.put("user_phone",number);
        params.put("password",pass);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, LOGIN, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        Log.e("data_login",""+response.toString());
                        //module.showToast(""+response.getString("message"));
                        JSONObject object=response.getJSONObject("data");
                        session_management.createLoginSession(object.getString("user_id").toString(),object.getString("user_email").toString(),
                                object.getString("user_fullname").toString(),object.getString("user_phone").toString(),
                                object.getString("user_image").toString(),object.getString("wallet").toString(),
                                object.getString("rewards").toString(),object.getString("address").toString());
                        Intent intent=new Intent(ctx,HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        module.showToast(""+response.getString("error"));
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    module.showToast(""+msg);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest);
    }
}
