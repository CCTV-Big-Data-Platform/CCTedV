package com.example.cctedv;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SetUserImgActivity  extends AppCompatActivity {
    private ListView mListView;
    private ImgListAdapter mAdapter;
    private ArrayList<ImgItem> mImgList = null;
    private Button mImgAddButton;
    private Button uploadButton;
    private static int RESULT_LOAD_IMG = 1;


    InputStream imageStream;
    EditText mItemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);

        mListView = findViewById(R.id.list_view);
        mImgAddButton = (Button)findViewById(R.id.img_upload);
        mImgList = new ArrayList<>();
        ImgItem t = new ImgItem();
//        t.setmFilename("");
        mImgList.add(t);

        mListView.setAdapter(mAdapter);

        mImgAddButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                openBuilder();
            }
        });

    }
//    @Override
//    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
//        super.onActivityResult(reqCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            try {
//                final Uri imageUri = data.getData();
//                imageStream = getContentResolver().openInputStream(imageUri);
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
////                Toast.makeText(PostImage.this, "Something went wrong", Toast.LENGTH_LONG).show();
//            }
//
//        }else {
////            Toast.makeText(PostImage.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
//        }
//    }

    public void openBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_img_upload, null);

//        AlertDialog alert = builder.create();
//        mItemName   = (EditText) view.findViewById(R.id.filename);

//        uploadButton = (Button) view.findViewById(R.id.select_img);
//        uploadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("??","!");
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
//            }
//        });

        builder.setView(inflater.inflate(R.layout.dialog_img_upload, null))
                // Add action buttons
                .setNegativeButton("UPLOAD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                        photoPickerIntent.setType("image/*");
//                        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, RESULT_LOAD_IMG);
                    }
                });


//        builder.setView(inflater.inflate(R.layout.dialog_img_upload, null))
//                // Add action buttons
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        ImgItem t = new ImgItem();
////                        t.setmFilename(mItemName.getText().toString());
////                        Log.i("name : ", mItemName.getText().toString());
//                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                        t.setSelectedImage(selectedImage);
//                        Log.i("?", selectedImage.toString());
//                        mImgList.add(t);
//                        mAdapter = new ImgListAdapter(mImgList);
//
//                        mListView.setAdapter(mAdapter);
//                    }
//                });


        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImgItem t = new ImgItem();
//          t.setmFilename(mItemName.getText().toString());
//          Log.i("name : ", mItemName.getText().toString());
            Bitmap selectedImg = BitmapFactory.decodeFile(picturePath);
            t.setSelectedImage(selectedImg);
            mImgList.add(t);
            mAdapter = new ImgListAdapter(mImgList);

            mListView.setAdapter(mAdapter);

//            ImageView imageView = (ImageView) findViewById(R.id.imgView);
//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter = new ImgListAdapter(mImgList);
        Log.d("size", String.valueOf(mImgList.size()));
        mListView.setAdapter(mAdapter);
    }
}
