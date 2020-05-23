package com.example.cctedv;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkTask extends AsyncTask<Void, Void, String> {

    private String url;
    private String data;
    private File mFiles;
    private String mDate;


    public NetworkTask(String url, String data, File mFiles, String mDate) {
        this.url = url;
        this.data = data;
        this.mFiles = mFiles;
        this.mDate = mDate;
    }

    @Override
    protected String doInBackground(Void... params) {
        String result; // 요청 결과를 저장할 변수.
        // file 전송시
//        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
////                .addFormDataPart("file", mFiles.getName(),
////                        RequestBody.create(MediaType.parse("text/plain"), mFiles))
////                .addFormDataPart("userName", "victoria")
////                .build();

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("befEncoding", this.data)
                .addFormDataPart("userId", Singleton.getInstance().getUserId())
                .addFormDataPart("timeStamp", this.mDate)
                .build();


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null)
            Log.i("RES",  response.toString());
        return "hello";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s != null)
            Log.i("RESPONSE : ", s);
    }

    @Override
    protected void onPreExecute() {

    }
}
