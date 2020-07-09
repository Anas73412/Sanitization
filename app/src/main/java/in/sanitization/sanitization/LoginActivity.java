package in.sanitization.sanitization;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import in.sanitization.sanitization.networkconnectivity.NoInternetConnection;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.Session_management;
import in.sanitization.sanitization.util.ToastMsg;

import static in.sanitization.sanitization.Config.BaseUrl.LOGIN;
import static in.sanitization.sanitization.Config.BaseUrl.SIGN_UP;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_mobile,et_pass;
    Button btn_login;
    TextView tv_create ,tv_back ,tv_forgot;
    Module module;
    LoadingBar loadingBar ;
    Session_management session_management;
    Activity ctx=LoginActivity.this;
    ToastMsg toastMsg;
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
        tv_forgot=findViewById(R.id.tv_forgot);
        tv_back=findViewById(R.id.txt_back);
        module=new Module(ctx);
       loadingBar=new LoadingBar(ctx);
       toastMsg=new ToastMsg(ctx);
        session_management =new Session_management(ctx);
        btn_login.setOnClickListener(this);
        tv_create.setOnClickListener(this);
        tv_forgot.setOnClickListener(this);
        tv_back.setOnClickListener(this);
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
                et_pass.setError("Minimum 6 characters allowed");
                et_pass.requestFocus();
            }
            else
            {
                if (ConnectivityReceiver.isConnected()) {
                    loginUser(number, pass);
                }
                else
                {
                    Intent intent = new Intent(LoginActivity.this, NoInternetConnection.class);
                    startActivity(intent);
                }

            }

        }
        else if(v.getId() == R.id.tv_create)
        {
//            Intent intent=new Intent(ctx,RegistrationActivity.class);
//            intent.putExtra("number","8081031624");
//            startActivity(intent);
            Intent intent=new Intent(ctx,Verfication_activity.class);
            intent.putExtra("type","r");
            startActivity(intent);
        }
        else if(v.getId() == R.id.tv_forgot)
        {
            Intent intent=new Intent(ctx,Verfication_activity.class);
            intent.putExtra("type","f");
            startActivity(intent);
        }
        else if(v.getId() == R.id.txt_back)
        {
           finish();
        }
    }

    private void loginUser(String number, String pass) {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_phone",number);
        params.put("password",pass);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, LOGIN, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    loadingBar.dismiss();
                    boolean resp=response.getBoolean("responce");
                    Log.e("login",response.toString());
                    if(resp)
                    {
                        JSONObject object=response.getJSONObject("data");
                        session_management.createLoginSession(object.getString("user_id"),object.getString("user_email"),object.getString("user_fullname"),object.getString("user_phone"),
                                object.getString("state"),object.getString("district"),object.getString("block"),object.getString("pincode"),object.getString("address"),object.getString("district_manager_id"),object.getString("area_manager_id"));
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
                    loadingBar.dismiss();
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    module.showToast(""+msg);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //((MainActivity) getActivity()).finish();
                finishAffinity();


            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();

    }
}
