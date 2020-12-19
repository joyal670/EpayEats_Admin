package com.epayeats.epayeatsadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.epayeats.epayeatsadmin.Model.MenuModel;
import com.epayeats.epayeatsadmin.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;


public class MenuListAdapter extends BaseAdapter
{
    Context context;
    List<MenuModel> menuModel;

    public MenuListAdapter(Context context, List<MenuModel> menuModel) {
        this.context = context;
        this.menuModel = menuModel;
    }

    @Override
    public int getCount() {
        return menuModel.size();
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
        convertView = inflater.inflate(R.layout.menu_listview_items, null);

        TextView listview_menu_name, listview_menu_description;
        TextView listview_menu_offer_price, listview_menu_selling_price;
        TextView listview_menu_unit, listview_menu_actual_price;
        TextView listview_menu_localrestname, listview_menu_approved;
        TextView listview_menu_opentime, listview_menu_closetime, listview_menu_gstno;
        ImageView listview_menu_image1;

        listview_menu_name = convertView.findViewById(R.id.listview_menu_name);
        listview_menu_description = convertView.findViewById(R.id.listview_menu_description);
        listview_menu_offer_price = convertView.findViewById(R.id.listview_menu_offer_price);
        listview_menu_selling_price = convertView.findViewById(R.id.listview_menu_selling_price);
        listview_menu_actual_price = convertView.findViewById(R.id.listview_menu_actual_price);
        listview_menu_unit = convertView.findViewById(R.id.listview_menu_unit);
        listview_menu_image1 = convertView.findViewById(R.id.listview_menu_image1);
        listview_menu_localrestname = convertView.findViewById(R.id.listview_menu_localrestname);
        listview_menu_approved = convertView.findViewById(R.id.listview_menu_approved);
        listview_menu_closetime = convertView.findViewById(R.id.listview_menu_closetime);
        listview_menu_opentime = convertView.findViewById(R.id.listview_menu_opentime);
        listview_menu_gstno = convertView.findViewById(R.id.listview_menu_gstno);

        listview_menu_name.setText(menuModel.get(position).getMenuName());
        listview_menu_description.setText(menuModel.get(position).getMenuDescription());
        listview_menu_offer_price.setText(menuModel.get(position).getMenuOfferPrice());
        listview_menu_selling_price.setText(menuModel.get(position).getMenuSellingPrice());
        listview_menu_actual_price.setText(menuModel.get(position).getMenuActualPrice());
        listview_menu_unit.setText(menuModel.get(position).getMenuUnit());
        listview_menu_localrestname.setText(menuModel.get(position).getRestName());
        listview_menu_approved.setText(menuModel.get(position).getMenuApprovel());
        listview_menu_closetime.setText(menuModel.get(position).getMenuCloseTime());
        listview_menu_opentime.setText(menuModel.get(position).getMenuOpenTime());
        listview_menu_gstno.setText(menuModel.get(position).getGstno());


        Picasso.get().load(menuModel.get(position).getImage1()).into(listview_menu_image1);

        return convertView;
    }
}


