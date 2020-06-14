package com.example.cctedv;

import android.graphics.Bitmap;

public class ImgItem {
    private String mFilename;
    private Bitmap selectedImage;
    public ImgItem() {
        mFilename = "";
    }
    public String getmFilename() {
        return mFilename;
    }
    public void setmFilename(String name) {
        this.mFilename = name;
    }
    public Bitmap getSelectedImage(){return selectedImage;}
    public void setSelectedImage(Bitmap bm) {this.selectedImage = bm;}
}
