package edu.miracostacollege.cs134.petprotector;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.miracostacollege.cs134.petprotector.Model.Pet;


/**
 * Helper class to provide custom adapter for the <code>Pet</code> list.
 */
public class PetListAdapter extends ArrayAdapter<Pet> {

    private Context mContext;
    private List<Pet> mPetList = new ArrayList<>();
    private int mResourceId;



    /**
     * Creates a new <code>PetListAdapter</code> given a mContext, resource id and list of pets.
     *
     * @param c The mContext for which the adapter is being used (typically an activity)
     * @param rId The resource id (typically the layout file name)
     * @param pets The list of pets to display
     */
    public PetListAdapter(Context c, int rId, List<Pet> pets) {
        super(c, rId, pets);
        mContext = c;
        mResourceId = rId;
        mPetList = pets;
    }

    /**
     * Gets the view associated with the layout.
     * @param pos The position of the Pet selected in the list.
     * @param convertView The converted view.
     * @param parent The parent - ArrayAdapter
     * @return The new view with all content set.
     */
    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        final Pet selectedPet = mPetList.get(pos);

        LayoutInflater inflater =
                (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(mResourceId, null);

        LinearLayout petListLinearLayout =
                (LinearLayout) view.findViewById(R.id.petListLinearLayout);
        ImageView petListImageView =
                (ImageView) view.findViewById(R.id.petListImageView);
        TextView petListNameTextView =
                (TextView) view.findViewById(R.id.petListNameTextView);
        TextView petListDescriptionTextView =
                (TextView) view.findViewById(R.id.petListDescriptionTextView);

        // Set Tag as current pet object to be retrieves in PetListActivity
        petListLinearLayout.setTag(selectedPet);
        petListNameTextView.setText(selectedPet.getName());
        petListDescriptionTextView.setText(selectedPet.getDescription());
        petListImageView.setImageURI(Uri.parse(selectedPet.getImageURI()));

        return view;
    }
}
