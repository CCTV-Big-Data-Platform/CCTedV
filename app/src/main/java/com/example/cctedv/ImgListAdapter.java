package com.example.cctedv;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ImgListAdapter extends BaseAdapter {
    private ArrayList<ImgItem> mImgList;
    private LayoutInflater mInflater;

    public ImgListAdapter(ArrayList<ImgItem> mImgList) {
        this.mImgList = mImgList;
    }
    @Override
    public int getCount() {
        return mImgList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();

        if (convertView == null) {
            if (mInflater == null) {
                mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView=LayoutInflater.from(context).inflate(R.layout.listview_img,null);
        }

        ImageView imgView = convertView.findViewById(R.id.img_source);
        TextView fileName = convertView.findViewById(R.id.list_file_name);

        ImgItem file = mImgList.get(position);
        Log.i("ITEM : ", file.getmFilename());
        fileName.setText(file.getmFilename());
        imgView.setImageBitmap(file.getSelectedImage());

        convertView.setTag("" + position);
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mImgList.get(position);
    }


}
