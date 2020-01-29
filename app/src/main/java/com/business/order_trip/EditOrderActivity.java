package com.business.order_trip;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.business.order_trip.helpers.AdressFromActivity;
import com.business.order_trip.helpers.AdressToActivity;
import com.business.order_trip.helpers.fullscreenActivity;
import com.business.order_trip.models.OrderModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class EditOrderActivity extends AppCompatActivity {
    ImageButton back;
    TextView productName, endDate, Price, mOthers, From, To, mCategory, mWeight, mCount, mStatus, showValue;
    ImageView productImage;

    String order_id, id, weight, category, country, city, destination, end_date, product_name, details, image_uri, price, quantity,
            trip_id, from, to, timestamp, imagePath, imagePath_storage;
    protected static int counter = 0;
    static int ORDER_SEARCH_CATEGORY = 901;
    int social_type;

    // upload image from library of device
    Uri uri;
    LinearLayout ic_camera;
    private final int PICK_IMAGE_REQUEST = 71;
    public final static int  REQUEST_CAMERA_IMAGE = 5;

    // firebase
    DatabaseReference reference;
    StorageReference storageReference;

    //Calender
    TextView editDate;
    DatePickerDialog.OnDateSetListener datePickerDialog;
    Switch dateSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        //Get Firebase Storage instance
        storageReference = FirebaseStorage.getInstance().getReference();

        back = (ImageButton) findViewById(R.id.ib_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EditOrderActivity.this, AwaitingOffersActivity.class);
                myIntent.putExtra("order_id", order_id);
                startActivity(myIntent);
                finish();
            }
        });

        Intent intent = getIntent();
        order_id = intent.getStringExtra("order_id");

        productImage = findViewById(R.id.picture);
        productName = findViewById(R.id.product_name);
        From = findViewById(R.id.from);
        To = findViewById(R.id.to);
        endDate = findViewById(R.id.edit_date);
        mCategory = findViewById(R.id.category);
        mWeight= findViewById(R.id.weight);
        Price = findViewById(R.id.price);
        mOthers = findViewById(R.id.others);
        mCount =  findViewById(R.id.count);

        reference = FirebaseDatabase.getInstance().getReference().child("Orders").child(order_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrderModel order1 = dataSnapshot.getValue(OrderModel.class);
//                destination = order1.getTo();

                image_uri = order1.getImage_url();
                imagePath = order1.getImagePath();
                product_name = order1.getProduct_name();
                category = order1.getCategory();
                weight = order1.getWeight();
                from = order1.getFrom();
                to = order1.getTo();
                end_date = order1.getEnd_date();
                price = order1.getPrice();
                quantity = order1.getQuantity();
                social_type = order1.getSocial_type();
                details = order1.getDetails();
                if (!image_uri.equals("null")){
                    Glide.with(EditOrderActivity.this).load(image_uri).into(productImage);
                }
                productName.setText(product_name);
                mCategory.setText(category);
                if(!weight.equals("")) mWeight.setText(weight + "kg");
                From.setText(from);
                To.setText(to);
                endDate.setText(end_date);
                Price.setText(price);
                mCount.setText(quantity);
                mOthers.setText(details);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        productImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(!image_uri.equals("null")){
                    Intent inf=new Intent(EditOrderActivity.this, fullscreenActivity.class);
                    inf.putExtra("image_uri", image_uri);
                    startActivity(inf);
                }
            }
        });

        if(social_type == 1) ic_camera.setEnabled(false);
        // conunter
        showValue =  (TextView) findViewById(R.id.count);
        String s1 =  showValue.getText().toString();
        counter = new Integer(Integer.parseInt(s1));

        // Calendar for date
        editDate = findViewById(R.id.edit_date);
        final Calendar myCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMMM dd,yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                editDate.setText(sdf.format(myCalendar.getTime()));
            }
        };
        dateSwitch= findViewById(R.id.switch1);
        dateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    new DatePickerDialog(EditOrderActivity.this, datePickerDialog, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }else{
                    editDate.setText("");
                }

            }
        });

        Button mSave = (Button) findViewById(R.id.btn_save_change);
        mSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                try {
                    product_name = productName.getText().toString().trim();
                    from = From.getText().toString().trim();
                    to = To.getText().toString().trim();
                    end_date = endDate.getText().toString().trim();
                    category = mCategory.getText().toString().trim();
                    weight = mWeight.getText().toString().trim();
                    price = Price.getText().toString().trim();
                    details = mOthers.getText().toString().trim();
                    quantity = mCount.getText().toString().trim();

                    if(image_uri == null){image_uri = "null";}
                    reference.child("product_name").setValue(product_name);
                    reference.child("details").setValue(details);
                    reference.child("price").setValue(price);
                    reference.child("quantity").setValue(quantity);
                    reference.child("image_url").setValue(image_uri);

                    reference.child("from").setValue(from);
                    reference.child("to").setValue(to);
                    reference.child("end_date").setValue(end_date);
                    reference.child("category").setValue(category);
                    reference.child("weight").setValue(weight);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent myIntent = new Intent(EditOrderActivity.this, AwaitingOffersActivity.class);
                myIntent.putExtra("order_id", order_id);
                startActivity(myIntent);
                finish();
            }
        });

        Button mDelete = (Button) findViewById(R.id.btn_delete_order);
        mDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                reference.child("status").setValue("deleted");

                Intent myIntent = new Intent(EditOrderActivity.this, MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        ic_camera = findViewById(R.id.ll_camera);
        ic_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(social_type != 0) {
                    ic_camera.setEnabled(false);
                    Toast.makeText(EditOrderActivity.this, "You can't change the your avatar!", Toast.LENGTH_LONG).show();
                }else{
                    checkForPermission();
                    alert_open();
                }
            }
        });
    }

    public void countIn(View v){
        counter++;
        showValue.setText(Integer.toString(counter));
    }

    public void countDe(View v){
        counter--;
        if(counter <= 0){
            counter =0;
        }
        showValue.setText(Integer.toString(counter));
    }

    // ---------------------------------- Image Upload from camera  ---------------------------------------------------
    public void alert_open(){
        productImage.setVisibility(View.VISIBLE);

        final Dialog dialog = new Dialog(EditOrderActivity.this);
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
                            reference.child("image_url").setValue("null");
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
                            reference.child("image_url").setValue("null");
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
                                reference.child("image_url").setValue("null");
                                reference.child("imagePath").setValue("null");
                                productImage.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Uh-oh, an error occurred!
                            Toast.makeText(EditOrderActivity.this, "Failed deleting the image.", Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    Toast.makeText(EditOrderActivity.this, "The image is null", Toast.LENGTH_LONG).show();
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
                    Glide.with(EditOrderActivity.this).load(uri).into(productImage);
                    uploadImage();
                }
                break;
            case PICK_IMAGE_REQUEST:
                if (resultCode == RESULT_OK && data != null && data.getData() != null ) {

                    uri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        productImage.setImageBitmap(bitmap);
                        uploadImage();
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

            imagePath_storage = "product_images/"+ UUID.randomUUID().toString();
            StorageReference ref = storageReference.child(imagePath_storage);
            ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri1) {
                            image_uri = uri1.toString();
                            reference.child("image_url").setValue(image_uri);
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
                    Toast.makeText(EditOrderActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
