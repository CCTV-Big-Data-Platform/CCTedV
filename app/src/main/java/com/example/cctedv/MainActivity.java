package com.example.cctedv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION = 1;
    private static final int REQ_RECORDING_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        grantPermissions();
        Button   mButton;
        final EditText mEdit;

        Button fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MainActivity","화면 전환");
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });

        mEdit   = (EditText)findViewById(R.id.userId);
        mButton = (Button)findViewById(R.id.enroll_user);
        mButton.setOnClickListener(
            new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    Singleton.getInstance().setUserId(mEdit.getText().toString());
                    String url = "http://victoria.khunet.net:5900/user";
                    final AsyncTask<Void, Void, String> execute = new NetworkTask(url, Singleton.getInstance().getUserId()).execute();

                    Log.v("UserId", Singleton.getInstance().getUserId());
                }
            });

    }

    public boolean grantPermissions() {
        ArrayList<String> permissions_array = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions_array.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissions_array.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissions_array.add(Manifest.permission.RECORD_AUDIO);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions_array.add(Manifest.permission.CAMERA);
        }
        if(!permissions_array.isEmpty()) {
            String[] permissions = new String[permissions_array.size()];
            permissions_array.toArray(permissions);
            //Callback으로 onRequestPermissionsResult 함수가 실행됨
            ActivityCompat.requestPermissions(this, permissions, REQ_RECORDING_PERMISSION);
            ActivityCompat.requestPermissions(this, permissions, CAMERA_PERMISSION);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(" ","Permission: " + permissions[0] + "was " + grantResults[0]);
            if (requestCode == REQ_RECORDING_PERMISSION) {
                for (int i = 0; i < grantResults.length ; i++) {
                    if (grantResults[i] < 0) {
                        Toast.makeText(MainActivity.this, "해당 권한을 활성화하셔야 합니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Log.v("info : ","mic permission");
            } else if (requestCode == CAMERA_PERMISSION) {
                for (int i = 0; i < grantResults.length ; i++) {
                    if (grantResults[i] < 0) {
                        Toast.makeText(MainActivity.this, "해당 권한을 활성화하셔야 합니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Log.v("info : ","mic permission");
            }
        }
    }

}

