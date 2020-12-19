package com.epayeats.epayeatsadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.epayeats.epayeatsadmin.Model.FoodCatagoryModel;
import com.epayeats.epayeatsadmin.R;

import java.util.List;

public class FoodCatagoryAdapter extends BaseAdapter
{
    Context context;
    List<FoodCatagoryModel> foodCatagoryModel;

    public FoodCatagoryAdapter(Context context, List<FoodCatagoryModel> foodCatagoryModel) {
        this.context = context;
        this.foodCatagoryModel = foodCatagoryModel;
    }

    @Override
    public int getCount() {
        return foodCatagoryModel.size();
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
        convertView = inflater.inflate(R.layout.food_catagorey_listview_items, null);

        TextView foodcatagory_editText;

        foodcatagory_editText = convertView.findViewById(R.id.foodcatagory_editText);

        foodcatagory_editText.setText(foodCatagoryModel.get(position).getFoodCatagoreyType());

        return convertView;
    }
}

