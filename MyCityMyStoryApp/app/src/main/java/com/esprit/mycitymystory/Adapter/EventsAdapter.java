package com.esprit.mycitymystory.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.esprit.mycitymystory.Entities.EntityEvent;
import com.esprit.mycitymystory.R;
import com.squareup.picasso.Picasso;


/**
 * Created by Syrine on 08/11/2016.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.CustomViewHolder> {
    private List<EntityEvent> feedItemList;
    private Context mContext;

    public EventsAdapter(Context context, List<EntityEvent> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        EntityEvent feedItem = feedItemList.get(i);

        //Render image using Picasso library
        if (!TextUtils.isEmpty(feedItem.getUrlImage())) {
            Picasso.with(mContext).load(feedItem.getUrlImage())
                    .into(customViewHolder.imageView);
        }

        //Setting text view title
        customViewHolder.txtTitle.setText(Html.fromHtml(feedItem.getTitle()));
        customViewHolder.txtPlace.setText(Html.fromHtml(feedItem.getPlace()));
        customViewHolder.txtDate.setText(Html.fromHtml(feedItem.getStartDate()));
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public void removeItem(int position) {
        feedItemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, feedItemList.size());
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView txtTitle;
        protected TextView txtPlace;
        protected TextView txtDate;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.img_row);
            this.txtTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.txtPlace = (TextView) view.findViewById(R.id.tvPlace);
            this.txtDate = (TextView) view.findViewById(R.id.tvDate);

        }
    }
}