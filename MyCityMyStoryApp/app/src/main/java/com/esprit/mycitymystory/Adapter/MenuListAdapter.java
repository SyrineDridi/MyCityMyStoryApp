package com.esprit.mycitymystory.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.mycitymystory.model.MenuItemModel;
import com.esprit.mycitymystory.R;


import java.util.ArrayList;



public class MenuListAdapter extends BaseAdapter {
    private ArrayList<MenuItemModel> menuItemModels;
    private LayoutInflater mInflater;
    private Context context;

    public MenuListAdapter(ArrayList<MenuItemModel> menuItemModels, Context context) {
        this.menuItemModels = menuItemModels;
        this.context = context;
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return menuItemModels.size();
    }

    @Override
    public Object getItem(int position) {
        return menuItemModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        ViewHolder viewHolder;
        MenuItemModel menuItemModel = (MenuItemModel) getItem(position);

        if (convertView==null) {
            view = mInflater.inflate(R.layout.menu_list_item, parent, false);
            setViewHolder(view);
        }else {
            view = convertView;
        }
        viewHolder = (ViewHolder)view.getTag();
        viewHolder.icon.setImageResource(menuItemModel.getIcon());
        viewHolder.title.setText(menuItemModel.getTitle());

        return view;
    }

    public static class ViewHolder {
        ImageView icon;
        TextView title;

    }

    public static void setViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
        viewHolder.title = (TextView) view.findViewById(R.id.title);

        view.setTag(viewHolder);
    }
}
