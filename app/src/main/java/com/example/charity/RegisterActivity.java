package com.example.charity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity{

    private TextView banner, registerUser;
    private EditText name, email, password, cpassword;
    private Button Rbutton;
    private ProgressBar progressBar ;
    FirebaseFirestore fstore;
    private FirebaseAuth mAuth;

    private String  userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        mAuth = FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        email = (EditText) findViewById(R.id.EmailInput);
        name = (EditText) findViewById(R.id.NameInput);
        password = (EditText) findViewById(R.id.PasswordInput);
        cpassword = (EditText) findViewById(R.id.CPasswordInput);
        Rbutton = (Button) findViewById(R.id.Rbutton);

        Rbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String emailt =email.getText().toString().trim();
                String passwordt = password.getText().toString().trim();
                String cpasswordt = cpassword.getText().toString().trim();
                String regex = "^(.+)@(.+)$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(emailt);
                if (name.getText().toString().trim().equalsIgnoreCase(""))
                {
                    name.setError("Name is Empty");
                }
                else if(!matcher.matches())
               {
                  email.setError("Email incorrect");

               }
               else if (passwordt.equalsIgnoreCase(""))
               {
                    password.setError("Password is Empty");
               }
                else if (!passwordt.equalsIgnoreCase(cpasswordt))
                {
                    cpassword.setError("Passwords doesn't match");
                }

                else
               {
                   mAuth.createUserWithEmailAndPassword(emailt, passwordt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           AppCompatActivity a = RegisterActivity.this;
                           if (task.isSuccessful()) {
                               userID=mAuth.getCurrentUser().getUid();
                               FirebaseFirestore db = FirebaseFirestore.getInstance();
                               Map<String,Object> user = new HashMap<>();
                               user.put("nomU",name.getText().toString());
                               user.put("email",emailt);
                               db.collection("users")
                                       .add(user)
                                       .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                           @Override
                                           public void onSuccess(DocumentReference documentReference) {

                                               moveToLogin();
                                               Toast.makeText(a, "User Created", Toast.LENGTH_SHORT).show();
                                           }
                                       })
                                       .addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {

                                           }
                                       });
                           } else {
                               Toast.makeText(a, "Error"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                           }


                       }
                   });
               }
                }
        });




    }
    private void moveToLogin() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}