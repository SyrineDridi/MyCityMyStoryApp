package com.esprit.mycitymystory.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.mycitymystory.Entities.Event;
import com.esprit.mycitymystory.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Syrine Dridi on 17/12/2016.
 */

public class EventRecycleviewAdapter extends RecyclerView.Adapter<EventRecycleviewAdapter.CustomViewHolder> {
    private List<Event> feedItemList;
    private Context mContext;

    public EventRecycleviewAdapter(Context context, List<Event> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public EventRecycleviewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_recycleview_item, null);
        EventRecycleviewAdapter.CustomViewHolder viewHolder = new EventRecycleviewAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventRecycleviewAdapter.CustomViewHolder customViewHolder, int i) {
        Event feedItem = feedItemList.get(i);

        //Render image using Picasso library
        //Setting text view title
        if (!TextUtils.isEmpty(feedItem.getThumbUrl())) {
            Picasso.with(mContext).load(feedItem.getThumbUrl()).into(customViewHolder.image);
        }

        customViewHolder.title.setText(feedItem.getEventName());


    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, distance, promos, rating;

        public CustomViewHolder(View view) {
            super(view);
            this.image = (ImageView) view.findViewById(R.id.image);
            this.title = (TextView) view.findViewById(R.id.title);


        }
    }

}
