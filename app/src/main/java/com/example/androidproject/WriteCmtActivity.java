package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidproject.dao.AccountDAO;
import com.example.androidproject.dao.DAOWriteCmt;
import com.example.androidproject.entity.Rating;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WriteCmtActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private Button btnSend;
    private EditText etxtCmt;
    private Context mContext;
    private final DAOWriteCmt daoWriteCmt = new DAOWriteCmt();
    private final List<Integer> idList = new ArrayList<>();
    private int marking = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_write_cmt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mappingComponent();

        getAllData();

        btnSend.setOnClickListener(v -> writeComment());

        btnBack.setOnClickListener(v -> goToBook());
    }

    private void mappingComponent() {
        etxtCmt = findViewById(R.id.editTextComment);

        btnSend = findViewById(R.id.buttonSend);
        btnBack = findViewById(R.id.buttonBack);

        mContext = this;
    }

    private void getAllData() {
        daoWriteCmt.getIDList(idList -> {
            this.idList.clear();
            for (String id : idList) {
                try {
                    Integer intValue = Integer.parseInt(id);
                    this.idList.add(intValue);
                } catch (NumberFormatException ignored) {
                }
            }
        });
    }

    private int getSQLiteData() {
        AccountDAO accountDAO = new AccountDAO(mContext);
        boolean check = accountDAO.checkExistAccount();
        int id = 0;
        if (check) {
            id = accountDAO.getID();
        }
        return id;
    }

    private void writeComment() {
        String comment = String.valueOf(etxtCmt.getText());
        int userId = getSQLiteData();
        int bookId = getParseData();

        getAllData();
        setPosition();

        int ratingId = marking;

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        String formattedDate = currentDate.format(formatter);

        Rating rating = new Rating(ratingId, comment, formattedDate, bookId, userId);
        daoWriteCmt.writeComment(marking, rating);
        goToBook();
    }

    private void setPosition() {
        getAllData();
        int i = 1;
        while (true) {
            if (!idList.contains(i)) {
                marking = i;
                break;
            }
            i += 1;
        }
    }

    private int getParseData() {
        Intent intent = getIntent();
        if (intent != null) {
            return intent.getIntExtra("BookId", 0);
        }
        return 0;
    }

    private void goToBook() {
        Intent intent = new Intent(WriteCmtActivity.this, DetailMgaActivity.class);
        intent.putExtra("BookId", getParseData());
        startActivity(intent);
    }
}