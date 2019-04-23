package com.example.ims2019;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    EditText updateUserName, updateUserStatus;
    Button updateUserProfile, cancelProfileButton;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        InitializeFields();

        updateUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                updateProfile();
            }
        });

        cancelProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SendUserToMainActivity();
            }
        });

        RetrieveUserInfo();
    }



    private void InitializeFields()
    {
        updateUserName = findViewById(R.id.update_username);
        updateUserStatus = findViewById(R.id.update_status);
        updateUserProfile = findViewById(R.id.update_profile_btn);
        cancelProfileButton = findViewById(R.id.cancel_profile_btn);
    }

    private void updateProfile()
    {
        String setUserName = updateUserName.getText().toString();
        String setUserStatus = updateUserStatus.getText().toString();

        if (TextUtils.isEmpty(setUserName)){
            Toast.makeText(ProfileActivity.this,"Please write your name!", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(setUserStatus)){
            Toast.makeText(ProfileActivity.this,"Please write your information!", Toast.LENGTH_SHORT).show();
        }

        else
        {
            HashMap<String, String> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserID);
            profileMap.put("name", setUserName);
            profileMap.put("status", setUserStatus);

            rootRef.child("Users").child(currentUserID).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                SendUserToMainActivity();
                                Toast.makeText(ProfileActivity.this, "Profile updated successfully...", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String message = task.getException().toString();
                                Toast.makeText(ProfileActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }

    }

    private void RetrieveUserInfo()
    {
        rootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists() && (dataSnapshot.hasChild("name")) && (dataSnapshot.hasChild("status"))))
                {
                    String retrieveUserName = dataSnapshot.child("name").getValue().toString();
                    String retrieveUserStatus = dataSnapshot.child("status").getValue().toString();

                    updateUserName.setText(retrieveUserName);
                    updateUserStatus.setText(retrieveUserStatus);

                }

                else
                {
                    Toast.makeText(ProfileActivity.this, "Please update your profile information!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(ProfileActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }
}
