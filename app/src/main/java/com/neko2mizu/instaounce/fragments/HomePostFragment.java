package com.neko2mizu.instaounce.fragments;



import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.neko2mizu.instaounce.PostsAdapter;
import com.neko2mizu.instaounce.R;
import com.neko2mizu.instaounce.objects.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class HomePostFragment extends Fragment {

    private static final String TAG = "HomePostFragment";
    protected RecyclerView rvPosts;
    protected SwipeRefreshLayout swipeContainer;


    protected PostsAdapter adapter;


    public HomePostFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceStale){
        return inflater.inflate(R.layout.fragment_home_post, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPosts);

        swipeContainer = view.findViewById(R.id.swipeContainer);

        adapter = new PostsAdapter(getContext(), new ArrayList<Post>());


        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        newQueryPosts();


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newQueryPosts();
                swipeContainer.setRefreshing(false);
            }
        });
    }


    protected void newQueryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null){
                    Log.e(TAG,"Issue with getting posts",e);
                    return;
                }
                for (Post post : posts)
                {
                    Log.i(TAG, "Post:" + post.getDescription() + ", username: "+ post.getUser().getUsername());
                }

                adapter.clear();
                adapter.addAll(posts);
            }
        });
    }

}