package com.esprit.mycitymystory.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.mycitymystory.Entities.EntityEvent;
import com.esprit.mycitymystory.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Syrine Dridi on 17/12/2016.
 */

 public class ExploreRecycleviewAdapter extends RecyclerView.Adapter<ExploreRecycleviewAdapter.CustomViewHolder> {
    private List<EntityEvent> feedItemList;
    private Context mContext;
    int width  , height ;

    public ExploreRecycleviewAdapter(Context context, List<EntityEvent> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public ExploreRecycleviewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_listview_item, null);
        ExploreRecycleviewAdapter.CustomViewHolder viewHolder = new ExploreRecycleviewAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExploreRecycleviewAdapter.CustomViewHolder customViewHolder, int i) {
        EntityEvent feedItem = feedItemList.get(i);

        getWeithAndHeight((Activity)mContext);
        if (!TextUtils.isEmpty(feedItem.getUrlImage())) {
            Picasso.with(mContext).load(feedItem.getUrlImage()).resize(width,width/2 ).into(customViewHolder.image);
        }

        customViewHolder.title.setText(feedItem.getTitle());
        customViewHolder.distance.setText(feedItem.getCategory());
        customViewHolder.promos.setText(feedItem.getStartDate());
        customViewHolder.rating.setText(feedItem.getEndDate());

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
            this.distance = (TextView) view.findViewById(R.id.distance);
            this.promos = (TextView) view.findViewById(R.id.promos);
            this.rating = (TextView) view.findViewById(R.id.rating);

        }
    }
    public  void getWeithAndHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
       width= dm.widthPixels;
        height = dm.heightPixels;

    }

}
