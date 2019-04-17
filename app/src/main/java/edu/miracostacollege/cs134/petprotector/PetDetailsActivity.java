package edu.miracostacollege.cs134.petprotector;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import edu.miracostacollege.cs134.petprotector.Model.Pet;

/**
 * This controller class handles the behavior of the Pet Details Activity, which takes in an intent
 * containing a Pet object and displays that pet's information to the user.
 *
 * @author William Craycroft
 * @version 1.0
 */
public class PetDetailsActivity extends AppCompatActivity {

    /**
     * Inflates the activity_pet_details layout, links views, and displays pet information from intent.
     *
     * @param savedInstanceState - Bundle of data saved from previous state (unused)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);

        ImageView petDetailsImageView = findViewById(R.id.petDetailsImageView);
        TextView petDetailsNameTextView = findViewById(R.id.petDetailsNameTextView);
        TextView petDetailsDescriptionTextView = findViewById(R.id.petDetailsDescriptionTextView);
        TextView petDetailsPhoneNumberTextView = findViewById(R.id.petDetailsPhoneNumberTextView);

        Intent detailsIntent = getIntent();
        // Retrieve pet object from intent
        Pet pet = detailsIntent.getParcelableExtra("Selected Pet");
        // Set ImageView
        petDetailsImageView.setImageURI(Uri.parse(pet.getImageURI()));
        // Set TextViews, with formatted phone number if applicable
        petDetailsNameTextView.setText(pet.getName());
        petDetailsDescriptionTextView.setText(pet.getDetails());
        petDetailsPhoneNumberTextView.setText(PhoneNumberUtils.formatNumber(pet.getPhone(),
                Locale.getDefault().getCountry()));
    }
}
