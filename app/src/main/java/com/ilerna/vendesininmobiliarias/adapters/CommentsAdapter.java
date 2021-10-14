package com.ilerna.vendesininmobiliarias.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.Utils.Utils;
import com.ilerna.vendesininmobiliarias.activities.DetailsPostActivity;
import com.ilerna.vendesininmobiliarias.models.Comment;
import com.ilerna.vendesininmobiliarias.models.Post;
import com.ilerna.vendesininmobiliarias.models.User;
import com.ilerna.vendesininmobiliarias.providers.UsersProvider;

import java.text.NumberFormat;
import java.util.Locale;

public class CommentsAdapter extends FirestoreRecyclerAdapter<Comment, CommentsAdapter.ViewHolder> {

    View view;
    Context context;
    UsersProvider up;

    public CommentsAdapter(FirestoreRecyclerOptions<Comment> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Comment model) {
        getUser(holder, model.getUserId());
        holder.textCardCommentTextView.setText(model.getComment());
    }

    public void getUser(ViewHolder holder, String userUid) {
        up.getUser(userUid).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                if (documentSnapshot.contains("username"))
                    holder.usernameCardCommentTextView.setText(documentSnapshot.getString("username"));
                if (documentSnapshot.contains("photoProfile")) {
                    String imageUrl = documentSnapshot.getString("photoProfile");
                    if (imageUrl != null && !imageUrl.isEmpty())
                        new Utils.ImageDownloadTasK(holder.photoProfileCardCommentImageView).execute(imageUrl);
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_comment, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView usernameCardCommentTextView;
        TextView textCardCommentTextView;
        ImageView photoProfileCardCommentImageView;
        View viewHolder;

        public ViewHolder(View view) {
            super(view);
            usernameCardCommentTextView = view.findViewById(R.id.usernameCardCommentTextView);
            textCardCommentTextView = view.findViewById(R.id.textCardCommentTextView);
            photoProfileCardCommentImageView = view.findViewById(R.id.photoProfileCardCommentImageView);
            viewHolder = view;
            up = new UsersProvider();
        }
    }

}
