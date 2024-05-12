package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.androidproject.IRecycleView.IRecycleViewUserChapter;
import com.example.androidproject.entity.Chapter;
import com.example.androidproject.dao.DAOBook;
import java.util.ArrayList;
import java.util.List;

public class ChapterActivity extends AppCompatActivity implements IRecycleViewUserChapter {
    private ArrayList<Chapter> chaptersList = new ArrayList<>();
    private String BookId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chapter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v ->{
            backToDetail();
        });

        RecyclerView recycleViewChapterList = findViewById(R.id.recycleViewChapterList);
        Intent intent = getIntent();
        String bookId = intent.getStringExtra("BookId");
        BookId = intent.getStringExtra("BookId");

        UViewChapter_RecyclerViewApdapter adapter = new UViewChapter_RecyclerViewApdapter(this, chaptersList, this);

        new DAOBook().getChapter(bookId,  new DAOBook.ChapterFetchListener(){

            @Override
            public void onChaptersFetched(ArrayList<Chapter> chapters) {
                chaptersList.clear();
                chaptersList.addAll(chapters);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFetchError(String e) {
                Log.i("ERROR", e);
            }
        });
        recycleViewChapterList.setAdapter(adapter);
        recycleViewChapterList.setLayoutManager(new LinearLayoutManager(ChapterActivity.this));
    }

    public void backToDetail(){
        Intent intent = new Intent(ChapterActivity.this, DetailMgaActivity.class);
        startActivity(intent);
    }
    @Override
    public void onChapterClicked(int position) {
        Intent intent = new Intent(ChapterActivity.this, ReadChapterActivity.class);
        intent.putExtra("chapter", chaptersList.get(position));
        intent.putExtra("BookId", BookId);
        startActivity(intent);
    }
}
