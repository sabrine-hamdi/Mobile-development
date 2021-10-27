package com.example.charity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class AssociationActivity extends AppCompatActivity {

    private TextView associaName;
    private TextView associaMail;
    private TextView associaTel;
    private ImageView associaImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_association);
        associaName = (TextView) findViewById(R.id.AssociaName);
        associaMail = (TextView) findViewById(R.id.AssociaMail);
        associaTel = (TextView) findViewById(R.id.AssociaTel);
        associaImg = (ImageView) findViewById(R.id.LogoImg);
        String associa = getIntent().getStringExtra("name");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("associations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot ds : task.getResult()) {
                                Map<String,Object> association = new HashMap<>();
                                String name = "e",mail,tel,image;
                                association = ds.getData();

                                name =(String) association.get("name");
                                if(name.equals(associa))
                                {

                                    tel = (String) association.get("tel");
                                    mail = (String) association.get("mail");
                                    image =(String) association.get("image");
                                    associaTel.setText(tel);
                                    associaMail.setText(mail);
                                    associaName.setText(associa);
                                    Glide.with(AssociationActivity.this)
                                            .load(image)
                                            .into(associaImg);

                                }




                            }
                        } else {

                        }
                    }
                });
    }

}