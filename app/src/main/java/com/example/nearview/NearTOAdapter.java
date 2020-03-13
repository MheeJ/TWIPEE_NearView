package com.example.nearview;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class NearTOAdapter extends BaseAdapter {
    private ArrayList<NearToDTO>listTourist=new ArrayList<>();
    @Override
    public int getCount() {
        return listTourist.size();
    }

    @Override
    public Object getItem(int i) {
        return listTourist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position , View convertView , ViewGroup parent) {
        TouristViewHolder holder;
        if (convertView==null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.near_trip_list , null , false);

            holder = new TouristViewHolder();
            holder.imageViewTourist = (ImageView) convertView.findViewById(R.id.tourist_image);
            holder.textNearPosition = (TextView) convertView.findViewById(R.id.nearposition);
            holder.textNearPositionDetail = (TextView) convertView.findViewById(R.id.nearposition_detail);
            holder.textDistance = (TextView) convertView.findViewById(R.id.distance);

            convertView.setTag(holder);
        }else{
            holder=(TouristViewHolder)convertView.getTag();
        }
        NearToDTO dto=listTourist.get(position);
        holder.imageViewTourist.setImageBitmap(dto.getResld_tourist());
        holder.textNearPosition.setText(dto.getNearposition());
        holder.textNearPositionDetail.setText(dto.getNearposition_detail());
        holder.textDistance.setText(dto.getDistance());
        return convertView;

    }
    public void addItem(NearToDTO dto){listTourist.add(dto);}
    public void addItems(ArrayList<NearToDTO> dtos){listTourist.addAll(dtos);}
    class TouristViewHolder{
        ImageView imageViewTourist;
        TextView textNearPosition;
        TextView textNearPositionDetail;
        TextView textDistance;
    }
}
