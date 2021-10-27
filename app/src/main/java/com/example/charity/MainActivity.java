package com.example.charity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private EditText EmailInput;
    private EditText PasswordInput;
    private Button CnxButton;
    private TextView Register_msg;
    private boolean loggedIn = false;
    private FirebaseAuth mAuth;
    private String res = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EmailInput = (EditText) findViewById(R.id.EmailInput);
        PasswordInput = (EditText) findViewById(R.id.PasswordInput);
        CnxButton = (Button) findViewById(R.id.CnxButton);
        Register_msg = (TextView) findViewById(R.id.Register_msg);
        mAuth = FirebaseAuth.getInstance();
        CnxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verif de bdd
                String  email = EmailInput.getText().toString();
                String  password = PasswordInput.getText().toString();
                String regex = "^(.+)@(.+)$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(email);

                if(!matcher.matches() || password.equalsIgnoreCase(""))
                {

                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        getUserID(email);
                                        User user = new User(MainActivity.this.res,EmailInput.getText().toString());
                                        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
                                        sessionManagement.saveSession(user);

                                        moveToMainActivity();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }

            }
        });
        Register_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToRegisterActivity();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

        checkSession();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    private void checkSession() {

        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        String userID = sessionManagement.getSession();
        //verif bdd
        if(!userID.equals("-1")){
              moveToMainActivity();
        }
    }

    private void getUserID(String mail){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot ds : task.getResult()) {
                                Map<String,Object> users = new HashMap<>();
                                String email;
                                users = ds.getData();

                                email =(String) users.get("email");
                                if(mail.equals(email))
                                {

                                    MainActivity.this.res = ds.getId();


                                }




                            }
                        } else {

                        }
                    }
                });
    }
    private void moveToRegisterActivity() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}