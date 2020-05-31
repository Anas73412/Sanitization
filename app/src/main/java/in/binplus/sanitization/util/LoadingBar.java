package in.binplus.sanitization.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.agrawalsuneet.dotsloader.loaders.LightsLoader;
import com.agrawalsuneet.dotsloader.loaders.LinearDotsLoader;

import in.binplus.sanitization.R;


/**
 * Developed by Binplus Technologies pvt. ltd.  on 06,April,2020
 */
public class LoadingBar {
    Context context;
    Dialog dialog;
    RelativeLayout rel_loader;
   LinearDotsLoader light_Loader ;


    public LoadingBar(Context context) {
        this.context = context;

        dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.loading_layout);
        dialog.setCanceledOnTouchOutside(false);
        rel_loader =dialog.findViewById(R.id.rel_loader);
        light_Loader = dialog.findViewById(R.id.light_loader);
        LinearDotsLoader loader = new LinearDotsLoader(context);
        loader.setAnimDur(10000);
//                LightsLoader lightsLoader = new LightsLoader(
//                context, 8,
//                10, 10,
//                ContextCompat.getColor(context, R.color.colorAccent));

//      rel_loader.addView(loader);
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
