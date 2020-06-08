package in.sanitization.sanitization;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.Session_management;

import static in.sanitization.sanitization.Config.BaseUrl.SEND_QUERY;
import static in.sanitization.sanitization.Config.Constants.KEY_ID;
import static in.sanitization.sanitization.Config.Constants.KEY_MOBILE;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_mobile,et_msg;
    Button btn_submit;
    Session_management session_management;
    Module module;
    Activity ctx=HelpActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initViews();
    }

    private void initViews() {
        et_mobile=findViewById(R.id.et_mobile);
        et_msg=findViewById(R.id.et_msg);
        btn_submit=findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        session_management=new Session_management(ctx);
        module =new Module(ctx);
        et_mobile.setText(session_management.getUserDetails().get(KEY_MOBILE));
        et_mobile.setEnabled(false);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_submit)
        {
            String message=et_msg.getText().toString();
            if(message.isEmpty())
            {
                et_msg.setError("Enter Message");
                et_msg.requestFocus();
            }
            else if(message.length()<20)
            {
                et_msg.setError("Minimum 20  characters allow");
                et_msg.requestFocus();
            }
            else
            {
                String user_id=session_management.getUserDetails().get(KEY_ID).toString();
                sendQuery(user_id,message);
            }
        }
    }

    private void sendQuery(String user_id, String message) {
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);
        params.put("message",message);
        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, SEND_QUERY, params, new Response.Listener<JSONObject>() {
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
