package com.example.charity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    Context c;
    ArrayList<Association> models;
   View.OnClickListener mOnClickListener ;

    public MyAdapter(Context c, ArrayList<Association> models,View.OnClickListener mOnClickListener) {
        this.c = c;
        this.models = models;
        this.mOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, null);

        view.setOnClickListener(mOnClickListener);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {

        holder.mTitle.setText(models.get(i).getTitle());
        holder.mDes.setText(models.get(i).getDescription());
        Glide.with(c)
                .load(models.get(i).getImg())
                .into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

}
