package com.yasser.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText email, password;
    Button btnLogin;
    TextView mTextLink;

    FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();

        // Automatically login
        if(mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Log In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.edt_email);
        password = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        mTextLink = findViewById(R.id.txt_signup);

        // Check if session is already active (true),
        if(mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if(txt_email.equals("")){
                    email.setError("من فضلك اكتب البريدالالكترونى!");
                }
                if(txt_password.equals("")){
                    password.setError("من فضلك اكتب الرقم السرى!");
                }
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(LoginActivity.this, "من فضلك ادخل جميع البيانات..", Toast.LENGTH_SHORT).show();
                } else {

                    mAuth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "تم تسجيل الدخول بنجاح..!", Toast.LENGTH_SHORT).show();
                                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                        final DatabaseReference myRef = database.getReference("Users").getRef().child(firebaseUser.getUid());

                                        myRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                for (DataSnapshot chidSnap : dataSnapshot.getChildren()) {
                                                    String x=dataSnapshot.child("Type").getValue().toString();
                                                    if(x.equals("اصم")){
                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                    }else if(x.equals("مساعد")){
                                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    } else {
                                        Toast.makeText(LoginActivity.this,  "هناك خطا فى البريد الالكترونى او الرقم السرى!"
                                                , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


        mTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}
