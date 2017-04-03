package com.younsukkoh.miscellaneous.mychat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Younsuk on 3/5/2017.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {

    TextView mUser, mContent, mTimeSent;

    public MessageViewHolder(View itemView) {
        super(itemView);
        mUser = (TextView) itemView.findViewById(R.id.mvh_tv_user);
        mContent = (TextView) itemView.findViewById(R.id.mvh_tv_content);
        mTimeSent = (TextView) itemView.findViewById(R.id.mvh_tv_timeSent);
    }

    /**
     * Set user name, content, time sent
     * @param message
     */
    public void bindMessages(Message message) {
        mUser.setText(message.getUser());
        mContent.setText(message.getContent());
        mTimeSent.setText(Utility.DATE_TIME_FORMAT.format(message.getTimeSent()));
    }
}
