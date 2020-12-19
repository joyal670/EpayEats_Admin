package com.epayeats.epayeatsadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.epayeats.epayeatsadmin.Model.orderModel;
import com.epayeats.epayeatsadmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ImageHolder>
{
    Context context;
    List<orderModel> mCheckout;
    OnitemClickListener mlistener;

    public OrderAdapter(Context context, List<orderModel> mCheckout) {
        this.context = context;
        this.mCheckout = mCheckout;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.pending_orders_items, parent, false);
        return new ImageHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position)
    {
        orderModel currentUpload = mCheckout.get(position);

        holder.ordered_itemname.setText(currentUpload.getMenuName());
        holder.ordered_itemprice.setText(currentUpload.getOfferPrice());
        holder.ordered_itemqty.setText(currentUpload.getQty());
        holder.ordered_date.setText(currentUpload.getOrderDate());
        holder.ordered_item_rest_name.setText(currentUpload.getRestName());

        if(currentUpload.getOrderStatus().equalsIgnoreCase("0"))
        {
            holder.ordered_itemstatus.setText("Pending, Not yet Delivered");
        }
        else if(currentUpload.getOrderStatus().equalsIgnoreCase("1"))
        {
            holder.ordered_itemstatus.setText("Order is Picked up by the Delivery Agent");

        }
        else if(currentUpload.getOrderStatus().equalsIgnoreCase("2"))
        {

            holder.ordered_itemstatus.setText("Delivered");
        }
        else
        {
            holder.ordered_itemstatus.setText("Cancelled");
        }

        Picasso.get()
                .load(currentUpload.getMenuImage())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.ordered_itemimage);

        int price = Integer.parseInt(currentUpload.getOfferPrice());
        int qty = Integer.parseInt(currentUpload.getQty());
        int total = qty * price;
        String tot = String.valueOf(total);

        holder.ordered_itemtotal.setText(tot);
    }


    @Override
    public int getItemCount() {
        return mCheckout.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView ordered_itemname, ordered_itemqty, ordered_itemprice, ordered_itemstatus, ordered_date, ordered_itemtotal, ordered_item_rest_name;
        ImageView ordered_itemimage;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);

            ordered_itemimage = itemView.findViewById(R.id.ordered_itemimage);
            ordered_itemqty = itemView.findViewById(R.id.ordered_itemqty);
            ordered_itemprice = itemView.findViewById(R.id.ordered_itemprice);
            ordered_itemstatus = itemView.findViewById(R.id.ordered_itemstatus);
            ordered_itemname = itemView.findViewById(R.id.ordered_itemname);
            ordered_date = itemView.findViewById(R.id.ordered_date);
            ordered_item_rest_name = itemView.findViewById(R.id.ordered_item_rest_name);
            ordered_itemtotal = itemView.findViewById(R.id.ordered_itemtotal);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mlistener != null)
            {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION)
                {
                    mlistener.onItemClick(position);
                }
            }
        }
    }

    public interface OnitemClickListener{
        void onItemClick(int position);
    }
    public void setOnClickListener(OnitemClickListener listener)
    {
        mlistener = listener;
    }
}
