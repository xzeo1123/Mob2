package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.dao.AccountDAO;
import com.example.androidproject.dao.DAOPlayList;
import com.example.androidproject.entity.Account;
import com.example.androidproject.entity.PlayList;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class BCListActivity extends AppCompatActivity {
    private final List<PlayList> playlists = new ArrayList<>();
    private final List<Integer> idList = new ArrayList<>();
    private final PlaylistAdapter adapter = new PlaylistAdapter(this, playlists);

    private final DAOPlayList daoPlayList = new DAOPlayList();
    private int accountID;
    private int marking = 0;
    protected Intent intent;
    private AccountDAO accountDAO;
    private Context mContext;
    private Button addButton;
    private EditText editTextName;
    private Button buttonAdd;
    private Button buttonCancel;
    private ImageButton buttonBack;
    private BottomSheetDialog bottomSheetDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bclist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomSheetDialog = new BottomSheetDialog(this);
        mappingComponent();
        getSQLiteData();
        getAllData();
        setPosition();
    }
    private void mappingComponent(){
        mContext = this;
        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> goToHome());
        addButton = findViewById(R.id.addNewButton);
        addButton.setOnClickListener(v -> addNewPlayList());
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPlaylists);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        bottomSheetDialog.setContentView(R.layout.addplaylist_sheet);
        editTextName = bottomSheetDialog.findViewById(R.id.editTextName);
        buttonAdd = bottomSheetDialog.findViewById(R.id.buttonAdd);
        buttonCancel = bottomSheetDialog.findViewById(R.id.buttonCancel);
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
    private void goToHome() {
        Intent intent = new Intent(BCListActivity.this, UserHomeActivity.class);
        startActivity(intent);
    }

    private void getAllData(){
        daoPlayList.getListByAccountId(accountID, playlistList ->  {
            this.playlists.clear();
            this.playlists.addAll(playlistList);
            adapter.notifyDataSetChanged();
        });
        daoPlayList.getIDList(idList -> {
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
    private void addNewPlayList(){
        buttonAdd.setOnClickListener(v -> {
            setPosition();
            // Perform add action here
            String getName = String.valueOf(editTextName.getText());
            if (getName.equals("")) {
                Toast.makeText(mContext, "Vui long nhap ten danh sach!", Toast.LENGTH_SHORT).show();
                editTextName.requestFocus();
                return;
            }
            else {
                PlayList newPlayList = new PlayList(marking, getName,accountID);
                DAOPlayList.addPlayList(marking, newPlayList);
            }
            bottomSheetDialog.dismiss(); // Dismiss the bottom sheet after adding
            restartActivity();
        });

        buttonCancel.setOnClickListener(v -> {
            bottomSheetDialog.dismiss(); // Dismiss the bottom sheet on cancel
        });

        bottomSheetDialog.show();
    }
    private void setPosition() {
        getAllData();
        while (true) {
            if (!idList.contains(marking)) {
                break;
            }
            marking += 1;
        }
    }
    private void restartActivity() {
        intent = getIntent();
        finish();
        startActivity(intent);
    }
}
