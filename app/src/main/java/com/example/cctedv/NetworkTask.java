package com.example.cctedv;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.mimecraft.Multipart;
import com.squareup.mimecraft.Part;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkTask extends AsyncTask<Void, Void, String> {

    private String url;
    private ContentValues values;
    private File mFiles;

    public NetworkTask(String url, ContentValues values, File mFiles) {

        this.url = url;
        this.values = values;
        this.mFiles = mFiles;
    }

    @Override
    protected String doInBackground(Void... params) {
        String result; // 요청 결과를 저장할 변수.

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", mFiles.getName(),
                        RequestBody.create(MediaType.parse("text/plain"), mFiles))
                .addFormDataPart("userName", "victoria")
                .build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s != null)
            Log.i("RESPONSE : ", s);
        //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
//            tv_outPut.setText(s);
    }

    @Override
    protected void onPreExecute() {

    }
}
