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

public class LocaladminAdapter extends BaseAdapter
{
    Context context;
    List<AdminModel> mAdminModel;

    public LocaladminAdapter(Context context, List<AdminModel> mAdminModel)
    {
        this.context = context;
        this.mAdminModel = mAdminModel;
    }

    @Override
    public int getCount() {
        return mAdminModel.size();
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
        convertView = inflater.inflate(R.layout.local_admin_listview_items, null);

        TextView local_admin_name, local_admin_address, local_admin_businessKM, local_admin_aadharNo, local_admin_validity;
        TextView local_admin_businessArea, local_admin_GSTNo, local_admin_PancardNo, local_admin_isBlocked, local_admin_phone, local_admin_place;

        local_admin_name = convertView.findViewById(R.id.local_admin_name);
        local_admin_address = convertView.findViewById(R.id.local_admin_address);
        local_admin_businessKM = convertView.findViewById(R.id.local_admin_businessKM);
        local_admin_aadharNo = convertView.findViewById(R.id.local_admin_aadharNo);
        local_admin_validity = convertView.findViewById(R.id.local_admin_validity);
        local_admin_businessArea = convertView.findViewById(R.id.local_admin_businessArea);
        local_admin_GSTNo = convertView.findViewById(R.id.local_admin_GSTNo);
        local_admin_PancardNo = convertView.findViewById(R.id.local_admin_PancardNo);
        local_admin_isBlocked = convertView.findViewById(R.id.local_admin_isBlocked);
        local_admin_phone = convertView.findViewById(R.id.local_admin_phone);
        local_admin_place = convertView.findViewById(R.id.local_admin_place);

        local_admin_name.setText(mAdminModel.get(position).getAdmnName());
        local_admin_address.setText(mAdminModel.get(position).getAdmnAddress());
        local_admin_businessKM.setText(mAdminModel.get(position).getAdmnBusinessKM());
        local_admin_aadharNo.setText(mAdminModel.get(position).getAdmnaadharNo());
        local_admin_validity.setText(mAdminModel.get(position).getAdmnValidaty());
        local_admin_businessArea.setText(mAdminModel.get(position).getAdmnBusinessArea());
        local_admin_GSTNo.setText(mAdminModel.get(position).getAdmnGSTNo());
        local_admin_PancardNo.setText(mAdminModel.get(position).getAdmnPancardNo());
        local_admin_isBlocked.setText(mAdminModel.get(position).getIsBlocked());
        local_admin_phone.setText(mAdminModel.get(position).getAdmnPhone());
        local_admin_place.setText(mAdminModel.get(position).getTemp1());

        return convertView;
    }
}

