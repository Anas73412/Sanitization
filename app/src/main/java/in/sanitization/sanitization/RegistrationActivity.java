package in.sanitization.sanitization;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;

import static in.sanitization.sanitization.Config.BaseUrl.SIGN_UP;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_name,et_number,et_email,et_address,et_pass,et_con_pass;
    Button btn_reg;
    Module module;
    Activity ctx=RegistrationActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initViews();
    }

    private void initViews() {
        et_name=findViewById(R.id.et_name);
        et_number=findViewById(R.id.et_number);
        et_email=findViewById(R.id.et_email);
        et_address=findViewById(R.id.et_address);
        et_pass=findViewById(R.id.et_pass);
        et_con_pass=findViewById(R.id.et_con_pass);
        btn_reg=findViewById(R.id.btn_reg);
        module=new Module(ctx);
        btn_reg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_reg)
        {
            String name=et_name.getText().toString();
            String number=et_number.getText().toString();
            String email=et_email.getText().toString();
            String address=et_address.getText().toString();
            String pass=et_pass.getText().toString();
            String cpass=et_con_pass.getText().toString();
            
            if(name.isEmpty())
            {
                et_name.setError("Enter Name");
                et_name.requestFocus();
            }
            else if(number.isEmpty())
            {
                et_number.setError("Enter Mobile Number");
                et_number.requestFocus();
            } else if(number.length()!=10)
            {
                et_number.setError("Invalid Mobile Number");
                et_number.requestFocus();
            }else if(email.isEmpty())
            {
                et_email.setError("Enter Email Address");
                et_email.requestFocus();
            }else if(!email.contains("@"))
            {
                et_email.setError("Invalid Email Address");
                et_email.requestFocus();
            }else if(address.isEmpty())
            {
                et_address.setError("Enter Address");
                et_address.requestFocus();
            }else if(pass.isEmpty())
            {
                et_pass.setError("Enter Password");
                et_pass.requestFocus();
            }
            else if(pass.length()<5)
            {
                et_pass.setError("Minimum 6 characters allow");
                et_pass.requestFocus();
            }else if(cpass.isEmpty())
            {
                et_con_pass.setError("Enter Password");
                et_con_pass.requestFocus();
            }
            else if(cpass.length()<5)
            {
                et_con_pass.setError("Minimum 6 characters allow");
                et_con_pass.requestFocus();
            }
            else
            {
               if(pass.equals(cpass))
               {
                   registerUser(name,number,email,address,pass);
               }
               else {
                   module.showToast("Password must be matched");
               }
            }
            
        }
    }

    private void registerUser(String name, String number, String email, String address, String pass) {

        HashMap<String,String> params=new HashMap<>();
        params.put("user_name",name);
        params.put("user_mobile",number);
        params.put("user_email",email);
        params.put("address",address);
        params.put("password",pass);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, SIGN_UP, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        module.showToast(""+response.getString("message"));
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
