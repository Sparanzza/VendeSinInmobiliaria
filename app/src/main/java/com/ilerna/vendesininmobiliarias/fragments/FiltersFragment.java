package com.ilerna.vendesininmobiliarias.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.Utils.CategoriesEnum;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FiltersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FiltersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ImageView imageViewHomes, imageViewOffices, imageViewFactories, imageViewFlats, imageViewStorages, imageViewFields, imageViewGarages, imageViewCommercials, imageViewAll;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FiltersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FiltersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FiltersFragment newInstance(String param1, String param2) {
        FiltersFragment fragment = new FiltersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filters, container, false);

        //Categories
        imageViewHomes = view.findViewById(R.id.imageViewHomes);
        imageViewOffices = view.findViewById(R.id.imageViewOffices);
        imageViewFactories = view.findViewById(R.id.imageViewFactories);
        imageViewFlats = view.findViewById(R.id.imageViewFlats);
        imageViewStorages = view.findViewById(R.id.imageViewStorages);
        imageViewFields = view.findViewById(R.id.imageViewFields);
        imageViewGarages = view.findViewById(R.id.imageViewGarages);
        imageViewCommercials = view.findViewById(R.id.imageViewCommercials);
        imageViewAll = view.findViewById(R.id.imageViewAll);

        imageViewHomes.setOnClickListener(v -> {
            openFragment(HomeFragment.newInstance(CategoriesEnum.HOMES.name(), ""));
        });

        imageViewOffices.setOnClickListener(v -> {
            openFragment(HomeFragment.newInstance(CategoriesEnum.OFFICES.name(), ""));
        });

        imageViewFactories.setOnClickListener(v -> {
            openFragment(HomeFragment.newInstance(CategoriesEnum.FACTORIES.name(), ""));
        });

        imageViewFlats.setOnClickListener(v -> {
            openFragment(HomeFragment.newInstance(CategoriesEnum.FLATS.name(), ""));
        });

        imageViewStorages.setOnClickListener(v -> {
            openFragment(HomeFragment.newInstance(CategoriesEnum.STORAGES.name(), ""));
        });

        imageViewFields.setOnClickListener(v -> {
            openFragment(HomeFragment.newInstance(CategoriesEnum.FIELDS.name(), ""));
        });

        imageViewGarages.setOnClickListener(v -> {
            openFragment(HomeFragment.newInstance(CategoriesEnum.GARAGES.name(), ""));
        });

        imageViewCommercials.setOnClickListener(v -> {
            openFragment(HomeFragment.newInstance(CategoriesEnum.COMMERCIALS.name(), ""));
        });

        imageViewAll.setOnClickListener(v -> {
            openFragment(HomeFragment.newInstance("ALL", ""));
        });

        return view;
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}