package com.neko2mizu.instaounce;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.neko2mizu.instaounce.helpers.TimeFormatter;
import com.neko2mizu.instaounce.objects.Post;
import com.parse.ParseFile;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;
    private final String TAG = "PostsAdapter";

    public PostsAdapter(Context context, List<Post> posts)
    {
        this.context = context;
        this.posts = posts;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear() {
        this.posts.clear();
        this.notifyDataSetChanged();
    }

    public void addAll(List<Post> posts){
        this.posts.addAll(posts);
        this.notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivProfilePic;
        private TextView tvUsername;
        private TextView tvTimeDisplay;
        private ImageView ivImage;
        private TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvTimeDisplay = itemView.findViewById(R.id.tvTimeDisplay);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(Post post) {

            ParseFile pfp = post.getUser().getParseFile("profileImage");
            if(pfp != null)
            {
                Glide.with(context).load(pfp.getUrl()).into(ivProfilePic);
            }


            tvUsername.setText(post.getUser().getUsername());
            tvTimeDisplay.setText(TimeFormatter.getTimeDifference(post.getDate()));
            ParseFile image = post.getImage();
            if(image != null)
            {
                Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
            }
            tvDescription.setText(post.getDescription());
        }
    }
}
