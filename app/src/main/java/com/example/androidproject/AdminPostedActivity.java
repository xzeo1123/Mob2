package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.IRecycleView.IRecycleViewAdminPosted;
import com.example.androidproject.dao.DAOBook;
import com.example.androidproject.entity.AdminAccount;
import com.example.androidproject.entity.Book;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AdminPostedActivity extends AppCompatActivity implements IRecycleViewAdminPosted {
    public ArrayList<Book> bookArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_posted);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        RecyclerView booksRecycleView = findViewById(R.id.booksRecycleView);
        APosted_RecyclerViewAdapter adapter = new APosted_RecyclerViewAdapter(this, bookArrayList, this);

        new DAOBook().getBook(new DAOBook.GetBookDataListener() {
            @Override
            public void onBooksLoaded(ArrayList<Book> books) {
                bookArrayList.clear();
                bookArrayList.addAll(books);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(String errorMessage) {
                Log.i("ERROR", errorMessage);
            }
        });
        booksRecycleView.setAdapter(adapter);
        booksRecycleView.setLayoutManager(new LinearLayoutManager(AdminPostedActivity.this));
    }

    @Override
    public void onBookClicked(int position) {
        Book book = bookArrayList.get(position);
        //Check this part is null or not
        Intent intent = new Intent(AdminPostedActivity.this, AdminBookDetail.class);
        intent.putExtra("singleBook", book);
        startActivity(intent);
    }
}