package in.sanitization.sanitization.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.HomeActivity;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.networkconnectivity.NoInternetConnection;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.Session_management;

import static in.sanitization.sanitization.Config.BaseUrl.SEND_QUERY;
import static in.sanitization.sanitization.Config.Constants.KEY_ID;
import static in.sanitization.sanitization.Config.Constants.KEY_MOBILE;

public class HelpActivity extends Fragment implements View.OnClickListener {

    EditText et_mobile,et_msg;
    Button btn_submit;
    Session_management session_management;
    Module module;
    LoadingBar loadingBar ;

    public HelpActivity() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_help, container, false);

        loadingBar = new LoadingBar(getActivity());
        ((HomeActivity) getActivity()).setTitle("Send Enquiry");
    initViews(view);
        return view;
    }



    private void initViews(View v) {
        et_mobile=v.findViewById(R.id.et_mobile);
        et_msg=v.findViewById(R.id.et_msg);
        btn_submit=v.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        session_management=new Session_management(getActivity());
        module =new Module(getActivity());
       loadingBar =new LoadingBar(getActivity());
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
                if (ConnectivityReceiver.isConnected())
                {
                sendQuery(user_id,message);
                }
                else
                {
                    Intent intent = new Intent(getActivity(), NoInternetConnection.class);
                    startActivity(intent);
                }
            }
        }
    }

    private void sendQuery(String user_id, String message) {
        loadingBar.show();
        HashMap<String,String> params=new HashMap<>();
        params.put("user_id",user_id);
        params.put("message",message);
        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, SEND_QUERY, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    loadingBar.dismiss();
                    boolean resp=response.getBoolean("responce");
                    if(resp)
                    {
                        module.showToast(""+response.getString("message"));
                        getActivity().getSupportFragmentManager().popBackStackImmediate();
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
}
