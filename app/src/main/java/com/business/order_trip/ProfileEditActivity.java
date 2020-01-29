package com.business.order_trip;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.business.order_trip.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditActivity extends AppCompatActivity {
    EditText et_lastName, et_firstName;
    String imageUri, imagePath, downloadUrl, imagePath_storage;
    CircleImageView ivAvatar;
    int  social_type;

    private FirebaseAuth auth;
    private FirebaseUser user;
    DatabaseReference reference;
    StorageReference storageReference;

    // upload image from library of device
    Uri uri;
    private final int PICK_IMAGE_REQUEST = 71;
    public final static int  REQUEST_CAMERA_IMAGE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        ivAvatar = findViewById(R.id.iv_avatar);
        et_lastName = (EditText) findViewById(R.id.txt_last_name);
        et_firstName = (EditText) findViewById(R.id.txt_first_name);

        //Get Firebase Storage instance
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);

                et_firstName.setText(user1.getFirst_name());
                et_lastName.setText(user1.getLast_name());
                imageUri = user1.getImageUri();
                imagePath = user1.getImagePath();
                social_type = user1.getSocial_type();


                if(!user1.getImageUri().equals("null")){
                    if (!ProfileEditActivity.this.isFinishing()) {
                        Glide.with(ProfileEditActivity.this).load(imageUri).into(ivAvatar);
                    }
                }else{
                    ivAvatar.setImageResource(R.drawable.person);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        TextView btn_save = (TextView) findViewById(R.id.btn_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_last_name = et_lastName.getText().toString().trim();
                String new_first_name = et_firstName.getText().toString().trim();
                String username = new_first_name + " " + new_last_name;

                Intent intent = new Intent(ProfileEditActivity.this, ProfileDetailActivity.class);
                startActivity(intent);
                finish();
                try {
                    reference.child("first_name").setValue(new_first_name);
                    reference.child("last_name").setValue(new_last_name);
                    reference.child("username").setValue(username);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
// ---------------------------------- Image Upload from camera  ---------------------------------------------------

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(social_type == 1) {
                    ivAvatar.setEnabled(false);
                    Toast.makeText(ProfileEditActivity.this, "You can't change the your avatar!", Toast.LENGTH_LONG).show();
                }else{
                    alert_open();
                }
            }
        });

        checkForPermission();

        ImageButton back = (ImageButton) findViewById(R.id.ib_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileEditActivity.this, ProfileDetailActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    // ---------------------------------- Image Upload from camera  ---------------------------------------------------
    public void alert_open(){

        final Dialog dialog = new Dialog(ProfileEditActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_profile_photo);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        LinearLayout from_camera = dialog.findViewById(R.id.ll_camera);
        from_camera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
                if(!imagePath.equals("null")){
                    // Create a reference to the file to delete
                    StorageReference desertRef = storageReference.child(imagePath);
                    desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            reference.child("imageUri").setValue("null");
                            reference.child("imagePath").setValue("null");
                            getPictureFromCamera();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Uh-oh, an error occurred!
                        }
                    });
                }else{
                    getPictureFromCamera();
                }
            }
        });

        LinearLayout from_library = dialog.findViewById(R.id.ll_library);
        from_library.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
                if(!imagePath.equals("null")){
                    // Create a reference to the file to delete
                    StorageReference desertRef = storageReference.child(imagePath);
                    desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            reference.child("imageUri").setValue("null");
                            reference.child("imagePath").setValue("null");
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Uh-oh, an error occurred!
                        }
                    });

                }else{
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                }
            }
        });

        LinearLayout image_remove = dialog.findViewById(R.id.ll_remove);
        image_remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
                if(!imagePath.equals("null")){
                    // Create a reference to the file to delete
                    StorageReference desertRef = storageReference.child(imagePath);
                    desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            try {
                                reference.child("imageUri").setValue("null");
                                reference.child("imagePath").setValue("null");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Uh-oh, an error occurred!
                            Toast.makeText(ProfileEditActivity.this, "Failed deleting the image.", Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    Toast.makeText(ProfileEditActivity.this, "The image is null", Toast.LENGTH_LONG).show();
                }
            }
        });

        LinearLayout dialog_btn3 = dialog.findViewById(R.id.ll_cancel);
        dialog_btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dialog.dismiss();
            }
        });

        dialog.show();

        Rect displayRectangle = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() *1f), dialog.getWindow().getAttributes().height);
    }

    private void checkForPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 123);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 123) {

        }
    }

    private void getPictureFromCamera()
    {
        Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(),  UUID.randomUUID().toString()+".jpg");
        uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
        m_intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);// ????
        startActivityForResult(m_intent, REQUEST_CAMERA_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQUEST_CAMERA_IMAGE:
                if (resultCode == RESULT_OK) {

                    Glide.with(ProfileEditActivity.this).load(uri).into(ivAvatar);
                    uploadImage();
                }
                break;
            case PICK_IMAGE_REQUEST:
                if (resultCode == RESULT_OK && data != null && data.getData() != null ) {

                    uri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        ivAvatar.setImageBitmap(bitmap);
                        uploadImage();
//                        Glide.with(ProfileEditActivity.this).load(uri).into(ivAvatar);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void uploadImage() {

        if(uri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            imagePath_storage = "user_images/"+ UUID.randomUUID().toString();
            StorageReference ref = storageReference.child(imagePath_storage);
            ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri1) {
                            downloadUrl = uri1.toString();
                            reference.child("imageUri").setValue(downloadUrl);
                            reference.child("imagePath").setValue(imagePath_storage);
                            progressDialog.dismiss();
                        }
                    });
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileEditActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });
        }
    }
}
