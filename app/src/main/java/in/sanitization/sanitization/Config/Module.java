package in.sanitization.sanitization.Config;

import android.content.Context;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class Module {

    Context context;

    public Module(Context context) {
        this.context = context;
    }

    public String VolleyErrorMessage(VolleyError error)
    {
        String str_error ="";
        if (error instanceof TimeoutError) {
            str_error="Connection Timeout";
        } else if (error instanceof AuthFailureError) {
            str_error="Session Timeout";
            //TODO
        } else if (error instanceof ServerError) {
            str_error="Server not responding please try again later";
            //TODO
        } else if (error instanceof NetworkError) {
            str_error="Server not responding please try again later";
            //TODO
        } else if (error instanceof ParseError) {
            //TODO
            str_error="An Unknown error occur";
        }else if(error instanceof NoConnectionError){
            str_error="No Internet Connection";
        }

        return str_error;
    }

    public void preventMultipleClick(final View view) {
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 1000);
    }
}
