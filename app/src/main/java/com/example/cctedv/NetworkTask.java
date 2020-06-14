package com.example.cctedv;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkTask extends AsyncTask<Void, Void, String> {
    /*
     * 이 NetworkTask Class를 기반으로 http 통신을 이용하여 api를 호출 할 수 있습니다.
     * 이 NetworkTask Class는 사용자 프레임데이터를 송신하거나, 사용자 아이디를 등록할 때 사용됩니다.
     * */
    private String url;
    private String data;
    private File mFiles = null;
    private String mDate = null;

    public NetworkTask(String url, String data) {
        this.url = url;
        this.data = data;
    }
    public NetworkTask(String url, String data, File mFiles, String mDate) {
        this.url = url;
        this.data = data;
        this.mFiles = mFiles;
        this.mDate = mDate;
    }

    @Override
    protected String doInBackground(Void... params) {

        if(mFiles == null) {
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("userId", data)
                    .addFormDataPart("userToken", "")
                    .build();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response != null)
                Log.i("RES", response.toString());
        } else {
            Log.i("DATA SIZE ", String.valueOf(this.data.length()));
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
            if (response != null)
                Log.i("RES", response.toString());
        }
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
