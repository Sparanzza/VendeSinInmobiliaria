package com.ilerna.vendesininmobiliarias.adapters;

import android.content.Context;
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
import com.ilerna.vendesininmobiliarias.models.Chat;
import com.ilerna.vendesininmobiliarias.providers.FirebaseAuthProvider;
import com.ilerna.vendesininmobiliarias.providers.UsersProvider;

public class ChatsAdapter extends FirestoreRecyclerAdapter<Chat, ChatsAdapter.ViewHolder> {

    View view;
    Context context;
    UsersProvider up;
    FirebaseAuthProvider fap;

    public ChatsAdapter(FirestoreRecyclerOptions<Chat> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Chat model) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(position);
        String chatId = documentSnapshot.getId();

        if (fap.getCurrentUid().equals(model.getUserHome())) getUser(holder, model.getUserAway());
        else getUser(holder, model.getUserHome());
        // holder.lastMsgCardViewChatsTextView.setText(model.getChat());
    }

    public void getUser(ViewHolder holder, String userUid) {
        up.getUser(userUid).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                if (documentSnapshot.contains("username"))
                    holder.usernameCardViewChatsTextView.setText(documentSnapshot.getString("username"));
                if (documentSnapshot.contains("photoProfile")) {
                    String imageUrl = documentSnapshot.getString("photoProfile");
                    if (imageUrl != null && !imageUrl.isEmpty())
                        new Utils.ImageDownloadTasK(holder.photoCardViewChatsImageView).execute(imageUrl);
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_chats, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView usernameCardViewChatsTextView;
        TextView lastMsgCardViewChatsTextView;
        ImageView photoCardViewChatsImageView;
        View viewHolder;

        public ViewHolder(View view) {
            super(view);
            usernameCardViewChatsTextView = view.findViewById(R.id.usernameCardViewChatsTextView);
            lastMsgCardViewChatsTextView = view.findViewById(R.id.lastMsgCardViewChatsTextView);
            photoCardViewChatsImageView = view.findViewById(R.id.photoCardViewChatsImageView);
            viewHolder = view;
            up = new UsersProvider();
            fap = new FirebaseAuthProvider();
        }
    }

}
