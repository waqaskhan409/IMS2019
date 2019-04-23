package com.example.ims2019;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddAdminActivity extends AppCompatActivity {
    private static final String TAG = "AddAdminActivity";
    private Button addAdminButton, cancelAdminButton;
    private AutoCompleteTextView addAdminPassword;
    private AutoCompleteTextView addAdminEmail;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private ProgressDialog loadingBar;
    String uidNew = "";
    private ArrayList<String> uid = new ArrayList<>();
    private ArrayList<String> emailL = new ArrayList<>();
    private ArrayList<String> role = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_admin);

        mAuth = FirebaseAuth.getInstance();

        rootRef = FirebaseDatabase.getInstance().getReference().child("Users");
        role.add("simple");
        role.add("admin");
        InitializeFields();
        addAdminEmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                uidNew = uid.get(position);
            }
        });

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String ui = snapshot.getKey();
                Log.d(TAG, "onDataChange: " + ui);
                    uid.add(snapshot.getKey());
                    emailL.add(snapshot.child("email").getValue().toString());

                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateNewAccount();
            }

        });


        cancelAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendToMainActivity();
            }

        });

    }
    private void setAdapter(){
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, emailL);
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, role);
        addAdminEmail.setAdapter(adapter);
        addAdminPassword.setAdapter(adapter1);

    }

    private void InitializeFields() {

        addAdminButton = (Button) findViewById(R.id.add_admin_btn);
        cancelAdminButton = (Button) findViewById(R.id.cancel_admin_btn);
        addAdminEmail = (AutoCompleteTextView) findViewById(R.id.add_admin_email);
        addAdminPassword = (AutoCompleteTextView) findViewById(R.id.add_admin_pasword);
        loadingBar = new ProgressDialog(AddAdminActivity.this);

    }

    private void SendToMainActivity() {
        Intent mainIntent = new Intent(AddAdminActivity.this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
    }

    private void CreateNewAccount() {



        String email = addAdminEmail.getText().toString();
        String password = addAdminPassword.getText().toString();

        if (TextUtils.isEmpty(email)){

            Toast.makeText(AddAdminActivity.this, "Please enter your email...", Toast.LENGTH_SHORT).show();
            return;

        }

        if (TextUtils.isEmpty(password)){

            Toast.makeText(AddAdminActivity.this, "Please enter your password...", Toast.LENGTH_SHORT).show();
            return;

        }
        if(!emailL.contains(email)){
            Toast.makeText(AddAdminActivity.this, "Sorry this email is not registered yet...", Toast.LENGTH_SHORT).show();
            return;

        }

        if(!role.contains(password)){
            Toast.makeText(AddAdminActivity.this, "Sorry this role is not defined yet ...", Toast.LENGTH_SHORT).show();
            return;

        }
        else{

            loadingBar.setTitle("Adding An Account");
            loadingBar.setMessage("Please wait, while we add the account...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            String currentUserID = mAuth.getCurrentUser().getUid();
            rootRef.child(uidNew).child("role").setValue(password);
            loadingBar.dismiss();
            /*mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful()){

                                String currentUserID = mAuth.getCurrentUser().getEmail();
                                rootRef.child("Users").child(currentUserID).setValue("");
                                Toast.makeText(AddAdminActivity.this, "Admin added succussfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                SendToMainActivity();

                            }
                            else{

                                String message = task.getException().toString();
                                Toast.makeText(AddAdminActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }
                        }
                    });*/

        }


    }
}
