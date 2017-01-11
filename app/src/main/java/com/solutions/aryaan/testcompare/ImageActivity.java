package com.solutions.aryaan.testcompare;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;


public class ImageActivity extends AppCompatActivity {

    public static final String UPLOAD_URL = "https://proshiksha.000webhostapp.com/android/upload.php";
    public static final String UPLOAD_KEY = "image";
    private ImageView camImageView,galImageView;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        camImageView = (ImageView)findViewById(R.id.imageView3);
        galImageView = (ImageView)findViewById(R.id.imageView4);

        Bundle intent = getIntent().getExtras();
        Bitmap image = (Bitmap) intent.get("IMAGE");
        camImageView.setImageBitmap(image);

        Bundle intent2 = getIntent().getExtras();
        Bitmap galImage = (Bitmap) intent2.get("GALLERYIMAGE");
        galImageView.setImageBitmap(galImage);
    }

    public void processImage(View view){
        if (view.getId() == R.id.button2){
            uploadImage();
        }
        if (view.getId() == R.id.button){

        }

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,100,bs);
        byte[] imageBytes = bs.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandler handler = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ImageActivity.this,"Uploading..",null,true,true);
            }

            @Override
            protected String doInBackground(Bitmap... bitmaps) {
                Bitmap bitmap = bitmaps[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();

                data.put(UPLOAD_KEY,uploadImage);
                String result = handler.sendPostRequest(UPLOAD_URL,data);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(ImageActivity.this, s, Toast.LENGTH_LONG).show();
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }
}
