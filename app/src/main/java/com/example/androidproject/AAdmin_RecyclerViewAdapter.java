package com.example.androidproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.IRecycleView.IRecycleViewAdminAcc;
import com.example.androidproject.entity.AdminAccount;

import java.util.ArrayList;

public class AAdmin_RecyclerViewAdapter extends RecyclerView.Adapter<AAdmin_RecyclerViewAdapter.MyViewHolder> {

    private final IRecycleViewAdminAcc iRecycleViewAdminAcc;
    Context context;
    ArrayList<AdminAccount> adminAccountArrayList;
    public AAdmin_RecyclerViewAdapter(Context context, ArrayList<AdminAccount> adminAccountArrayList, IRecycleViewAdminAcc iRecycleViewAdminAcc) {
        this.context = context;
        this.adminAccountArrayList = adminAccountArrayList;
        this.iRecycleViewAdminAcc = iRecycleViewAdminAcc;
    }

    @NonNull
    @Override
    public AAdmin_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_adminaccountadmin, parent, false);
        return new AAdmin_RecyclerViewAdapter.MyViewHolder(view, iRecycleViewAdminAcc);
    }

    @Override
    public void onBindViewHolder(@NonNull AAdmin_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tvEmail.setText(adminAccountArrayList.get(position).getDisplayName());
    }

    @Override
    public int getItemCount() {
        return adminAccountArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvEmail;
        public MyViewHolder(@NonNull View itemView, IRecycleViewAdminAcc iRecycleViewAdminAcc) {
            super(itemView);
            tvEmail = itemView.findViewById(R.id.recyclerEmail);

            itemView.findViewById(R.id.btnDetail).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (iRecycleViewAdminAcc != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            iRecycleViewAdminAcc.onClickResetPassword(pos);
                        }
                    }
                }
            });
        }
    }
}
