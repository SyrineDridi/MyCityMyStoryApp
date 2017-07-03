package com.esprit.mycitymystory.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.mycitymystory.R;
import com.esprit.mycitymystory.model.NewsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class NewsListAdapter extends BaseAdapter {
    private ArrayList<NewsModel> newsModels;
    private LayoutInflater mInflater;
    private Context context;

    public NewsListAdapter(ArrayList<NewsModel> newsModels, Context context) {
        this.newsModels = newsModels;
        this.context = context;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return newsModels.size();
    }

    @Override
    public Object getItem(int position) {
        return newsModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        ViewHolder viewHolder;
        NewsModel newsModel = (NewsModel) getItem(position);

        if (convertView==null) {
            view = mInflater.inflate(R.layout.news_item, parent, false);
            setViewHolder(view);
        }else {
            view = convertView;
        }
        viewHolder = (ViewHolder)view.getTag();
        Picasso.with(context).load(newsModel.getLogoIcon()).into(viewHolder.logoIcon);
        viewHolder.newsTitle.setText(newsModel.getTitle());
        viewHolder.newsText.setText(newsModel.getText());
        if (newsModel.getImage() != null) {
            Picasso.with(context).load(newsModel.getImage()).into(viewHolder.newsImage);
        }

        return view;
    }

    public static class ViewHolder {
        ImageView logoIcon, newsImage;
        TextView newsTitle, newsText;

    }

    public static void setViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.logoIcon = (ImageView) view.findViewById(R.id.logo_icon);
        viewHolder.newsImage = (ImageView) view.findViewById(R.id.news_image);
        viewHolder.newsTitle = (TextView) view.findViewById(R.id.news_title);
        viewHolder.newsText = (TextView) view.findViewById(R.id.news_text);

        view.setTag(viewHolder);
    }
}

