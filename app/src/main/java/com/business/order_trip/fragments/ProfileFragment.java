package com.business.order_trip.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.business.order_trip.ContactUsActivity;
import com.business.order_trip.ProfileNoteActivity;
import com.business.order_trip.ProfileSettingActivity;
import com.business.order_trip.ProfileDetailActivity;
import com.business.order_trip.R;
import com.business.order_trip.helpers.fullscreenActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfileFragment extends Fragment {
    RelativeLayout button, button1, button2, button3, button4;
    View view;
    CircleImageView ivAvatar;
    String username, imageUri;
    TextView userName;
    int social_type;

    private FirebaseAuth auth;
    private FirebaseUser user;
    DatabaseReference reference;
    StorageReference storageReference;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        userName = view.findViewById(R.id.user_name);

        //Get Firebase Storage instance
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                username = dataSnapshot.child("username").getValue().toString();
                userName.setText(username);
                imageUri = dataSnapshot.child("imageUri").getValue().toString();
                social_type = Integer.parseInt(dataSnapshot.child("social_type").getValue().toString());

                if(!imageUri.equals("null")){
                    Glide.with(getApplicationContext()).load(imageUri).into(ivAvatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity().getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(!imageUri.equals("null")){
                    Intent inf = new Intent(getActivity(), fullscreenActivity.class);
                    inf.putExtra("image_uri", imageUri);
                    startActivity(inf);
                }
            }
        });

        button = (RelativeLayout) view.findViewById(R.id.rl_profile_more);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), ProfileDetailActivity.class);
                getActivity().startActivity(intent);
            }
        });

        button1 = (RelativeLayout) view.findViewById(R.id.rl_profile_note);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), ProfileNoteActivity.class);
                getActivity().startActivity(intent);
            }
        });

        button2 = (RelativeLayout) view.findViewById(R.id.rl_profile_setting);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), ProfileSettingActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

        button3 = (RelativeLayout) view.findViewById(R.id.rl_contacts);
        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), ContactUsActivity.class);
                getActivity().startActivity(intent);
            }
        });
//
//        RelativeLayout button4 = (RelativeLayout) view.findViewById(R.id.rl_profile_press);
//        button4.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                Intent intent = new Intent(getActivity(), ProfilePressActivity.class);
//                getActivity().startActivity(intent);
//            }
//        });
        return view;
    }

}
