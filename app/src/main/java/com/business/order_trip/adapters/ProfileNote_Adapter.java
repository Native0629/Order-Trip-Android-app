package com.business.order_trip.adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.business.order_trip.ProfileEditActivity;
import com.business.order_trip.R;
import com.business.order_trip.models.Chat;
import com.business.order_trip.models.MessageModel;
import com.business.order_trip.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfileNote_Adapter extends BaseAdapter {
    ArrayList<Chat> arrayList;
    Context context;
    String imageUri, user_name;

    ProfileNote_Adapter() {
        context = null;
        arrayList = null;
    }
    public ProfileNote_Adapter(Context _context, ArrayList<Chat> _arrayList) {
        context = _context;
        arrayList = _arrayList;
    }

    @Override
    public int getCount() {
        if (arrayList == null)
            return 0;
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Chat chat = arrayList.get(i);
        if (view == null) {
            int resource = R.layout.cell_profile_note;
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
        }
        String receiver_id = chat.getReceiver();
        ImageView ivAvatar = view.findViewById(R.id.profile_image);
        TextView userName = view.findViewById(R.id.user_name);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(receiver_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                imageUri = user.getImageUri();
                user_name = user.getUsername();
                userName.setText(user_name);
                if(!user.getImageUri().equals("null")){
                        Glide.with(getApplicationContext()).load(imageUri).into(ivAvatar);
                }else{
                    ivAvatar.setImageResource(R.drawable.person);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        TextView content = view.findViewById(R.id.content);
        TextView datetime = view.findViewById(R.id.datetime);

        content.setText(chat.getMessage());
        datetime.setText(chat.getDate()+" at "+ chat.getTime());

        return view;
    }
}
