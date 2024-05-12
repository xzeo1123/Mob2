package com.example.androidproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.IRecycleView.IRecycleViewUserChapter;
import com.example.androidproject.entity.Chapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UViewChapter_RecyclerViewApdapter  extends RecyclerView.Adapter<UViewChapter_RecyclerViewApdapter.MyViewHolder> {
    private final IRecycleViewUserChapter iRecycleViewUserChapter;
    Context context;
    ArrayList<Chapter> chapters;
    public UViewChapter_RecyclerViewApdapter(Context context, ArrayList<Chapter> chapters, IRecycleViewUserChapter iRecycleViewUserChapter){
        this.context = context;
        this.chapters = chapters;
        this.iRecycleViewUserChapter = iRecycleViewUserChapter;
    }
    @NonNull
    @Override
    public UViewChapter_RecyclerViewApdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_view_userchapterlist, parent, false);
        return new UViewChapter_RecyclerViewApdapter.MyViewHolder(view, iRecycleViewUserChapter);
    }

    @Override
    public void onBindViewHolder(@NonNull UViewChapter_RecyclerViewApdapter.MyViewHolder holder, int position) {
        holder.txtChapterName.setText(chapters.get(position).getName());
        holder.txtChapterUploadDate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                .format(new Date(chapters.get(position).getUploadDate())));
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtChapterName, txtChapterUploadDate;
        public MyViewHolder(@NonNull View itemView, IRecycleViewUserChapter iRecycleViewUserChapter) {
            super(itemView);
            txtChapterName = itemView.findViewById(R.id.txtChapterName);
            txtChapterUploadDate = itemView.findViewById(R.id.txtChapterUploadDate);
            itemView.setOnClickListener(v ->{
                if(iRecycleViewUserChapter != null){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        iRecycleViewUserChapter.onChapterClicked(pos);
                    }
                }
            });
        }
    }
}
