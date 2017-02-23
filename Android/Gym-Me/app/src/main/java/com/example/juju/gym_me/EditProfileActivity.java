package com.example.juju.gym_me;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class EditProfileActivity extends Activity {
    private static int RESULT_LOAD_IMAGE = 1;
    String imgDecodableString;
    String email;
    String password;
    ProfileInfo user;
    EditText name;
    EditText phone;
    EditText address;
    EditText description;
    EditText tags;
    String encodedphoto;
    Server s = new Server();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        name = (EditText) findViewById(R.id.edit_profile_name);
        phone = (EditText) findViewById(R.id.edit_profile_phone);
        address = (EditText) findViewById(R.id.edit_profile_address);
        description = (EditText) findViewById(R.id.edit_profile_bio);
        tags = (EditText) findViewById(R.id.edit_profile_tags);
        try {
            user = new ProfileInfo(email, password);
            name.setText(user.name);
            if(!user.phone.equals("")) {
                phone.setText(user.phone);
            }
            if(!user.address.equals("")) {
                address.setText(user.address);
            }
            if(!user.description.equals("")) {
                description.setText(user.description);
            }
            if(!user.tags.equals("")) {
                tags.setText(user.tags);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void camera(View view) {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(i == null)
            Log.d("Tag1","null");
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public void done(View view) {

        //updateprofile: 2-name, 3-email, 4-password, 5-phone, 6-address, 7-description, 8-tags, 9-image
        s.execute("updateprofile",name.getText().toString(), email, password, phone.getText().toString(),
                address.getText().toString(), description.getText().toString(), tags.getText().toString());

        //This line below doesnt work
        //s.execute("updateprofilepicture", email, password, encodedphoto);


        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null)
            Log.d("Tag","null");
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }
            }
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.edit_profile_imageee);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            // TODO: Image to send
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                encodedphoto = Base64.encodeToString(byteArray, Base64.DEFAULT);

            }
            catch (IOException e){

            }

        }
    }
}