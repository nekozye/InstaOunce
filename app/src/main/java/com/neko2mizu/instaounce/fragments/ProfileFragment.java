package com.neko2mizu.instaounce.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.neko2mizu.instaounce.PostsAdapter;
import com.neko2mizu.instaounce.R;
import com.neko2mizu.instaounce.objects.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends HomePostFragment {

    private final String TAG = "ProfileFragment";


    public ProfileFragment()
    {

    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPosts);

        swipeContainer = view.findViewById(R.id.swipeContainer);

        adapter = new PostsAdapter(getContext(), new ArrayList<Post>());


        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new GridLayoutManager(getContext(),2));

        newQueryPosts();


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newQueryPosts();
                swipeContainer.setRefreshing(false);
            }
        });
    }



    @Override
    protected void newQueryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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