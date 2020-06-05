package in.sanitization.sanitization.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.RelativeLayout;


import in.sanitization.sanitization.R;


/**
 * Developed by Binplus Technologies pvt. ltd.  on 06,April,2020
 */
public class LoadingBar {
    Context context;
    Dialog dialog;
    RelativeLayout rel_loader;


    public LoadingBar(Context context) {
        this.context = context;

        dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.loading_layout);
        dialog.setCanceledOnTouchOutside(false);
        rel_loader =dialog.findViewById(R.id.rel_loader);


    }
    public void show()
    {
        dialog.show();
    }

    public void dismiss()
    {
        if(dialog.isShowing())
        {
            dialog.dismiss();

        }
    }

    public boolean isShowing()
    {
       return dialog.isShowing();
    }
}
