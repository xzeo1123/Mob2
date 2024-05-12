package com.example.androidproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.dao.DAOBook;
import com.example.androidproject.dao.DAOBookList;
import com.example.androidproject.dao.DAOPlayList;
import com.example.androidproject.entity.Book;
import com.example.androidproject.entity.BookList;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BooklistViewHolder> {

    private final List<Book> books;
    private final Context mContext;
    private final DAOBookList daoBookList = new DAOBookList();
    private final DAOBook daoBook = new DAOBook();
    private static Intent intent;
    private final int listID;

    public BookListAdapter(Context context, List<Book> books, int listID) {
        this.books = books;
        this.mContext = context;
        this.listID = listID;
    }


    @NonNull
    @Override
    public BooklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_blist_item, parent, false);
        return new BooklistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooklistViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Book book = books.get(position);
        String imageUrl = book.getCoverURL();
        Picasso.get().load(imageUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // Placeholder image if needed
            }
        });
        holder.textViewTitle.setText(book.getName());
        holder.textViewPrice.setText(String.valueOf(book.getPrice()));
        holder.textViewAuthor.setText(book.getAuthorID());
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle overflow button click
                holder.showDeleteConfirmationDialog(book.getBookID(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BooklistViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTitle;
        TextView textViewAuthor;
        TextView textViewPrice;
        Button buttonDelete;


        public BooklistViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }

        private void showDeleteConfirmationDialog(String bookID, int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            Log.d("favor", "List" + listID);
            builder.setTitle("Confirm Delete");
            builder.setMessage("Are you sure you want to delete this book from the list?");
            builder.setPositiveButton("Delete", (dialog, which) -> {
                deleteBooklist(bookID, position);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // Cancel action, do nothing
            });
            builder.show();
        }



        private void deleteBooklist(String bookID, int position) {
            daoBookList.deleteBooklistById(bookID, listID, new DAOBookList.DeleteCallback() {
                @Override
                public void onDeleteSuccess() {
                    // Handle successful deletion
                    Toast.makeText(mContext, "Booklist deleted successfully", Toast.LENGTH_SHORT).show();
                    books.remove(position); // Assuming playlists is your list of playlists
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, books.size());
                }

                @Override
                public void onDeleteFailure(String errorMessage) {
                    // Handle deletion failure
                }
            });
        }
    }
}
