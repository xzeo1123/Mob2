package com.example.androidproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

import com.example.androidproject.dao.DAOPlayList;
import com.example.androidproject.entity.PlayList;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    private final List<PlayList> playlists;
    private final Context mContext;
    private final DAOPlayList daoPlayList = new DAOPlayList();
    private static Intent intent;

    public PlaylistAdapter(Context context, List<PlayList> playlists) {
        this.playlists = playlists;
        this.mContext = context;
    }


    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_bclist_item, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PlayList playlist = playlists.get(position);
//        holder.imageView.setImageResource(playlist.getImageResId());
        holder.textViewTitle.setText(playlist.getName());
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle overflow button click
                holder.showDeleteConfirmationDialog(playlist.getListID(), position);
            }
        });
        holder.buttonOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create Intent to navigate to the new activity
                Intent intent = new Intent(mContext, BCBooksActivity.class);

                // Put the list ID as an extra in the Intent
                intent.putExtra("LIST_ID", playlist.getListID());

                // Start the new activity with the Intent
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTitle;
        Button buttonDelete;
        Button buttonOpen;


        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonOpen = itemView.findViewById(R.id.buttonOpen);
        }

        private void showDeleteConfirmationDialog(int listID, int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Confirm Delete");
            builder.setMessage("Are you sure you want to delete this playlist?");
            builder.setPositiveButton("Delete", (dialog, which) -> {
                deletePlaylist(listID, position);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // Cancel action, do nothing
            });
            builder.show();
        }
        private void openPlaylist(int listID, int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Confirm Delete");
            builder.setMessage("Are you sure you want to delete this playlist?");
            builder.setPositiveButton("Delete", (dialog, which) -> {
                deletePlaylist(listID, position);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // Cancel action, do nothing
            });
            builder.show();
        }



        private void deletePlaylist(int listID, int position) {
            daoPlayList.deletePlaylistById(listID, new DAOPlayList.DeleteCallback() {
                @Override
                public void onDeleteSuccess() {
                    // Handle successful deletion
                    Toast.makeText(mContext, "Playlist deleted successfully", Toast.LENGTH_SHORT).show();
                    playlists.remove(position); // Assuming playlists is your list of playlists
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, playlists.size());
                }

                @Override
                public void onDeleteFailure(String errorMessage) {
                    // Handle deletion failure
                }
            });
        }
    }
}
