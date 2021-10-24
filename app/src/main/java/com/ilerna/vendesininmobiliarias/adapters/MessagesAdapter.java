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
import com.ilerna.vendesininmobiliarias.activities.ChatActivity;
import com.ilerna.vendesininmobiliarias.models.Chat;
import com.ilerna.vendesininmobiliarias.models.Message;
import com.ilerna.vendesininmobiliarias.providers.FirebaseAuthProvider;
import com.ilerna.vendesininmobiliarias.providers.UsersProvider;

public class MessagesAdapter extends FirestoreRecyclerAdapter<Message, MessagesAdapter.ViewHolder> {

    View view;
    Context context;
    UsersProvider up;
    FirebaseAuthProvider fap;

    public MessagesAdapter(FirestoreRecyclerOptions<Message> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, Message model) {
        // DocumentSnapshot document = getSnapshots().getSnapshot(position);
        // final String messageId = document.getId();
        holder.messageTextView.setText(model.getText());
        holder.dateMessageTextView.setText(Utils.getTimeAgo(model.getTimestamp()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView messageTextView;
        TextView dateMessageTextView;
        ImageView messageCheckdimageView;
        View viewHolder;

        public ViewHolder(View view) {
            super(view);
            messageTextView = view.findViewById(R.id.messageTextView);
            dateMessageTextView = view.findViewById(R.id.dateMessageTextView);
            messageCheckdimageView = view.findViewById(R.id.messageCheckdimageView);
            viewHolder = view;
            up = new UsersProvider();
            fap = new FirebaseAuthProvider();
        }
    }

}
