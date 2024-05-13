package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.androidproject.dao.AccountDAO;
import com.example.androidproject.dao.DAOBookList;
import com.example.androidproject.dao.DAODetailManga;
import com.example.androidproject.dao.DAOPlayList;
import com.example.androidproject.entity.Account;
import com.example.androidproject.entity.BookList;
import com.example.androidproject.entity.PlayList;

import java.util.ArrayList;
import java.util.List;

public class DetailMgaActivity extends AppCompatActivity {
    private TextView txtName, txtAuthor, txtCategory, txtPrice, txtDescription, txtNameAndComment;
    private Button btnRead, btnAdd, btnComment, btnChapter;
    private ImageView imgView;
    private int idReadingList = 0;
    private int idFavorList = 0;
    private final List<PlayList> playlists = new ArrayList<>();
    private Context mContext;
    private DAODetailManga daoDetailManga = new DAODetailManga();
    private DAOBookList daoBookList = new DAOBookList();
    private DAOPlayList daoPlayList = new DAOPlayList();
    private AccountDAO accountDAO;
    boolean isDataLoaded = false;
    private int accountID;

    public DetailMgaActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_mga);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        mappingComponent();
        getSQLiteData();
        getAllData();
        RenderData();

        btnRead.setOnClickListener(v -> goToChapter());

        btnAdd.setOnClickListener(v -> addFavor());

        btnComment.setOnClickListener(v -> goToComment());

        btnChapter.setOnClickListener(v -> goToChapter());

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            backToHome();
        });
    }
    public void backToHome(){
        Intent intent = new Intent(DetailMgaActivity.this, UserHomeActivity.class);
        startActivity(intent);
    }
    private void mappingComponent() {
        mContext = this;
        txtName = findViewById(R.id.txtName);
        txtAuthor = findViewById(R.id.txtAuthor);
        txtCategory = findViewById(R.id.txtCategory);
        txtPrice = findViewById(R.id.txtPrice);
        txtDescription = findViewById(R.id.txtDescription);
        txtNameAndComment = findViewById(R.id.textViewNameAndComment);

        btnRead = findViewById(R.id.buttonRead);
        btnAdd = findViewById(R.id.buttonAddToFavor);
        btnComment = findViewById(R.id.buttonComment);
        btnChapter = findViewById(R.id.buttonChapter);

        imgView = findViewById(R.id.imgViewCover);

    }

    private void RenderData() {
        String bookId = getParseData();

        daoDetailManga.getBookById(bookId, detailBook -> {
            if (detailBook != null) {
                txtName.setText(detailBook.getName());

                String authorID = detailBook.getAuthorId();
                daoDetailManga.getAuthorByID(authorID, author -> {
                    txtAuthor.setText(author.getName());
                });

                txtPrice.setText("Price: " + detailBook.getPrice() + "$");

                txtDescription.setText(detailBook.getName());

                String imageUrl = detailBook.getCoverURL();
                Glide.with(this)
                        .load(imageUrl)
                        .apply(new RequestOptions())
                        .into(imgView);
            }
        });

        daoDetailManga.getLatestRating(bookId, rating -> {
            if(rating != null) {
                daoDetailManga.getUserNameByID(String.valueOf(rating.getUserId()), account -> {
                    txtNameAndComment.setText(account.getFullName() + "\n" + rating.getComment());
                });
            }
        });
    }

    private String getParseData() {
        try {
            Intent intent = getIntent();
            if (intent != null) {
                Log.i("aaaaaaaaaaaaaaaaaaaaaaaa", intent.getStringExtra("BOOK_ID"));
                return intent.getStringExtra("BOOK_ID");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "-NxSlK3ynwldt7Wp3ihX";
    }

    private void goToChapter() {
        addReading();
        Intent intent = new Intent(DetailMgaActivity.this, ChapterActivity.class);
        intent.putExtra("BookId", getParseData());
        startActivity(intent);
    }

    private void addFavor() {
        getAllData();
        if (isDataLoaded) {
            BookList favorBL = new BookList(idFavorList, getParseData());
            daoBookList.addBookList(favorBL);
            Toast.makeText(mContext, "Add to favorite successfully!", Toast.LENGTH_SHORT).show();
        }
    }
    private void addReading() {
        getAllData();
        if (isDataLoaded) {
            BookList favorR = new BookList(idReadingList, getParseData());
            daoBookList.addBookList(favorR);
        }
    }
    private void getAllData(){
        daoPlayList.getAllListByAccountId(accountID, playlistList ->  {
            this.playlists.clear();
            this.playlists.addAll(playlistList);
        });
        for (PlayList list : playlists) {
            if (idFavorList == 0 || idReadingList == 0) {
                Log.d("List", "" + list.getName());
                if (list.getName().equals("Favorite")) {
                    idFavorList = list.getListID();
                } else if (list.getName().equals("Reading")) {
                    idReadingList = list.getListID();
                }
            } else {
                isDataLoaded = true;
                break;
            }
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
    private void goToComment() {
        Intent intent = new Intent(DetailMgaActivity.this, WriteCmtActivity.class);
        intent.putExtra("BookId", getParseData());
        startActivity(intent);
    }
}