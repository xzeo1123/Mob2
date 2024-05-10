package com.example.androidproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {

//    private List<Book> books;

//    public BookListAdapter(List<Book> books) {
//        this.books = books;
//    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_bclist_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
//        Book book = books.get(position);
//        holder.textViewTitle.setText(book.getTitle());
//        holder.textViewAuthor.setText(book.getAuthor());
//        // Set other book details as needed
    }

    @Override
    public int getItemCount() {
        return 0;
    }

//    @Override
//    public int getItemCount() {
//        return books.size();
//    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewAuthor;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
//            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
        }
    }
}
