package in.sanitization.sanitization.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sanitization.sanitization.Adapter.FAQAdapter;
import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.HomeActivity;
import in.sanitization.sanitization.MainActivity;
import in.sanitization.sanitization.Model.FAQModel;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.networkconnectivity.NoInternetConnection;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonArrayRequest;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;


public class ContactFragment extends Fragment {
    TextView textView ,title;
    LoadingBar loadingBar ;
    ArrayList<FAQModel> faq_list;
    FAQAdapter faqAdapter;
    RecyclerView rv_faq;
    Module module;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view =  inflater.inflate(R.layout.fragment_contact, container, false);
      textView = view.findViewById(R.id.text);
        title= view.findViewById(R.id.text_title);
        rv_faq = view.findViewById(R.id.rv_faq);
        faq_list= new ArrayList<>();
        loadingBar = new LoadingBar(getActivity());
        module = new Module(getActivity());
        ((HomeActivity) getActivity()).setTitle("Contact Us");
        if (ConnectivityReceiver.isConnected()) {
            getInfo();
            getfaqs();
        }  else
        {
            Intent intent = new Intent(getActivity(), NoInternetConnection.class);
            startActivity(intent);
        }
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
    private void getfaqs() {
        loadingBar.show();
        faq_list.clear();
        Map<String, String> params = new HashMap<String, String>();

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseUrl.FAQ, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("faqs", response.toString());
                loadingBar.dismiss();

                if (response != null && response.length() > 0) {


                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<FAQModel>>() {
                    }.getType();
                    faq_list = gson.fromJson(response.toString(), listType);
                    rv_faq.setLayoutManager(new LinearLayoutManager(getActivity()));
                    faqAdapter = new FAQAdapter(getActivity(),faq_list);
                    rv_faq.setAdapter(faqAdapter);
                    faqAdapter.notifyDataSetChanged();

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);

                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,"plans");

    }
}
