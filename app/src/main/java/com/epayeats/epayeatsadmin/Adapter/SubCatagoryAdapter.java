package com.epayeats.epayeatsadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.epayeats.epayeatsadmin.Model.SubCatagoryModel;
import com.epayeats.epayeatsadmin.R;

import java.util.List;

public class SubCatagoryAdapter extends BaseAdapter
{
    Context context;
    List<SubCatagoryModel> model;

    public SubCatagoryAdapter(Context context, List<SubCatagoryModel> model) {
        this.context = context;
        this.model = model;
    }

    @Override
    public int getCount() {
        return model.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.sub_catagory, null);

        TextView sub_menu_adapter_textview;

        sub_menu_adapter_textview = convertView.findViewById(R.id.sub_menu_adapter_textview);

        sub_menu_adapter_textview.setText(model.get(position).getSubCatagoryName());


        return convertView;
    }
}
