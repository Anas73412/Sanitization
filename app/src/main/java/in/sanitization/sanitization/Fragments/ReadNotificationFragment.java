package in.sanitization.sanitization.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.sanitization.sanitization.R;

public class ReadNotificationFragment extends Fragment {
    TextView tv_title,tv_desc;
    public ReadNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_read_notification, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View v) {
        tv_desc=v.findViewById(R.id.tv_desc);
        tv_title=v.findViewById(R.id.tv_title);
        tv_title.setText(Html.fromHtml(getArguments().getString("title")));
        tv_desc.setText(Html.fromHtml(getArguments().getString("desc")));
    }
}