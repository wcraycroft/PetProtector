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

    // Constants
    public static final int RESULT_LOAD_IMAGE = -1;
    public static final String TAG = PetListActivity.class.getSimpleName();
    // Declarations
    private Uri defaultImageURI;
    private DBHelper db;
    private List<Pet> petList;
    private PetListAdapter petListAdapter;
    // View objects
    private ListView petListView;
    private ImageView petImageView;
    EditText nameEditText;
    EditText descriptionEditText;
    EditText phoneNumberEditText;

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

        // Link views
        petImageView = findViewById(R.id.petImageView);
        petImageView.setImageURI(defaultImageURI);
        petImageView.setTag(defaultImageURI);
        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        petListView = findViewById(R.id.petListView);

        // Instantiate Database
        db = new DBHelper(this);

        // Load initial Pets from database, create new ListAdapter and link it
        petList = db.getAllPets();
        petListAdapter = new PetListAdapter(this, R.layout.pet_list_item, petList);
        petListView.setAdapter(petListAdapter);
    }

    /**
     * Helper method which returns the passed resource id as a URI object
     *
     * @param context - The context linked to the resource
     * @param id - The id of the target resource
     * @return The formatted Uri object pointing to the resource
     */
    private static Uri getUriToResource(Context context, int id) {
        Resources res = context.getResources();
        String uri = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + res.getResourcePackageName(id) + "/"
                + res.getResourceTypeName(id) + "/"
                + res.getResourceEntryName(id);

        return Uri.parse(uri);
    }

    /**
     * Prompts the user for any missing permissions (Camera, storage). Then, opens the gallery
     * to retrieve an image resource.
     *
     * @param v - Reference to the calling ImageView object
     */
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

    /**
     * Override onActivityResult to find out what the user picked
     *
     * @param requestCode - the request code indicating the call type
     * @param resultCode - the code indicating a successful load
     * @param data - the intent containing the loaded image
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_LOAD_IMAGE) {
            Uri uri = data.getData();
            petImageView.setImageURI(uri);
            Log.i(TAG, "Attempted to set image to URI: " + uri.toString());
            petImageView.setTag(uri);
        }
    }

    /**
     * Helper method which check if all permissions in the passed list are allow and returns a list
     * of permissions that are not.
     *
     * @param requiredPermissions - the list of required permissions for this app
     * @return The list of denied permissions after prompting the user
     */
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

    /**
     * After the user clicks a pet in the list, get the Pet object from the list item tag,
     * save that Pet information as a parcelable object and send it to a new PetDetailsActivity.
     *
     * @param view - Reference to the calling Button object
     */
    public void viewPetDetails(View view) {
        // Get Pet object sent in list item object's tag property
        Pet selectedPet = (Pet) view.getTag();
        // Instantiate details intent
        Intent detailsIntent = new Intent(this, PetDetailsActivity.class);
        detailsIntent.putExtra("Selected Pet", selectedPet);
        // Launch activity
        startActivity(detailsIntent);
    }

    /**
     * Validates EditText input, adds the new Pet to the database, and updates the Pet List
     * @param view - Reference to the calling Button object
     */
    public void addPet(View view)
    {
        // Store input from EditTexts
        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String uri = petImageView.getTag().toString();

        // Check if any fields are empty, show error Toast and return if they are.
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
        // Create new Pet object from inputs
        Pet newPet = new Pet(name, description, phoneNumber, uri);

        // Add the new pet to the database to ensure it is persisted.
        db.addPet(newPet);
        // Update List
        petListAdapter.add(newPet);
        // Clear all EditText views
        nameEditText.setText("");
        descriptionEditText.setText("");
        phoneNumberEditText.setText("");
        // Reset pet image
        petImageView.setImageURI(defaultImageURI);
        petImageView.setTag(defaultImageURI);
    }


}
