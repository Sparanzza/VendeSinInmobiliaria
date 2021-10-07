package com.ilerna.vendesininmobiliarias.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.ilerna.vendesininmobiliarias.R;
import com.ilerna.vendesininmobiliarias.Utils.Utils;
import com.ilerna.vendesininmobiliarias.models.Post;

import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;

public class PostsAdapter extends FirestoreRecyclerAdapter<Post, PostsAdapter.ViewHolder> {

    View view;

    public PostsAdapter(FirestoreRecyclerOptions<Post> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Post model) {
        holder.titlePostTextView.setText(model.getTitle());
        holder.pricePostTextView.setText(NumberFormat.getNumberInstance(Locale.US).format(Long.parseLong(model.getPrice())) + " $");
        holder.categoryPostTextView.setText(model.getCategory());
        holder.locationPostTextView.setText("Location Test");

        holder.bedroomPostTextView.setText(model.getBedroom().isEmpty() ? "-" : model.getBedroom());
        holder.bathroomPostTextView.setText(model.getBathroom().isEmpty() ? "-" : model.getBathroom());
        holder.sqmPostTextView.setText(model.getSqm().isEmpty() ? "-" : model.getSqm());
        holder.floorPostTextView.setText(model.getFloor().isEmpty() ? "-" : model.getFloor());
        holder.antiquityPostTextView.setText(model.getAntiquity().isEmpty() ? "-" : model.getAntiquity());


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

    }

    public void setMainPhotoPostImageView(String url) {
        new Utils.ImageDownloadTasK((ImageView) view.findViewById(R.id.mainPhotoPostImageView)).execute(url);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView titlePostTextView;
        TextView pricePostTextView;
        TextView categoryPostTextView;
        TextView locationPostTextView;
        TextView bedroomPostTextView;
        TextView bathroomPostTextView;
        TextView sqmPostTextView;
        TextView floorPostTextView;
        TextView antiquityPostTextView;
        ImageView mainPhotoPostImageView;

        public ViewHolder(View view) {
            super(view);
            titlePostTextView = view.findViewById(R.id.titlePostTextView);
            pricePostTextView = view.findViewById(R.id.pricePostTextView);
            categoryPostTextView = view.findViewById(R.id.categoryPostTextView);
            locationPostTextView = view.findViewById(R.id.locationPostTextView);
            bedroomPostTextView = view.findViewById(R.id.bedroomPostTextView);
            bathroomPostTextView = view.findViewById(R.id.bathroomPostTextView);
            sqmPostTextView = view.findViewById(R.id.sqmPostTextView);
            floorPostTextView = view.findViewById(R.id.floorPostTextView);
            antiquityPostTextView = view.findViewById(R.id.antiquityPostTextView);
            mainPhotoPostImageView = view.findViewById(R.id.mainPhotoPostImageView);
        }
    }

}
