package com.esprit.mycitymystory.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.mycitymystory.Entities.EntityEvent;
import com.esprit.mycitymystory.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExploreListviewAdapter extends BaseAdapter {

    private ArrayList<EntityEvent> nearbyModels;
    private LayoutInflater mInflater;
    private Context context;

    public ExploreListviewAdapter(ArrayList<EntityEvent> nearbyModels, Context context) {
        this.nearbyModels = nearbyModels;
        this.context = context;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return nearbyModels.size();
    }

    @Override
    public Object getItem(int position) {
        return nearbyModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        ViewHolder viewHolder;
        EntityEvent nearbyModel = (EntityEvent) getItem(position);

        if (convertView==null) {
            view = mInflater.inflate(R.layout.main_listview_item, parent, false);
            setViewHolder(view);
        }else {
            view = convertView;
        }
        viewHolder = (ViewHolder)view.getTag();
        Picasso.with(context).load(nearbyModel.getUrlImage()).into(viewHolder.image);
        viewHolder.title.setText(nearbyModel.getTitle());
        viewHolder.distance.setText(nearbyModel.getCategory());
        viewHolder.promos.setText(nearbyModel.getStartDate());
        viewHolder.rating.setText(nearbyModel.getEndDate());

        return view;
    }

    public static class ViewHolder {
        ImageView image;
        TextView title, distance, promos, rating;

    }

    public static void setViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.image = (ImageView) view.findViewById(R.id.image);
        viewHolder.title = (TextView) view.findViewById(R.id.title);
        viewHolder.distance = (TextView) view.findViewById(R.id.distance);
        viewHolder.promos = (TextView) view.findViewById(R.id.promos);
        viewHolder.rating = (TextView) view.findViewById(R.id.rating);

        view.setTag(viewHolder);
    }
}
