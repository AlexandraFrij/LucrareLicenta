package com.example.licenta.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.licenta.ChatPage;
import com.example.licenta.FirebaseHelper;
import com.example.licenta.holder.SearchUserHolder;
import com.example.licenta.R;
import com.example.licenta.item.SearchUserRecyclerViewItem;
import com.example.licenta.util.AndroidUtil;

import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserHolder>
{
    Context context;
    List<SearchUserRecyclerViewItem> items;

    public SearchUserAdapter(Context context, List<SearchUserRecyclerViewItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public SearchUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new SearchUserHolder(LayoutInflater.from(context).inflate(R.layout.searched_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserHolder holder, int position)
    {
        String username = items.get(position).getUsername();
        int photo = items.get(position).getImage();
        String email = items.get(position).getEmail();
        holder.username.setText(username);
        holder.profilePicture.setImageResource(photo);
        FirebaseHelper.getProfilePicture(email).getDownloadUrl()
                .addOnCompleteListener( task ->
                {
                    if(task.isSuccessful())
                    {
                        Uri uri = task.getResult();
                        AndroidUtil.setProfilePicture(context, uri, holder.profilePicture);
                    }
                });

        holder.itemView.setOnClickListener(v ->{
            Intent intent = new Intent(context, ChatPage.class);
            intent.putExtra("username", username);
            intent.putExtra("photo", photo);
            intent.putExtra("email", email);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
