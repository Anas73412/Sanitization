package in.binplus.sanitization.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.binplus.sanitization.Adapter.PackageAdapter;
import in.binplus.sanitization.Model.PackageModel;
import in.binplus.sanitization.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PackagesFragment extends Fragment {

    PackageAdapter packageAdapter;
    ArrayList<PackageModel> list;
    RecyclerView rv_package;
    public PackagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_packages, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        rv_package=view.findViewById(R.id.rv_package);
        list=new ArrayList<>();
        list.add(new PackageModel("1","Package 1","300","description1"));
        list.add(new PackageModel("2","Package 2","400","description2"));
        list.add(new PackageModel("3","Package 3","500","description3"));
        list.add(new PackageModel("4","Package 4","600","description4"));
        list.add(new PackageModel("5","Package 5","700","description5"));
        list.add(new PackageModel("6","Package 6","800","description6"));
        list.add(new PackageModel("7","Package 7","900","description7"));

        packageAdapter=new PackageAdapter(getActivity(),list);
        rv_package.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_package.setAdapter(packageAdapter);
        packageAdapter.notifyDataSetChanged();
    }
}
