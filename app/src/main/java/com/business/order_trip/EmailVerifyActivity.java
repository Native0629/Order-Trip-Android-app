package com.business.order_trip;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EmailVerifyActivity extends AppCompatActivity {
    ImageButton back;
    Button send_email;
    EditText set_email;
    private FirebaseAuth auth;
    private FirebaseUser user;
    DatabaseReference reference;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        back = findViewById(R.id.ib_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(EmailVerifyActivity.this, ProfileSettingActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        email = user.getEmail();
        set_email = findViewById(R.id.email);
        set_email.setText(email);

        send_email = findViewById(R.id.send_email);
        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_email.setEnabled(false);

                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        send_email.setEnabled(true);
                        if (task.isSuccessful()){
                            Toast.makeText(EmailVerifyActivity.this, "success", Toast.LENGTH_LONG).show();
                            try {
                                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                                reference.child("email_status").setValue(1);
                                reference.child("email").setValue(email);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(EmailVerifyActivity.this, "failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                Intent myIntent = new Intent(EmailVerifyActivity.this, ProfileDetailActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
    }
}
