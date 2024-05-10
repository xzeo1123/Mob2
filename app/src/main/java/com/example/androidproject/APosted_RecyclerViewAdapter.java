package com.example.androidproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.IRecycleView.IRecycleViewAdminAcc;
import com.example.androidproject.IRecycleView.IRecycleViewAdminPosted;
import com.example.androidproject.entity.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class APosted_RecyclerViewAdapter extends RecyclerView.Adapter<APosted_RecyclerViewAdapter.MyViewHolder> {
    private final IRecycleViewAdminPosted iRecycleViewAdminPosted;

    public Context context;
    public ArrayList<Book> bookArrayList;
    public APosted_RecyclerViewAdapter(Context context, ArrayList<Book> bookArrayList, IRecycleViewAdminPosted iRecycleViewAdminPosted){
        this.context = context;
        this.bookArrayList = bookArrayList;
        this.iRecycleViewAdminPosted = iRecycleViewAdminPosted;
    }

    @NonNull
    @Override
    public APosted_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_view_adminpostedbooks, parent, false);
        return new APosted_RecyclerViewAdapter.MyViewHolder(view, iRecycleViewAdminPosted);
    }

    @Override
    public void onBindViewHolder(@NonNull APosted_RecyclerViewAdapter.MyViewHolder holder, int position) {
        Book book = bookArrayList.get(position);
        holder.txtbookTitle.setText(book.getName());
        holder.txtBookAuthor.setText(book.getAuthorID());
        Picasso.get().load(book.getCoverURL()).into(holder.bookCover);
    }

    @Override
    public int getItemCount() {
        return bookArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView bookCover;
        TextView txtbookTitle, txtBookAuthor;
        public MyViewHolder(@NonNull View itemView, IRecycleViewAdminPosted iRecycleViewAdminPosted) {
            super(itemView);

            bookCover = itemView.findViewById(R.id.imgBookCover);
            txtbookTitle = itemView.findViewById(R.id.txtbookTitle);
            txtBookAuthor = itemView.findViewById(R.id.txtBookAuthor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(iRecycleViewAdminPosted != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            iRecycleViewAdminPosted.onBookClicked(position);
                        }
                    }
                }
            });
        }
    }
}
