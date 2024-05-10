package com.example.androidproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity {

//    private List<Book> books = new ArrayList<>();
    private BookListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bclist);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewPlaylists);
//        adapter = new BookListAdapter(books);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Call DAO to fetch book IDs based on the selected playlist ID
//        int playlistID = getIntent().getIntExtra("playlist_id", -1);
//        if (playlistID != -1) {
//            fetchBookIds(playlistID);
//        }
    }

//    private void fetchBookIds(int playlistID) {
//        DAOBookList.getInstance().getbookIdList(playlistID, bookIDList -> {
//            // Now, fetch details of each book using its ID
//            for (String bookID : bookIDList) {
//                BookDAO.getInstance().getBookDetails(bookID, book -> {
//                    if (book != null) {
//                        books.add(book);
//                        adapter.notifyDataSetChanged();
//                    }
//                });
//            }
//        });
//    }
}
