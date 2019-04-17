package edu.miracostacollege.cs134.petprotector.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * This class helps create and maintain a database of Pets, storing their name, description,
 * phone number and image URI.
 *
 * @author William Craycroft
 * @version 1.0
 */
public class DBHelper extends SQLiteOpenHelper {

    // Database name, Table name and version
    public static final String DATABASE_NAME = "PetProtector";
    private static final String DATABASE_TABLE = "Pets";
    private static final int DATABASE_VERSION = 1;

    // Field names
    private static final String KEY_FIELD_ID = "_id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_PHONE_NUMBER = "phone_number";
    private static final String FIELD_IMAGE_NAME = "image_name";

    /**
     * Creates a new DBHelper given its context.
     * @param context - The calling activity
     */
    public DBHelper(Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates a new Database if one does not currently exist.
     * @param database - the SQLiteDatabase object
     */
    @Override
    public void onCreate (SQLiteDatabase database){
        String table = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + "("
                + KEY_FIELD_ID + " INTEGER PRIMARY KEY, "
                + FIELD_NAME + " TEXT, "
                + FIELD_DESCRIPTION + " TEXT, "
                + FIELD_PHONE_NUMBER + " REAL, "
                + FIELD_IMAGE_NAME + " TEXT" + ")";
        database.execSQL (table);
    }

    /**
     * Upgradres the database to a newer version. Drops and recreates the table.
     * @param database - reference to the SQLiteDatabase object
     * @param oldVersion - the old version ID
     * @param newVersion - the new version ID
     */
    @Override
    public void onUpgrade(SQLiteDatabase database,
                          int oldVersion,
                          int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(database);
    }

    /**
     * Adds a new Pet to the database and updates its id in the model.
     * @param pet - the Pet object to be added
     */
    public void addPet(Pet pet) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        // Add key-value pairs for each member variable
        values.put(FIELD_NAME, pet.getName());
        values.put(FIELD_DESCRIPTION, pet.getDetails());
        values.put(FIELD_PHONE_NUMBER, pet.getPhone());
        values.put(FIELD_IMAGE_NAME, pet.getImageURI());

        // Insert new row
        long id = db.insert(DATABASE_TABLE, null, values);

        // Set the pet id to the assigned primary key.
        pet.setId(id);

        // Close database
        db.close();
    }

    /**
     * Returns a list of all Pets currently in the database
     * @return List of all pets
     */
    public List<Pet> getAllPets() {
        List<Pet> petList = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        // A cursor is the results of a database query (what gets returned)
        Cursor cursor = database.query(
                DATABASE_TABLE,
                new String[]{KEY_FIELD_ID, FIELD_NAME, FIELD_DESCRIPTION, FIELD_PHONE_NUMBER, FIELD_IMAGE_NAME},
                null,
                null,
                null, null, null, null );

        // Loop through table, add new Pet object to list
        if (cursor.moveToFirst()){
            do {
                Pet pet =
                        new Pet(cursor.getLong(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4));
                petList.add(pet);
            } while (cursor.moveToNext());
        }
        // Close DB
        cursor.close();
        database.close();
        // Return list
        return petList;
    }

    /**
     * Deletes all pets from the current database by deleting the current table (used for debugging)
     */
    public void deleteAllPets()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DATABASE_TABLE, null, null);
        db.close();
    }

}