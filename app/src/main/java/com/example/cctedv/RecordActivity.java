package com.example.cctedv;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.widget.FrameLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;

import androidx.annotation.RequiresApi;

public class RecordActivity extends Activity implements TextureView.SurfaceTextureListener {
    private Camera mCamera;
    private TextureView mTextureView;

    private String mOutputFile; // 파일
    private DateFormat mDateFormat;
    private String mDate;
    private String mUserName = "victoria";
    public File directory;
    private FileOutputStream mFileOutputStream;
    private File mFiles;
    private boolean isCameraOpen = false;

    private int mUnitTime = 1000;
    private int mRemainingFileSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        mTextureView = new TextureView(this);
        mTextureView.setSurfaceTextureListener(this);


        setContentView(mTextureView);
    }

    public void settingVideoInfo() {
        TimeZone mTimeZone = TimeZone.getDefault();
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        mDateFormat.setTimeZone(mTimeZone);
        mDate = mDateFormat.format(new Date());
        mOutputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CCTedV" + "/" + mUserName + "_" + mDate;
        Log.i("PATH :: ", mOutputFile);
        mFiles = new File(mOutputFile);

        mRemainingFileSize = calculateGap(mDate)*44100*2;
        try {
            mFileOutputStream = new FileOutputStream(mOutputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void makeDir(){
        directory = new File(Environment.getExternalStorageDirectory() + File.separator + "CCTedV");
        boolean success = true;
        if (!directory.exists()) {
            success = directory.mkdirs();
        }
        if (success) {
            Log.v("FILE", "Directory is exist");
        } else {
            Log.e("FILE", "Directory not created");
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.i("hello", "aaa");
        makeDir();
        settingVideoInfo();

        isCameraOpen = true;
        mCamera = Camera.open();

        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        mTextureView.setLayoutParams(new FrameLayout.LayoutParams(
                previewSize.width, previewSize.height, Gravity.CENTER));

        try {
            mCamera.setPreviewTexture(surface);
        } catch (IOException t) {
        }

        mCamera.startPreview();

        mCamera.setPreviewCallback(new Camera.PreviewCallback() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onPreviewFrame(final byte[] data, final Camera camera) {
                if(isCameraOpen) {
                    if(!accumulateFile(data)) {
                        if (mFiles.exists()) {
                            try {
                                //파일 저장
                                mFileOutputStream.close();
                                mFileOutputStream.flush();
                                mFileOutputStream = null;
                                // URL 설정.
                                String url = "http://victoria.khunet.net:5900/upload";

                                mDate = mDateFormat.format(new Date());
                                File photo=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/CCTedV" + "/" + "img" + "_" + mDate+".jpeg");
                                FileOutputStream fos = new FileOutputStream(photo);
                                Bitmap bmp = mTextureView.getBitmap();
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                byte[] currentData = stream.toByteArray();

                                fos = new FileOutputStream(photo);
                                fos.write(currentData);
                                fos.flush();
                                fos.close();

//                                String byteData = new String(currentData);
                                String s = Base64.getEncoder().encodeToString(currentData);
//                                Log.i("this : ", byteData);

                                // AsyncTask를 통해 HttpURLConnection 수행.
                                (new NetworkTask(url, s, mFiles, mDate)).execute();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //다시 시작
                            settingVideoInfo();
                        }
                    }
                }
                // Process the contents of byte for whatever you need
            }
        });
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        isCameraOpen = false;
        try {
            mFileOutputStream.close();
            mFileOutputStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.stopPreview();
        mCamera.release();
        return true;
    }

    private boolean accumulateFile(byte[] byteBuffer) {
        try {
            if(mRemainingFileSize >0) {
                if(byteBuffer != null) {
                    mFileOutputStream.write(byteBuffer);

                    mRemainingFileSize -= byteBuffer.length * 2;
                    return true;
                } else {
//                    Toast.makeText(RecorderService.this, "Playing...", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                mFileOutputStream.close();
            }
        }catch (IOException e){
            Log.e("file out" , e.toString());
        }
        return false;
    }

    private int calculateGap(String date) {
        String time = date.substring(11, date.length());
        final int hour = Integer.parseInt(time.substring(0, 2));
        final int min = Integer.parseInt(time.substring(3, 5));
        final int sec = Integer.parseInt(time.substring(6, time.length()));
        final int timeInSecond = hour * 3600 + min * 60 + sec;
        final int gap = mUnitTime - (timeInSecond % mUnitTime);
        return gap;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
