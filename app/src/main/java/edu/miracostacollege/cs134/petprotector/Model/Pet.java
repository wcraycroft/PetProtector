package edu.miracostacollege.cs134.petprotector.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The <code>Pet</code> class holds information a pet up for adoption,
 * including its id number, name, details, phone number and image URI.
 *
 * @author Will Craycroft
 */
public class Pet implements Parcelable {

    //Member variables
    private long mId;
    private String mName;
    private String mDetails;
    private String mPhone;
    private String mImageURI;

    /**
     * Creates a new <code>Pet</code> from its name, details, phone number and image URI.
     * @param name The pet's name
     * @param details The pet details
     * @param phone The phone number associated with the pet
     * @param imageURI The image file URI for the pet
     */
    public Pet(String name, String details, String phone, String imageURI) {
        this(-1, name, details, phone, imageURI);
    }

    /**
     * Creates a new <code>Pet</code> from its id, name, details, phone number and image URI.
     * @param id The pet id
     * @param name The pet's name
     * @param details The pet details
     * @param phone The phone number associated with the pet
     * @param imageURI The image file URI for the pet
     */
    public Pet(long id, String name, String details, String phone, String imageURI) {
        mId = id;
        mName = name;
        mDetails = details;
        mPhone = phone;
        mImageURI = imageURI;
    }

    /**
     * Gets the unique id of the <code>Pet</code>.
     * @return The unique id
     */
    public long getId() {
        return mId;
    }

    /**
     * Sets the unique id of the <code>Pet</code>.
     * @param id The unique id
     */
    public void setId(long id) {
        mId = id;
    }

    /**
     * Gets the name of the <code>Pet</code>.
     * @return The pet's name
     */
    public String getName() {
        return mName;
    }

    /**
     * Sets the name of the <code>Pet</code>.
     * @param name The new pet name.
     */
    public void setName(String name) {
        mName = name;
    }

    /**
     * Gets the details of the <code>Pet</code>.
     * @return The pet's details
     */
    public String getDetails() {
        return mDetails;
    }

    /**
     * Sets the details of the <code>Pet</code>.
     * @param details The pet's details
     */
    public void setDetails(String details) {
        mDetails = details;
    }

    /**
     * Gets the phone number for the <code>Pet</code>.
     * @return The phone number associated with the pet
     */
    public String getPhone() {
        return mPhone;
    }

    /**
     * Sets the phone number for the <code>Pet</code>.
     * @param phone The phone number associated with the pet
     */
    public void setPhone(String phone) {
        mPhone = phone;
    }

    /**
     * Gets the image file URI of the <code>Pet</code>.
     * @return The image file URI of the pet.
     */
    public String getImageURI() {
        return mImageURI;
    }

    /**
     * Sets the image file URI of the <code>Pet</code>.
     * @param imageURI The image file URI of the pet.
     */
    public void setImageURI(String imageURI) {
        mImageURI = imageURI;
    }

    /**
     * A method for displaying a <code>Pet</code> as a String in the form:
     *
     * Pet{id=1, Name=Fido, Details=Chases his tail, Phone=5554420093,
     * ImageURI=android.resource://edu.miracostacollege.cs134.petprotector/drawable/fido}
     *
     * @return The formatted String
     */
    @Override
    public String toString() {
        return "Pet{" +
                "Id=" + mId +
                ", Name='" + mName + '\'' +
                ", Details='" + mDetails + '\'' +
                ", Phone=" + mPhone +
                ", ImageURI='" + mImageURI + '\'' +
                '}';
    }

    /**
     * Describe any special contents of this object (Files, video, audio)
     * @return Zero, unless there are any special contents
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the current Pet object into a passed Parcel object
     *
     * @param dest - the Parcel object to be written to
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(mId);
        dest.writeString(mName);
        dest.writeString(mDetails);
        dest.writeString(mPhone);
        dest.writeString(mImageURI);

    }

    /**
     * Creates a new <code>Pet</code> from a passed Parcel object
     *
     * @param parcel - Parcel containing Pet data
     */
    private Pet(Parcel parcel)
    {
        mId = parcel.readLong();
        mName = parcel.readString();
        mDetails = parcel.readString();
        mPhone = parcel.readString();
        mImageURI = parcel.readString();

    }

    /**
     * Overrides Parcelable.Creator so that it calls the parcel constructor of this class
     */
    public static final Parcelable.Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel source) {
            return new Pet(source);
        }

        // Allows the creation of an array of Pet objects from JSON file.
        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };
}