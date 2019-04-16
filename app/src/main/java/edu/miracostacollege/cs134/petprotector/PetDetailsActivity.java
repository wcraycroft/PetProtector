package edu.miracostacollege.cs134.petprotector;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import edu.miracostacollege.cs134.petprotector.Model.Pet;


public class PetDetailsActivity extends AppCompatActivity {

    private static final String TAG = PetDetailsActivity.class.getSimpleName();

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

        petDetailsImageView.setImageURI(Uri.parse(pet.getImageURI()));

        petDetailsNameTextView.setText(pet.getName());
        petDetailsDescriptionTextView.setText(pet.getDescription());
        petDetailsPhoneNumberTextView.setText(PhoneNumberUtils.formatNumber(pet.getPhone(),
                Locale.getDefault().getCountry()));
    }
}
