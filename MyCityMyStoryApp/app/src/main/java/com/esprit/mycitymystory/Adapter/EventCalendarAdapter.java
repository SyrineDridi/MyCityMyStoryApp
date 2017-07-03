package com.esprit.mycitymystory.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esprit.mycitymystory.Entities.EntityEvent;
import com.esprit.mycitymystory.R;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.List;

/**
 * Created by Syrine Dridi on 30/11/2016.
 */

public class EventCalendarAdapter extends RecyclerView.Adapter<EventCalendarAdapter.CustomViewHolder> {
    private List<Event> feedItemList;
    private Context mContext;

    public EventCalendarAdapter(Context context, List<Event> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public EventCalendarAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_calendar_item, null);
        EventCalendarAdapter.CustomViewHolder viewHolder = new EventCalendarAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventCalendarAdapter.CustomViewHolder customViewHolder, int i) {
        Event feedItem = feedItemList.get(i);

        //Render image using Picasso library


        //Setting text view title
        customViewHolder.textTitle.setText(Html.fromHtml(String.valueOf(((EntityEvent)feedItem.getData()).getTitle())));
        customViewHolder.txtPlace.setText(Html.fromHtml(String.valueOf(((EntityEvent)feedItem.getData()).getPlace())));
        customViewHolder.textDate.setText(Html.fromHtml(String.valueOf(((EntityEvent)feedItem.getData()).getStartDate())));


    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView textTitle;
        protected TextView txtPlace;
        protected TextView textDate;

        public CustomViewHolder(View view) {
            super(view);
             this.textTitle = (TextView) view.findViewById(R.id.txtTitle);
            this.txtPlace = (TextView) view.findViewById(R.id.txtPlace);
            this.textDate = (TextView) view.findViewById(R.id.txtDate);
        }
    }

}
