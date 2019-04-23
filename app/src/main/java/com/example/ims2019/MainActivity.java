 package com.example.ims2019;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

 public class MainActivity extends AppCompatActivity {

    private Toolbar main_page_toolbar;

    private ViewPager myViewPager;

    private TabLayout myTabLayout;

    private TabsAccessorAdapter myTabsAccessorAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
     private DatabaseReference rootRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (mAuth.getCurrentUser() == null){
            SendUserToLoginActivity();
            return;
        }
        rootRef = FirebaseDatabase.getInstance().getReference();
        final ProgressDialog dialog = ProgressDialog.show(this, "Login Current User",
                "Loading. Please wait...", true);
        dialog.show();
        rootRef.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               String value = dataSnapshot.child("role").getValue().toString();
               if(value.equals("simple")){
                   Intent intent = new Intent(MainActivity.this, ForSimpleUser.class);
                   startActivity(intent);
                   dialog.dismiss();
                   finish();
               }else {
                   VerifyUserExistance();
                   dialog.dismiss();
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        main_page_toolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(main_page_toolbar);
        getSupportActionBar().setTitle("Incident Management System");

        myViewPager = (ViewPager) findViewById(R.id.main_tabs_pager);
        myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessorAdapter);

        myTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);


    }

     @Override
     protected void onStart() {
         super.onStart();

     }

     private void VerifyUserExistance()
     {
         String currentUserID = mAuth.getCurrentUser().getUid();
         rootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if ((dataSnapshot.child("name").exists()))
                 {
                     Toast.makeText(MainActivity.this, "Welcome " + dataSnapshot.child("name").getValue().toString(), Toast.LENGTH_SHORT).show();
                 }

                 if ((dataSnapshot.child("role").exists()))
                 {
                     Toast.makeText(MainActivity.this, "You're an Admin", Toast.LENGTH_SHORT).show();
                 }

                 else
                 {
                     Toast.makeText(MainActivity.this, "Please update your profile...", Toast.LENGTH_SHORT).show();
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });



     }


     private void SendUserToLoginActivity() {

         Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
         loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(loginIntent);
         finish();
     }


     private void SendUserToProfileActivity() {

         Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
         profileIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         profileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(profileIntent);
     }


     private void SendUserToAddAdminActivity() {

         Intent addAdminIntent = new Intent(MainActivity.this, AddAdminActivity.class);
         addAdminIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         addAdminIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(addAdminIntent);
     }


     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

         getMenuInflater().inflate(R.menu.options_menu, menu);

         return true;
     }



     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

         if (item.getItemId() == R.id.main_logout_option){

            mAuth.signOut();
            SendUserToLoginActivity();

         }
         else if (item.getItemId() == R.id.main_add_admin_option){
             SendUserToAddAdminActivity();

         }

         else if (item.getItemId() == R.id.main_updateProfile_option){
             SendUserToProfileActivity();

         }


         return true;

     }


 }
