package in.sanitization.sanitization.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.HomeActivity;
import in.sanitization.sanitization.MainActivity;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;


public class ContactFragment extends Fragment {
    TextView textView ,title;
    LoadingBar loadingBar ;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view =  inflater.inflate(R.layout.fragment_contact, container, false);
      textView = view.findViewById(R.id.text);
        title= view.findViewById(R.id.text_title);
        loadingBar = new LoadingBar(getActivity());
        ((HomeActivity) getActivity()).setTitle("Contact Us");
        getInfo();
      return view;
    }
    private void getInfo()
    {
        loadingBar.show();
        HashMap<String,String> params = new HashMap<>();
        CustomVolleyJsonRequest jsonRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.CONTACT,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean stat = response.getBoolean("responce");
                    if (stat)
                    {
                        loadingBar.dismiss();
                        JSONArray array = response.getJSONArray("data");
                        JSONObject object = array.getJSONObject(0);
                        textView.setText(Html.fromHtml(object.getString("pg_descri")));
                        title.setText(Html.fromHtml(object.getString("pg_title")));

                    }
                } catch (JSONException e) {
                    loadingBar.dismiss();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                new Module(getActivity()).showToast(error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }

}
