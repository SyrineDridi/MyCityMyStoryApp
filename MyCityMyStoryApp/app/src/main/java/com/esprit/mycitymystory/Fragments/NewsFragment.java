package com.esprit.mycitymystory.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.esprit.mycitymystory.Adapter.NewsListAdapter;
import com.esprit.mycitymystory.R;
import com.esprit.mycitymystory.model.NewsModel;

import java.util.ArrayList;



/**
 * Fragment for displaying & Managing News Tab
 *
 * @author luchfilip
 */
public class NewsFragment extends Fragment {

    ListView listView;
    NewsListAdapter newsListAdapter;
    ArrayList<NewsModel> newsModels = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstance) {
        View view = inflater.inflate(R.layout.news_fragment, viewGroup, false);
        listView = (ListView) view.findViewById(R.id.listview);

        setNewsData();
        newsListAdapter = new NewsListAdapter(newsModels, getContext());
        listView.setAdapter(newsListAdapter);

        return view;
    }

    public void setNewsData() {
        newsModels.clear();

        for (int i = 0; i < 3; i++) {
            NewsModel newsModel1 = new NewsModel();
            newsModel1.setLogoIcon("http://i.imgur.com/YJRDWHu.png");
            newsModel1.setTitle("limited offer!");
            newsModel1.setText("New Bussiness Lunch added in menu");
            newsModel1.setImage("http://i.imgur.com/We5SBiw.jpg");
            newsModels.add(newsModel1);

            NewsModel newsModel2 = new NewsModel();
            newsModel2.setLogoIcon("http://i.imgur.com/O4Iad8I.jpg");
            newsModel2.setTitle("checkin");
            newsModel2.setText("Gabi Merlot has checked in at Trattoria!");
            newsModels.add(newsModel2);
        }

    }

}
