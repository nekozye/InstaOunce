package com.neko2mizu.instaounce.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.neko2mizu.instaounce.R;
import com.neko2mizu.instaounce.objects.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;


public class ComposePostFragment extends Fragment {

    public static final String TAG = "ComposePostFragment";

    private ActivityResultLauncher<Intent> actreslauncher;

    private EditText etmlDescription;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private Button btnSubmit;
    private ProgressBar pbDurringPost;
    private String photoFileName = "photo.jpg";
    private File photoFile;

    public ComposePostFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceStale){
        return inflater.inflate(R.layout.fragment_compose_post, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        actreslauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // by this point we have the camera photo on disk
                            Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                            // RESIZE BITMAP, see section below
                            // Load the taken image into a preview
                            ImageView ivPreview = (ImageView) view.findViewById(R.id.ivPostImage);
                            ivPreview.setImageBitmap(takenImage);
                        }
                        else { // Result was a failure
                            Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



        etmlDescription = view.findViewById(R.id.etmlDescription);
        btnCaptureImage = view.findViewById(R.id.btnCaptureImage);
        ivPostImage = view.findViewById(R.id.ivPostImage);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        pbDurringPost = view.findViewById(R.id.pbDurringPost);



        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera(view);
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pbDurringPost.setVisibility(ProgressBar.VISIBLE);

                String description = etmlDescription.getText().toString();
                if (description.isEmpty()){
                    Toast.makeText(getContext(), "Description cannot be empty.", Toast.LENGTH_SHORT).show();
                    pbDurringPost.setVisibility(ProgressBar.INVISIBLE);
                    return;
                }
                if (photoFile == null || ivPostImage.getDrawable() == null){
                    Toast.makeText(getContext(), "There was no Image!", Toast.LENGTH_SHORT).show();
                    pbDurringPost.setVisibility(ProgressBar.INVISIBLE);
                    return;
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser,photoFile);
                pbDurringPost.setVisibility(ProgressBar.INVISIBLE);
                Toast.makeText(getContext(), "Posted!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void launchCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.neko2mizu.fileprovider",photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getContext().getPackageManager()) != null){
            actreslauncher.launch(intent);
        }

    }


    private File getPhotoFileUri(String fileName){
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if(!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }


    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error While Saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post Save was successful!");
                etmlDescription.setText("");
                ivPostImage.setImageResource(0);
            }
        });
    }


}