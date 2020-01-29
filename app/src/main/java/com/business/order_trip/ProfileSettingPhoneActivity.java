package com.business.order_trip;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.business.order_trip.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class ProfileSettingPhoneActivity extends AppCompatActivity {
    CountryCodePicker codePicker;
    EditText e1, e2;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    String verification_code, phone_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting_phone);
        codePicker = findViewById(R.id.ccp);

        e1 = findViewById(R.id.editText_carrierNumber);
        e2 = findViewById(R.id.sms_code);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verification_code = s;
                Toast.makeText(getApplicationContext(), verification_code, Toast.LENGTH_LONG).show(); }
        };

        ImageButton ibback = (ImageButton) findViewById(R.id.ib_back);
        ibback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        Button button2 = (Button) findViewById(R.id.btn_verify_call);
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent myIntent = new Intent(ProfileSettingPhoneActivity.this, ProfileSettingActivity.class);
//                startActivity(myIntent);
//                finish();
//            }
//        });
    }

    public void send_sms(View v){
        codePicker.registerCarrierNumberEditText(e1);
        phone_number = codePicker.getFullNumberWithPlus();
        Toast.makeText(getApplicationContext(), phone_number, Toast.LENGTH_LONG).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone_number, 60, TimeUnit.SECONDS, ProfileSettingPhoneActivity.this, mCallback
        );
    }



    public void verify(View v){
        String input_code = e2.getText().toString();
        if(!verification_code.equals(""))
            verifyPhoneNumber(verification_code, input_code);
    }

    public void verifyPhoneNumber(String verifyCode, String input_code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifyCode, input_code);
        Toast.makeText(getApplicationContext(), input_code, Toast.LENGTH_LONG).show();
        signInWithPhone(credential);
    }

    public void  signInWithPhone(PhoneAuthCredential credential){
//        auth.signInWithCredential(credential)
        user.linkWithCredential(credential)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "User signed in successfully", Toast.LENGTH_LONG).show();
//
                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                        try {
                            reference.child("phone_number").setValue(phone_number);
                            reference.child("phone_status").setValue(1);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_LONG).show();

                    }
                }
            });
    }
}
