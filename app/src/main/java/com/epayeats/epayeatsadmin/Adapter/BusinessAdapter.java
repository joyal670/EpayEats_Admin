package com.epayeats.epayeatsadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.epayeats.epayeatsadmin.Model.BusinessModel;
import com.epayeats.epayeatsadmin.R;

import java.util.List;

public class BusinessAdapter extends BaseAdapter
{
    Context context;
    List<BusinessModel> mBusinessModel;

    public BusinessAdapter(Context context, List<BusinessModel> mBusinessModel) {
        this.context = context;
        this.mBusinessModel = mBusinessModel;
    }

    @Override
    public int getCount() {
        return mBusinessModel.size();
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
        convertView = inflater.inflate(R.layout.business_listview_items, null);

        TextView business_adapter_location_textview;

        business_adapter_location_textview = convertView.findViewById(R.id.business_adapter_location_textview);

        business_adapter_location_textview.setText(mBusinessModel.get(position).getLocation());

        return convertView;
    }
}
