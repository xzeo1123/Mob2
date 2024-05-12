package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.dao.AccountDAO;
import com.example.androidproject.dao.DAOBook;
import com.example.androidproject.dao.DAOBookList;
import com.example.androidproject.dao.DAOPlayList;
import com.example.androidproject.entity.Account;
import com.example.androidproject.entity.Book;
import com.example.androidproject.entity.BookList;
import com.example.androidproject.entity.PlayList;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class BCReadingActivity extends AppCompatActivity {

    private int idList =100000;
    private final DAOPlayList daoPlayList = new DAOPlayList();
    private final DAOBookList daoBookList = new DAOBookList();
    private final List<BookList> booklist = new ArrayList<>();
    private final List<Book> books = new ArrayList<>();
    private int accountID;
    private PlayList readingList;
    private int marking = 1;
    private boolean isDataLoaded = false; // Flag to track if data is loaded

    protected Intent intent;
    private AccountDAO accountDAO;
    private Context mContext;
    private Button readingButton;
    private Button favorButton;
    private Button listButton;
    private EditText editTextName;
    private Button buttonAdd;
    private Button buttonCancel;
    private ImageButton buttonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bcreading);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mappingComponent();
        getSQLiteData();
        getAllData();
    }
    private void mappingComponent(){
        mContext = this;
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> goToHome());

        readingButton = findViewById(R.id.reading);
        readingButton.setOnClickListener(v -> goToReading());
        favorButton = findViewById(R.id.favor);
        favorButton.setOnClickListener(v -> goToFavor());
        listButton = findViewById(R.id.playList);
        listButton.setOnClickListener(v -> goToList());
    }
    private void setAdapter(){
        final BookListAdapter adapter = new BookListAdapter(this, books, idList);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewBooklists);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (isDataLoaded) { // Check if data is loaded before initializing the adapter
            recyclerView.setAdapter(adapter);
        } else {
            // Handle case where data is not loaded yet
        }
    }
    private void getSQLiteData() {
        if (mContext == null) {
            Log.d("a", "mContext is null");
            return;
        }
        accountDAO = new AccountDAO(mContext);
        boolean check = accountDAO.checkExistAccount();
        if (check) {
            Account account = accountDAO.getAccount();
            accountID = AccountDAO.getID();
        }
    }
    private void getAllData(){
        daoPlayList.getReadingListByAccountId(accountID, playlistList ->  {
            if (!playlistList.isEmpty()) {
                this.readingList = playlistList.get(0);
                Log.d("readinglist", "List" + readingList.getName());
                idList = readingList.getListID();
                // Call daoBookList.getBookListByListID only after readingList is fetched
                daoBookList.getBookListByListID(readingList.getListID(), books ->  {
                    this.books.clear();
                    this.books.addAll(books);
                    isDataLoaded = true;
                    setAdapter();
                });
            } else {
                Log.d("readinglist", "Reading list is empty");
                // Handle the case where the reading list is empty
            }
        });
    }

    private void goToReading() {
        restartActivity();
    }
    private void goToFavor() {
        Intent intent = new Intent(BCReadingActivity.this, BCFavorActivity.class);
        startActivity(intent);
    }
    private void goToList() {
        Intent intent = new Intent(BCReadingActivity.this, BCListActivity.class);
        startActivity(intent);
    }
    private void goToHome() {
        Intent intent = new Intent(BCReadingActivity.this, UserHomeActivity.class);
        startActivity(intent);
    }
    private void restartActivity() {
        intent = getIntent();
        finish();
        startActivity(intent);
    }
}