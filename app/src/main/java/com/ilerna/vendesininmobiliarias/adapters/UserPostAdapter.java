package com.ilerna.vendesininmobiliarias.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.Utils.Utils;
import com.ilerna.vendesininmobiliarias.activities.DetailsPostActivity;
import com.ilerna.vendesininmobiliarias.models.Like;
import com.ilerna.vendesininmobiliarias.models.Post;
import com.ilerna.vendesininmobiliarias.providers.FirebaseAuthProvider;
import com.ilerna.vendesininmobiliarias.providers.LikesProvider;
import com.ilerna.vendesininmobiliarias.providers.PostsProvider;
import com.ilerna.vendesininmobiliarias.providers.UsersProvider;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class UserPostAdapter extends FirestoreRecyclerAdapter<Post, UserPostAdapter.ViewHolder> {

    View view;
    Context context;
    UsersProvider up;
    LikesProvider lp;
    FirebaseAuthProvider fap;
    PostsProvider pp;

    public UserPostAdapter(FirestoreRecyclerOptions<Post> options, Context context) {
        super(options);
        this.context = context;
        up = new UsersProvider();
        lp = new LikesProvider();
        pp = new PostsProvider();
        fap = new FirebaseAuthProvider();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post model) {

        holder.titleCardUserPostTextView.setText(model.getTitle());
        holder.timeCardUserPostTextView.setText(Utils.getTimeAgo(model.getTimestamp()));

        if (model.getImage0() != null && !model.getImage0().isEmpty())
            setMainPhotoPostImageView(model.getImage0());
        else if (model.getImage1() != null && !model.getImage1().isEmpty())
            setMainPhotoPostImageView(model.getImage1());
        else if (model.getImage2() != null && !model.getImage2().isEmpty())
            setMainPhotoPostImageView(model.getImage2());
        else if (model.getImage3() != null && !model.getImage3().isEmpty())
            setMainPhotoPostImageView(model.getImage3());
        else if (model.getImage4() != null && !model.getImage4().isEmpty())
            setMainPhotoPostImageView(model.getImage4());
        else if (model.getImage5() != null && !model.getImage5().isEmpty())
            setMainPhotoPostImageView(model.getImage5());
        else if (model.getImage6() != null && !model.getImage6().isEmpty())
            setMainPhotoPostImageView(model.getImage6());
        else if (model.getImage7() != null && !model.getImage7().isEmpty())
            setMainPhotoPostImageView(model.getImage7());

        view.setOnClickListener(view -> {
            DocumentSnapshot doc = getSnapshots().getSnapshot(position);
            String postId = doc.getId();

            Intent intent = new Intent(context, DetailsPostActivity.class);
            intent.putExtra("postId", postId);
            context.startActivity(intent);
        });

        holder.cancelCardUserPostButton.setOnClickListener(view -> {
            pp.deletePost(getSnapshots().getSnapshot(position).getId()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Delete post successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error on delete post", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public void setMainPhotoPostImageView(String url) {
        new Utils.ImageDownloadTasK(view.findViewById(R.id.photoCardUserPostImageView)).execute(url);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_user_post, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeCardUserPostTextView;
        TextView titleCardUserPostTextView;
        ImageView photoCardUserPostImageView;
        ImageView cancelCardUserPostButton;
        View viewHolder;

        public ViewHolder(View view) {
            super(view);
            timeCardUserPostTextView = view.findViewById(R.id.timeCardUserPostTextView);
            titleCardUserPostTextView = view.findViewById(R.id.titleCardUserPostTextView);
            photoCardUserPostImageView = view.findViewById(R.id.photoCardUserPostImageView);
            cancelCardUserPostButton = view.findViewById(R.id.cancelCardUserPostButton);
            viewHolder = view;
        }
    }

}
