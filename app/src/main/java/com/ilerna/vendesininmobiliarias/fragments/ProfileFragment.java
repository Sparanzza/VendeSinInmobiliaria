package com.ilerna.vendesininmobiliarias.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.Utils.Utils;
import com.ilerna.vendesininmobiliarias.activities.EditProfileActivity;
import com.ilerna.vendesininmobiliarias.adapters.PostsAdapter;
import com.ilerna.vendesininmobiliarias.adapters.UserPostAdapter;
import com.ilerna.vendesininmobiliarias.models.Post;
import com.ilerna.vendesininmobiliarias.providers.FirebaseAuthProvider;
import com.ilerna.vendesininmobiliarias.providers.ImagesProvider;
import com.ilerna.vendesininmobiliarias.providers.PostsProvider;
import com.ilerna.vendesininmobiliarias.providers.UsersProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FloatingActionButton editProfileFab;
    View view;

    TextView existPostProfileTextView;
    TextView usernameProfileTextView;
    TextView phoneProfileTextView;
    TextView emailProfileTextView;
    TextView totalPostsProfileTextView;
    RecyclerView postsProfileRecyclerView;

    ListenerRegistration listenerRegistration;

    ImagesProvider ip;
    UsersProvider up;
    FirebaseAuthProvider fap;
    PostsProvider pp;

    UserPostAdapter userPostAdapter;

    public ProfileFragment() {
        // Required empty public constructorZ
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    public void onStart() {
        super.onStart();
        Query query = pp.getAllPostByUser(fap.getCurrentUid());
        FirestoreRecyclerOptions<Post> options =
                new FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(query, Post.class).build();
        userPostAdapter = new UserPostAdapter(options, getContext());
        postsProfileRecyclerView.setAdapter(userPostAdapter);
        userPostAdapter.startListening(); // Start listening from FireStore database
    }

    @Override
    public void onStop() {
        super.onStop();
        userPostAdapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (listenerRegistration != null) listenerRegistration.remove();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        editProfileFab = view.findViewById(R.id.editProfileFab);
        usernameProfileTextView = view.findViewById(R.id.usernameProfileTextView);
        emailProfileTextView = view.findViewById(R.id.emailProfileTextView);
        phoneProfileTextView = view.findViewById(R.id.phoneProfileTextView);
        totalPostsProfileTextView = view.findViewById(R.id.totalPostsProfileTextView);
        postsProfileRecyclerView = view.findViewById(R.id.postsProfileRecyclerView);
        existPostProfileTextView = view.findViewById(R.id.existPostProfileTextView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        postsProfileRecyclerView.setLayoutManager(linearLayoutManager);

        editProfileFab.setOnClickListener(view -> goToEditProfile());

        ip = new ImagesProvider();
        up = new UsersProvider();
        fap = new FirebaseAuthProvider();
        pp = new PostsProvider();

        getUserProfile();
        getPosts();
        return view;
    }

    private void goToEditProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void getUserProfile() {
        listenerRegistration = pp.getAllPostByUser(fap.getCurrentUid()).addSnapshotListener((querySnapshot, exception) -> {
            if (querySnapshot.size() > 0) {
                existPostProfileTextView.setText(querySnapshot.size() + " Posts");
            } else {
                existPostProfileTextView.setText("No Post");
            }
        });

        up.getUser(fap.getCurrentUid()).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                if (documentSnapshot.contains("username")) {
                    String username = documentSnapshot.getString("username");
                    if (username != null && !username.isEmpty())
                        usernameProfileTextView.setText(username);
                }
                if (documentSnapshot.contains("email")) {
                    String email = documentSnapshot.getString("email");
                    if (email != null && !email.isEmpty())
                        emailProfileTextView.setText(email);
                }
                if (documentSnapshot.contains("phoneNumber")) {
                    String phoneNumber = documentSnapshot.getString("phoneNumber");
                    if (phoneNumber != null && !phoneNumber.isEmpty())
                        phoneProfileTextView.setText(phoneNumber);
                }
                if (documentSnapshot.contains("photoProfile")) {
                    String photoProfile = documentSnapshot.getString("photoProfile");
                    if (photoProfile != null && !photoProfile.isEmpty())
                        new Utils.ImageDownloadTasK((ImageView) view.findViewById(R.id.photoProfileImageView)).execute(photoProfile);
                }
            } else {
                Toast.makeText(getContext(), "The user dosen't exist.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getPosts() {
        pp.getAllPostByUser(fap.getCurrentUid()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            int totalPosts = queryDocumentSnapshots.size();
            totalPostsProfileTextView.setText(String.valueOf(totalPosts));
        });
    }
}