package com.ilerna.vendesininmobiliarias.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.Utils.CategoriesEnum;
import com.ilerna.vendesininmobiliarias.activities.AddPostActivity;
import com.ilerna.vendesininmobiliarias.adapters.PostsAdapter;
import com.ilerna.vendesininmobiliarias.api.FCMApi;
import com.ilerna.vendesininmobiliarias.models.Post;
import com.ilerna.vendesininmobiliarias.providers.PostsProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView postHomeRecyclerView;
    PostsProvider pp;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;
    FloatingActionButton fab;
    PostsAdapter postsAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query;
        if (getArguments().getString("param1").equals("ALL") || getArguments().getString("param1").isEmpty()) {
            if (getArguments().getString("param2").isEmpty()) query = pp.getAllPosts();
            else query = pp.getAllPostsByTitle(getArguments().getString("param2"));
        } else query = pp.getAllPostsByCategory(getArguments().getString("param1"));

        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class).build();
        postsAdapter = new PostsAdapter(options, getContext());
        postHomeRecyclerView.setAdapter(postsAdapter);
        postsAdapter.startListening(); // Start listening from FireStore database
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
    public void onStop() {
        super.onStop();
        postsAdapter.stopListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pp = new PostsProvider();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(view -> goToPost());
        postHomeRecyclerView = view.findViewById(R.id.postHomeRecyclerView);
        if (
                (getArguments().getString("param1").equals("ALL") || getArguments().getString("param1").isEmpty())
                && getArguments().getString("param2").isEmpty()
        ) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            postHomeRecyclerView.setLayoutManager(linearLayoutManager);
        } else {
            postHomeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }

        return view;
    }

    private void goToPost() {
        Intent intent = new Intent(getContext(), AddPostActivity.class);
        startActivity(intent);
    }
}