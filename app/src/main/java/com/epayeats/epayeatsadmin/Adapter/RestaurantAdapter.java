package com.epayeats.epayeatsadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.epayeats.epayeatsadmin.Model.RestaurantModel;
import com.epayeats.epayeatsadmin.R;

import java.util.List;

public class RestaurantAdapter extends BaseAdapter
{
    Context context;
    List<RestaurantModel> restaurantModel;

    public RestaurantAdapter(Context context, List<RestaurantModel> restaurantModel) {
        this.context = context;
        this.restaurantModel = restaurantModel;
    }

    @Override
    public int getCount() {
        return restaurantModel.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.restaurant_listview_items, null);

        TextView restaurant__item_name, restaurant__item_phone, restaurant__item_location, restaurant__item_localadmin;

        restaurant__item_name = convertView.findViewById(R.id.restaurant__item_name);
        restaurant__item_phone = convertView.findViewById(R.id.restaurant__item_phone);
        restaurant__item_location = convertView.findViewById(R.id.restaurant__item_location);
        restaurant__item_localadmin = convertView.findViewById(R.id.restaurant__item_localadmin);

        restaurant__item_name.setText(restaurantModel.get(position).getResName());
        restaurant__item_phone.setText(restaurantModel.get(position).getResPhone());
        restaurant__item_location.setText(restaurantModel.get(position).getResLocation());
        restaurant__item_localadmin.setText(restaurantModel.get(position).getResLocalAdminName());

        return convertView;
    }
}

