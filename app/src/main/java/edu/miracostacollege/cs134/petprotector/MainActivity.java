package edu.miracostacollege.cs134.petprotector;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.MediaActionSound;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int RESULT_LOAD_IMAGE = 200;

    private ImageView petImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        petImageView = findViewById(R.id.petImageView);
        petImageView.setImageURI(getUriToResource(this, R.drawable.none));
    }

    private static Uri getUriToResource(Context context, int id) {
        Resources res = context.getResources();
        String uri = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + res.getResourcePackageName(id) + "/"
                + res.getResourceTypeName(id) + "/"
                + res.getResourceEntryName(id);

        return Uri.parse(uri);
    }

    public void selectPetImage(View v) {
        // Make a list (empty) of permissions
        // As user grants them, add each permission to the list
        List<String> permsList;
        List<String> requiredPerms = new ArrayList<>();

        requiredPerms.add(Manifest.permission.CAMERA);
        requiredPerms.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        requiredPerms.add(Manifest.permission.READ_EXTERNAL_STORAGE);

        int permReqCode = 100;

        permsList = getPermsList(requiredPerms);

        // Ask user for for permissions
        if (permsList.size() > 0) {
            // Convert List into array
            String[] perms = new String[permsList.size()];
            permsList.toArray(perms);
            // Make the request to the user (backwards compatible
            ActivityCompat.requestPermissions(this, perms, permReqCode);
        }

        // Check to see if ALL permissions were granted
        if (permsList.size() == 0) {
            // Open the Gallery
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, permReqCode);
        }
        else {
            Toast.makeText(this, "Missing Permission to access Files or Camera.", Toast.LENGTH_SHORT).show();
        }

    }

    // Override onActivityResult to find out what user picked


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_LOAD_IMAGE) {
            Uri uri = data.getData();
            petImageView.setImageURI(uri);
        }
    }

    private List<String> getPermsList(List<String> requiredPermissions) {

        List<String> permsList = new ArrayList<>();
        int i = 0;
        while (requiredPermissions.size() > 0) {
            String perm = requiredPermissions.remove(i++);
            int hasPerm = ContextCompat.checkSelfPermission(this, perm);
            if (hasPerm == PackageManager.PERMISSION_DENIED)
                permsList.add(perm);
        }
        return permsList;
    }


}
