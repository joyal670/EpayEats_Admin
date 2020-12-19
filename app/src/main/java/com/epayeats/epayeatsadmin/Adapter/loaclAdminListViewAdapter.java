package com.epayeats.epayeatsadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.epayeats.epayeatsadmin.Model.AdminModel;
import com.epayeats.epayeatsadmin.R;

import java.util.List;

public class loaclAdminListViewAdapter extends BaseAdapter
{
    Context context;
    List<AdminModel> adminModel;

    public loaclAdminListViewAdapter(Context context, List<AdminModel> adminModel) {
        this.context = context;
        this.adminModel = adminModel;
    }

    @Override
    public int getCount()
    {
        return adminModel.size();
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
        convertView = inflater.inflate(R.layout.local_admin_report_items, null);

        TextView report_name, report_email, report_phone, report_area;

        report_name = convertView.findViewById(R.id.report_localadmin_name);
        report_email = convertView.findViewById(R.id.report_localadmin_email);
        report_phone = convertView.findViewById(R.id.report_localadmin_phone);
        report_area = convertView.findViewById(R.id.report_localadmin_area);

        report_name.setText(adminModel.get(position).getAdmnName());
        report_email.setText(adminModel.get(position).getAdmnEmail());
        report_phone.setText(adminModel.get(position).getAdmnPhone());
        report_area.setText(adminModel.get(position).getAdmnBusinessArea());

        return convertView;
    }
}
