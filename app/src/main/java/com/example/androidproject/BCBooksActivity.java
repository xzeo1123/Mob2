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
import com.example.androidproject.dao.DAOBookList;
import com.example.androidproject.dao.DAOPlayList;
import com.example.androidproject.entity.Account;
import com.example.androidproject.entity.Book;
import com.example.androidproject.entity.PlayList;

import java.util.ArrayList;
import java.util.List;

public class BCBooksActivity extends AppCompatActivity {



    private final DAOPlayList daoPlayList = new DAOPlayList();
    private final DAOBookList daoBookList = new DAOBookList();
    private final List<Book> books = new ArrayList<>();
    private int idList = 0;
    private BookListAdapter adapter ;
    private int accountID;
    private PlayList favorList;
    private int marking = 1;
    protected Intent intent;
    private AccountDAO accountDAO;
    private Context mContext;
    private Button readingButton;
    private Button favorButton;
    private Button listButton;
    private EditText editTextName;
    private Button buttonAdd;
    private Button buttonCancel;
    private boolean isDataLoaded = false; // Flag to track if data is loaded
    private ImageButton buttonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bcfavor);
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
        idList = getIntent().getIntExtra("LIST_ID", -1);
        Log.d("favor", "List" + idList);
        // Call daoBookList.getBookListByListID only after readingList is fetched
        daoBookList.getBookListByListID(idList, books ->  {
            this.books.clear();
            this.books.addAll(books);
            isDataLoaded = true;
            setAdapter();
        });
    }

    private void goToFavor() {
        Intent intent = new Intent(BCBooksActivity.this, BCFavorActivity.class);
        startActivity(intent);
    }
    private void goToList() {
        Intent intent = new Intent(BCBooksActivity.this, BCListActivity.class);
        startActivity(intent);
    }
    private void goToHome() {
        Intent intent = new Intent(BCBooksActivity.this, UserHomeActivity.class);
        startActivity(intent);
    }
    private void goToReading() {
        Intent intent = new Intent(BCBooksActivity.this, BCReadingActivity.class);
        startActivity(intent);
    }
    private void restartActivity() {
        intent = getIntent();
        finish();
        startActivity(intent);
    }
}