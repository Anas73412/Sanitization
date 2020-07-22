package in.sanitization.sanitization;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.Session_management;
import in.sanitization.sanitization.util.ToastMsg;

import static in.sanitization.sanitization.Config.BaseUrl.RESET_PASS_URL;
import static in.sanitization.sanitization.Config.Constants.KEY_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment implements View.OnClickListener {
    EditText et_old_pass,et_new_pass,et_c_pass;
    Button btn_sbmit;
String user_id ;
LoadingBar loadingBar ;
Module module;
Session_management session_management;
    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =inflater.inflate(R.layout.fragment_reset_password, container, false);
        ((HomeActivity) getActivity()).setTitle("Change Password");
       initViews(view);
       return view ;
    }
    void initViews(View v)
    {
        et_c_pass= v.findViewById(R.id.et_con_password);
        et_old_pass= v.findViewById(R.id.et_old_password);
        et_new_pass= v.findViewById(R.id.et_new_password);
       btn_sbmit= v.findViewById(R.id.btn_submit);
       btn_sbmit.setOnClickListener(this);
       session_management = new Session_management(getActivity());
       loadingBar = new LoadingBar(getActivity());
       module = new Module(getActivity());
       user_id = session_management.getUserDetails().get(KEY_ID);


    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_submit)
        {
            String oldpass = et_old_pass.getText().toString().trim();
            String newpass = et_new_pass.getText().toString().trim();
            String cpass= et_c_pass.getText().toString().trim();
            if (oldpass.isEmpty())
            {
                et_old_pass.setError("Enter old Password");
                et_old_pass.requestFocus();
            }
            else if(oldpass.length()<6)
            {
                et_old_pass.setError("Minimum 6 characters allow");
                et_old_pass.requestFocus();
            }
            else if (newpass.isEmpty())
            {
                et_new_pass.setError("Enter new Password");
                et_new_pass.requestFocus();
            }
            else if(newpass.length()<6)
            {
                et_new_pass.setError("Minimum 6 characters allow");
                et_new_pass.requestFocus();
            }
            else  if (cpass.isEmpty())
            {
                et_c_pass.setError("Enter confirm Password");
                et_c_pass.requestFocus();
            }
            else if(cpass.length()<6)
        {
            et_c_pass.setError("Minimum 6 characters allow");
            et_c_pass.requestFocus();
        }
            else
            {
                if (newpass.equals(cpass))
                {

                    makeResetRequest(user_id,oldpass,newpass);
                }
                else
                {
                    et_c_pass.setError("new password and confirm password should be same");
                    et_c_pass.requestFocus();
                }
            }
        }

    }

    public void makeResetRequest(String user_id,String old_pass,String new_pass)
    {
        loadingBar.show();
        HashMap params = new HashMap();
        params.put("user_id",user_id);
        params.put("old_pass",old_pass);
        params.put("new_pass",new_pass);

        CustomVolleyJsonRequest jsonRequest = new CustomVolleyJsonRequest(Request.Method.POST, RESET_PASS_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                Log.e("reset",response.toString());
                try {
                    boolean res = response.getBoolean("responce");
                    if (res)
                    {
                        new ToastMsg(getActivity()).toastIconSuccess(response.getString("message"));
                    }
                    else
                    {
                        new ToastMsg(getActivity()).toastIconError(response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
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
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }
}
