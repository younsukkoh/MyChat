package com.younsukkoh.miscellaneous.mychat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by Younsuk on 3/5/2017.
 */

public class MessagesAdapter extends FirebaseRecyclerAdapter<Message, MessageViewHolder> {

    public MessagesAdapter(Class<Message> modelClass, int modelLayout, Class<MessageViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(MessageViewHolder viewHolder, Message message, int position) {
        viewHolder.bindMessages(message);
    }
}
