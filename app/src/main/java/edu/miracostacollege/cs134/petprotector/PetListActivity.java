package edu.miracostacollege.cs134.petprotector;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.miracostacollege.cs134.petprotector.Model.DBHelper;
import edu.miracostacollege.cs134.petprotector.Model.Pet;

/**
 * This controller class handles the behavior of the Pet List Activity, which allows a user to add
 * a pet to the database. It also allows the user to select an image from external storage.
 *
 * @author William Craycroft
 * @version 1.0
 */
public class PetListActivity extends AppCompatActivity {

    public static final int RESULT_LOAD_IMAGE = -1;

    public static final String TAG = PetListActivity.class.getSimpleName();
    private Uri defaultImageURI;
    private DBHelper db;
    private List<Pet> petList;
    private PetListAdapter petListAdapter;
    private ListView petListView;
    private ImageView petImageView;

    /**
     * Inflates the activity_pet_list layout, links views, and get pet information from database.
     *
     * @param savedInstanceState - Bundle of data saved from previous state (unused)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);

        defaultImageURI = getUriToResource(this, R.drawable.none);

        petImageView = findViewById(R.id.petImageView);
        petImageView.setImageURI(defaultImageURI);
        petImageView.setTag(defaultImageURI);
        db = new DBHelper(this);


        petList = db.getAllPets();
        petListAdapter = new PetListAdapter(this, R.layout.pet_list_item, petList);

        petListView = findViewById(R.id.petListView);
        petListView.setAdapter(petListAdapter);
    }

    // Helper method which returns the passed resource id as a URI String
    private static Uri getUriToResource(Context context, int id) {
        Resources res = context.getResources();
        String uri = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + res.getResourcePackageName(id) + "/"
                + res.getResourceTypeName(id) + "/"
                + res.getResourceEntryName(id);

        //Log.i(PetListActivity.TAG, uri);

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

        permsList = getDeniedPermsList(requiredPerms);

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

        //Log.i(TAG, "After super call. Result code: " + resultCode);
        if (resultCode == RESULT_LOAD_IMAGE) {
            Uri uri = data.getData();
            petImageView.setImageURI(uri);
            Log.i(TAG, "Attempted to set image to URI: " + uri.toString());
            petImageView.setTag(uri);
        }
    }

    // Helper method which check if all permissions in the passed list are allow and returns a list
    // of permissions that are not.
    private List<String> getDeniedPermsList(List<String> requiredPermissions) {

        List<String> permsList = new ArrayList<>();
        // Loop through all required permissions
        for (int i = 0; i < requiredPermissions.size(); i++) {
            String perm = requiredPermissions.get(i);
            // This does NOT ask the user for permissions
            int hasPerm = ContextCompat.checkSelfPermission(this, perm);
            // If permission is denied, add it to denied list
            if (hasPerm == PackageManager.PERMISSION_DENIED)
                permsList.add(perm);
        }
        // Return denied permissions list
        return permsList;
    }

    public void viewPetDetails(View view) {
        Pet selectedPet = (Pet) view.getTag();

        Intent detailsIntent = new Intent(this, PetDetailsActivity.class);
        detailsIntent.putExtra("Selected Pet", selectedPet);

        startActivity(detailsIntent);
    }

    public void addPet(View view)
    {
        //db.deleteAllPets();

        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText descriptionEditText = findViewById(R.id.descriptionEditText);
        EditText phoneNumberEditText = findViewById(R.id.phoneNumberEditText);

        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String uri = petImageView.getTag().toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Name of the pet must be provided.", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(description))
        {
            Toast.makeText(this, "Details for the pet must be provided.", Toast.LENGTH_LONG).show();
            return;
        }
        if ( TextUtils.isEmpty(phoneNumber))
        {
            Toast.makeText(this, "Phone number for the pet must be provided.", Toast.LENGTH_LONG).show();
            return;
        }
        Pet newPet = new Pet(name, description, phoneNumber, uri);

        // Add the new pet to the database to ensure it is persisted.
        db.addPet(newPet);
        petListAdapter.add(newPet);
        // Clear all the entries (reset them)
        nameEditText.setText("");
        descriptionEditText.setText("");
        phoneNumberEditText.setText("");
        petImageView.setImageURI(defaultImageURI);
        petImageView.setTag(defaultImageURI);
    }


}
