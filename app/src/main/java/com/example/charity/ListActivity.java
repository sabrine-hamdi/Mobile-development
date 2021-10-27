package com.example.charity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListActivity extends AppCompatActivity {
    private Button LgoBtn;
    private ImageView MapBtn;
    RecyclerView mRecyclerView;
    MyAdapter myAdapter;
    ArrayList<Association> associaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        MapBtn = (ImageView) findViewById(R.id.MapBtn);
        MapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToMap();
            }
        });

         mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        LgoBtn = (Button) findViewById(R.id.lgoBtn);
        LgoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManagement sessionManagement = new SessionManagement(ListActivity.this);
                sessionManagement.removeSession();

                moveToLogin();
            }
        });



          }


    @Override
    protected void onStart() {
        super.onStart();
       getMyList();

    }

    public void getMyList() {

        ArrayList<Association> models = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();;
        StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReference();

      db.collection("associations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {


                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot ds: task.getResult()) {
                               Map<String,Object> association = new HashMap<>();
                                String name = "e",type,image;
                                association = ds.getData();

                                type = (String) association.get("type");
                                name =(String) association.get("name");
                                image =(String) association.get("image");
                               Association m = new Association();
                                m.setTitle(name);
                                m.setDescription(type);

                                m.setImg(image);
                                models.add(m);




                            }
                            View.OnClickListener mOnclickListener = new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int itemPosition = mRecyclerView.getChildLayoutPosition(v);
                                    String item = models.get(itemPosition).getTitle();
                                    moveToAssociation(item);
                                }
;                            };

                            myAdapter = new MyAdapter(ListActivity.this,models,mOnclickListener);
                            mRecyclerView.setAdapter(myAdapter);


                        }
                        else {
                        }
                    }
                });


    }
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
    private void moveToMap() {
        Intent intent = new Intent(ListActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void moveToAssociation(String name) {
        Intent intent = new Intent(ListActivity.this, AssociationActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("name",name);
        startActivity(intent);
    }
    private void moveToLogin() {
        Intent intent = new Intent(ListActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}