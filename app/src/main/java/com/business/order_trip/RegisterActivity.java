package com.business.order_trip;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.business.order_trip.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    ImageButton back;
    CountryPicker picker;
    TextView btnAlready,  btnRegister;
    private ProgressBar progressBar;
    private EditText inputEmail, inputPassword, inputFirstName, inputLastName, inputPhoneNumber, inputCountry;
    String id, status, username, email, password, first_name, last_name, phone_number, country, date, imagePath, imageUri, downloadUrl, imagePath_storage;
    int phone_status, email_status, shopper_count, trip_count, upcoming_trip_count, pre_shopper_count, pre_trip_count, social_type;

    // image upload from camera
    CircleImageView ivAvatar;
    public final static int  REQUEST_CAMERA_IMAGE = 5;
    Uri uri;
    File file;

    //Firebase
    private FirebaseAuth auth;
    StorageReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //Get Firebase Storage instance
        reference = FirebaseStorage.getInstance().getReference();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputFirstName = (EditText) findViewById(R.id.txt_first_name);
        inputLastName = (EditText) findViewById(R.id.txt_last_name);
        inputPhoneNumber = (EditText) findViewById(R.id.txt_phone_number);
        inputCountry = (EditText) findViewById(R.id.ev_country);


        back = (ImageButton) findViewById(R.id.ib_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnAlready = (TextView) findViewById(R.id.btn_already);
        btnAlready.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent2 = new Intent(RegisterActivity.this, SignInActivity.class);
                startActivity(myIntent2);
                finish();
            }
        });

        // Country address search
        inputCountry.setKeyListener(null);
        inputCountry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                picker = CountryPicker.newInstance("Select Country");
                picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        String selectedItem = name;
                        inputCountry.setText(selectedItem);
                        picker.dismiss();
                    }
                });
            }
        });

        btnRegister = (TextView) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "online";
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();
                first_name = inputFirstName.getText().toString().trim();
                last_name = inputLastName.getText().toString().trim();
                username = first_name + " " + last_name;
                phone_number = inputPhoneNumber.getText().toString().trim();
                country = inputCountry.getText().toString().trim();
                // Generate the date
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
                date = sdf.format(cal.getTime());
                phone_status = 0;
                email_status = 0;
                shopper_count = 0;
                trip_count = 0;
                upcoming_trip_count = 0;
                pre_shopper_count = 0;
                pre_trip_count = 0;
                imageUri = downloadUrl;

                if(uri != null){
                    imagePath = imagePath_storage;
                    imageUri = downloadUrl;
                }
                else{
                    imagePath = "null";
                    imageUri = "null";
                }
                createUser(email, password);
            }
        });

// ---------------------------------- Image Upload from camera  ---------------------------------------------------
        ivAvatar = findViewById(R.id.iv_avatar);
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPictureFromCamera();
            }
        });

        checkForPermission();
    }

    public void createUser(final String email, final String password) {
        // field required
        if(first_name.isEmpty()){
            inputFirstName.setError( getString(R.string.fill_message));
            inputFirstName.requestFocus();
            return;
        }
        if(last_name.isEmpty()){
            inputLastName.setError( getString(R.string.fill_message));
            inputLastName.requestFocus();
            return;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError(getString(R.string.invalid_email_message));
            inputEmail.requestFocus();
            return;
        }
        if(password.length()<8){
            inputPassword.setError(getString(R.string.password_min_length_message));
            inputPassword.requestFocus();
            return;
        }

        if(phone_number.length()<6 || phone_number.length()>13){
            inputPhoneNumber.setError(getString(R.string.number_min_length_message));
            inputPhoneNumber.requestFocus();
            return;
        }
        if(country.isEmpty()){
            inputCountry.setError(getString(R.string.fill_message));
            inputCountry.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//                Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                if (!task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    id = firebaseUser.getUid();
                    User user = new User(id, status, username, first_name, last_name, email, password, country, phone_number, imageUri, date, imagePath,
                            phone_status, email_status, shopper_count, trip_count, pre_shopper_count, pre_trip_count, upcoming_trip_count, social_type);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent myIntent = new Intent(RegisterActivity.this, SignInActivity.class);
                                startActivity(myIntent);
                                finish();
                            }else {
                                Toast.makeText(RegisterActivity.this,  "failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
// ---------------------------------- Image Upload from camera  ---------------------------------------------------
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
        //File object of camera image
        File file = new File(Environment.getExternalStorageDirectory(), UUID.randomUUID().toString()+".jpg");
        //Uri of camera image
        uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
        m_intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(m_intent, REQUEST_CAMERA_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQUEST_CAMERA_IMAGE:
                if (resultCode == RESULT_OK) {
                    Glide.with(RegisterActivity.this).load(uri).into(ivAvatar);
//                    Picasso.get().load(uri).fit().centerInside().transform(new CircleTransform()).rotate(90).into(ivAvatar);
                    uploadImage();
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
            StorageReference ref = reference.child(imagePath_storage);
            ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri1) {
                                downloadUrl = uri1.toString();
                                progressDialog.dismiss();
                            }
                        });
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });
        }
    }

    public void onBackPressed() {        // to prevent irritating accidental logouts
        Intent intent = new Intent(RegisterActivity.this, SplashActivity.class);
        startActivity(intent);
        finish();
    }
}
