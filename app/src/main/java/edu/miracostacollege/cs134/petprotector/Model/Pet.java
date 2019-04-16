package edu.miracostacollege.cs134.petprotector.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The <code>Pet</code> class maintains information about a video pet,
 * including its id number, name, description, rating and image name.
 *
 * @author Will Craycroft
 */
public class Pet implements Parcelable {

    //Member variables
    private long mId;
    private String mName;
    private String mDescription;
    private String mPhone;
    private String mImageURI;

    /**
     * Creates a default <code>Pet</code> with an id of -1, empty description,
     * empty phone number and default image name of none.png.
     */
    public Pet()
    {
        this(-1, "", "", "", "android.resource://edu.miracostacollege.cs134.petprotector/drawable/none");
    }

    /**
     * Creates a new <code>Pet</code> from its id, description and status.
     * @param description The pet description
     * @param phone The pet phone (out of 5.0)
     */
    public Pet(String name, String description, String phone) {
        this(-1, name, description, phone, "none.png");
    }

    /**
     * Creates a new <code>Pet</code> from its id, description and status.
     * @param description The pet description
     * @param phone The pet phone (out of 5.0)
     * @param imageURI The image file name of the pet
     */
    public Pet(String name, String description, String phone, String imageURI) {
        this(-1, name, description, phone, imageURI);
    }

    /**
     * Creates a new <code>Pet</code> from its id, description and status.
     * @param id The pet id
     * @param description The pet description
     * @param phone The pet phone (out of 5.0)
     * @param imageURI The image file name of the pet
     */
    public Pet(long id, String name, String description, String phone, String imageURI) {
        mId = id;
        mName = name;
        mDescription = description;
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
     * @return The pet name
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
     * Gets the description of the <code>Pet</code>.
     * @return The pet's description
     */
    public String getDescription () {
        return mDescription;
    }

    /**
     * Sets the description of the <code>Pet</code>.
     * @param desc The pet's description
     */
    public void setDescription (String desc) {
        mDescription = desc;
    }

    /**
     * Gets the phone number for the <code>Pet</code>.
     * @return The phone number for the pet.
     */
    public String getPhone() {
        return mPhone;
    }

    /**
     * Sets the phone of the <code>Pet</code>.
     * @param phone The phone (number of stars) of the pet.
     */
    public void setPhone(String phone) {
        mPhone = phone;
    }

    /**
     * Gets the image file name of the <code>Pet</code>.
     * @return The image file name (e.g. lol.png) of the pet.
     */
    public String getImageURI() {
        return mImageURI;
    }

    /**
     * Sets the image file name of the <code>Pet</code>.
     * @param imageURI The image file name (e.g. lol.png) of the pet.
     */
    public void setImageURI(String imageURI) {
        mImageURI = imageURI;
    }

    /**
     * A method for displaying a <code>Pet</code> as a String in the form:
     *
     * Pet{id=1, Name=League of Legends, Description=Multiplayer online battle arena pet,
     * Rating=4.5, ImageName=lol.png}
     *
     * @return The formatted String
     */
    @Override
    public String toString() {
        return "Pet{" +
                "Id=" + mId +
                ", Name='" + mName + '\'' +
                ", Description='" + mDescription + '\'' +
                ", Rating=" + mPhone +
                ", ImageName='" + mImageURI + '\'' +
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(mId);
        dest.writeString(mName);
        dest.writeString(mDescription);
        dest.writeString(mPhone);
        dest.writeString(mImageURI);

    }

    // Mechanism to create a new Pet object from a Parcel
    // Private constructor to create a new Pet from Parcel
    private Pet(Parcel parcel)
    {
        mId = parcel.readLong();
        mName = parcel.readString();
        mDescription = parcel.readString();
        mPhone = parcel.readString();
        mImageURI = parcel.readString();

    }

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