package edu.miracostacollege.cs134.petprotector.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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

    // Constructor
    public DBHelper(Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          int oldVersion,
                          int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(database);
    }

    //********** DATABASE OPERATIONS:  ADD, UPDATE, EDIT, DELETE

    public void addPet(Pet pet) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        //ADD KEY-VALUE PAIR INFORMATION FOR THE PET NAME
        values.put(FIELD_NAME, pet.getName());

        //ADD KEY-VALUE PAIR INFORMATION FOR THE PET DESCRIPTION
        values.put(FIELD_DESCRIPTION, pet.getDescription());

        //ADD KEY-VALUE PAIR INFORMATION FOR THE PET RATING
        values.put(FIELD_PHONE_NUMBER, pet.getPhone());

        //ADD KEY-VALUE PAIR INFORMATION FOR THE PET RATING
        values.put(FIELD_IMAGE_NAME, pet.getImageURI());

        // INSERT THE ROW IN THE TABLE
        long id = db.insert(DATABASE_TABLE, null, values);

        // UPDATE THE PET WITH THE NEWLY ASSIGNED ID FROM THE DATABASE
        pet.setId(id);

        // CLOSE THE DATABASE CONNECTION
        db.close();
    }

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

        //COLLECT EACH ROW IN THE TABLE
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
        cursor.close();
        database.close();
        return petList;
    }

    public void deletePet(Pet pet){
        SQLiteDatabase db = getWritableDatabase();

        // DELETE THE TABLE ROW
        db.delete(DATABASE_TABLE, KEY_FIELD_ID + " = ?",
                new String[] {String.valueOf(pet.getId())});
        db.close();
    }

    public void deleteAllPets()
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DATABASE_TABLE, null, null);
        db.close();
    }

    public void updatePet(Pet pet){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FIELD_NAME, pet.getName());
        values.put(FIELD_DESCRIPTION, pet.getDescription());
        values.put(FIELD_PHONE_NUMBER, pet.getPhone());
        values.put(FIELD_IMAGE_NAME, pet.getImageURI());

        db.update(DATABASE_TABLE, values, KEY_FIELD_ID + " = ?",
                new String[]{String.valueOf(pet.getId())});
        db.close();
    }

    public Pet getPet(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                DATABASE_TABLE,
                new String[]{KEY_FIELD_ID, FIELD_NAME, FIELD_DESCRIPTION, FIELD_PHONE_NUMBER, FIELD_IMAGE_NAME},
                KEY_FIELD_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null );

        Pet pet = null;
        if (cursor != null) {
            cursor.moveToFirst();

            pet = new Pet(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4));

            cursor.close();
        }
        db.close();
        return pet;
    }

}