package in.sanitization.sanitization.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.sanitization.sanitization.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivacyFragment extends Fragment {

    private TextView textView;

    public PrivacyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_contact, container, false);
        textView = view.findViewById(R.id.text);
        return view;
    }
}
