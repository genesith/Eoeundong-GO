package com.example.q.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import static com.example.q.myapplication.OnspotVerification.getRestaurantNameFromID;
import static com.example.q.myapplication.StatusClass.getImageForLoad;
import static com.example.q.myapplication.StatusClass.getUserByUserID;

public class StatusListAdapter extends BaseAdapter {
    int groupid;
    ArrayList<StatusClass> StatusList;
    Context context;

    public StatusListAdapter(Context context, int vg, ArrayList<StatusClass> StatusFeed){
        this.context=context;
        groupid=vg;
        StatusList = StatusFeed;
    }
    // Hold views of the ListView to improve its scrolling performance
    public class ViewHolder {
        public ImageView TheImage, Heart;
        public TextView ResText, StarText, StatusText, DateText, LikeText;
    }
    public Object getItem(int position) {
        return  StatusList.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    public  int getCount() {
        return StatusList.size();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        ViewHolder viewHolder;
        // Inflate the list_item.xml file if convertView is null
        if(rowView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView= inflater.inflate(groupid, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.TheImage= (ImageView) rowView.findViewById(R.id.imageView);
            viewHolder.ResText= (TextView) rowView.findViewById(R.id.ResText);
            viewHolder.StarText= (TextView) rowView.findViewById(R.id.StarsText);
            viewHolder.StatusText= (TextView) rowView.findViewById(R.id.StatusText);
            viewHolder.DateText = (TextView) rowView.findViewById(R.id.DateTextView);
            viewHolder.LikeText = (TextView) rowView.findViewById(R.id.LikeText);
            viewHolder.Heart= (ImageView) rowView.findViewById(R.id.LikeButton);
            rowView.setTag(viewHolder);
        }
        // Set text to each TextView of ListView item
        else{
            viewHolder = (ViewHolder) rowView.getTag();
        }
        final StatusClass temp = StatusList.get(position);

        if (temp.liked){
            Glide.with(context).load(R.drawable.heart).into(viewHolder.Heart);
        }
        else{
            Glide.with(context).load(R.drawable.heartbreak).into(viewHolder.Heart);
        }
        //String imagestring = getImageForLoad(temp.Image);

    //    bmp.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte[] encodebyte = Base64.decode(temp.Image, Base64.DEFAULT);
        InputStream istream = new ByteArrayInputStream(encodebyte);
        Bitmap bitmap = BitmapFactory.decodeStream(istream);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()*8/10, (Integer)bitmap.getHeight()*8/10,true);
        viewHolder.TheImage.setImageBitmap(bitmap);
        viewHolder.TheImage.setAdjustViewBounds(true);
        viewHolder.TheImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //Glide.with(context).load(bitmap.into).into(viewHolder.TheImage);
        viewHolder.ResText.setText(temp.UserID + " @" + getRestaurantNameFromID(temp.ResCode, context));
        viewHolder.StarText.setText(String.valueOf(temp.Stars));
        viewHolder.StatusText.setText(temp.StatusContent);

        viewHolder.Heart.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SERVER
                StatusList.get(position).liked = !StatusList.get(position).liked;
                if (StatusList.get(position).liked){
                    Glide.with(context).load(R.drawable.heart).into((ImageView) v);
                }
                else{
                    Glide.with(context).load(R.drawable.heartbreak).into((ImageView) v);
                }
            }
        }
            );
        String LikeString = StringForLikes(temp.LikeNumber);
        viewHolder.LikeText.setText(LikeString);
        viewHolder.DateText.setText(temp.Date);


        return rowView;
    }

    String StringForLikes (int num){
        if (num == 0)
            return "아직 아무도 좋아요 하지 않았습니다";
        else
            return num + "명이 좋아합니다";
    }
}
