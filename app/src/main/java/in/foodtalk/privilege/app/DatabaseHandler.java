package in.foodtalk.privilege.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

import in.foodtalk.privilege.models.LoginValue;

/**
 * Created by RetailAdmin on 18-05-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "foodtalkDb";

    private static final String TABLE_LOGIN = "loginInfo";

    private static final String KEY_ID = "id";
    private static final String KEY_SID = "sId";
    private static final String KEY_RTOKEN = "rToken";
    private static final String KEY_USERID = "uId";
    private static final String KEY_CREATE_AT = "createAt";
    private static final String KEY_UPDATE_AT = "updateAt";

    private static final String KEY_NAME = "uName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_DOB = "dob";
    private static final String KEY_PREF = "pref";

    private static final String KEY_SUB_ID = "subId";
    private static final String KEY_SUB_CITY_ID = "subCityId";
    private static final String KEY_SUB_EXPIRY = "subExpiry";
    private static final String KEY_SUB_CREATED = "subCreated";

    String TAG = DatabaseHandler.class.getSimpleName();
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_SID + " TEXT,"
                + KEY_RTOKEN + " TEXT,"
                + KEY_CREATE_AT + " TEXT,"
                + KEY_UPDATE_AT + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_GENDER + " TEXT,"
                + KEY_DOB + " TEXT,"
                //+ KEY_PREF + " TEXT"
                //+ KEY_SUB_ID + " TEXT"
                //+ KEY_SUB_CITY_ID + " TEXT"
                //+ KEY_SUB_EXPIRY + " TEXT"
                //+ KEY_SUB_CREATED + " TEXT"
                + KEY_USERID +" TEXT"+ ")";
        db.execSQL(CREATE_LOGIN_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //subCityId=1 dob= createAt=null sId=8ff71b90934544cb0eb1657168d025c74abf1404 phone=8383083893 email=android@foodtalkindia.com gender= uName=Mandeep singh subCreated=2017-06-16 10:46:04 subId=21 rToken=cd1a665be738f9669c1aef5314cfb14428a71906 pref=null subExpiry=2018-06-15 23:59:59 uId=null

    // adding new credantial to login table
    public void addUser(LoginValue loginValue){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SID, loginValue.sId);
        values.put(KEY_RTOKEN, loginValue.rToken);
        values.put(KEY_USERID, loginValue.uId);
        values.put(KEY_CREATE_AT, loginValue.updateAt);
        values.put(KEY_USERID, loginValue.updateAt);
        values.put(KEY_NAME, loginValue.name);
        values.put(KEY_EMAIL, loginValue.email);
        values.put(KEY_GENDER, loginValue.gender);
        values.put(KEY_DOB, loginValue.dob);
        values.put(KEY_PHONE, loginValue.phone);
        //values.put(KEY_PREF, loginValue.pref);

       // values.put(KEY_SUB_ID, loginValue.subId);
        //values.put(KEY_SUB_CITY_ID, loginValue.subCityId);
        //values.put(KEY_SUB_EXPIRY, loginValue.subExpiry);
        //values.put(KEY_SUB_CREATED, loginValue.subCreated);

        db.insert(TABLE_LOGIN, null, values);
        db.close();

        Log.d(TAG, "user added");
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0){

            user.put("subId", cursor.getString(cursor.getColumnIndex(KEY_SUB_ID)));
            user.put("subCityId", cursor.getString(cursor.getColumnIndex(KEY_SUB_CITY_ID)));
            user.put("subExpiry", cursor.getString(cursor.getColumnIndex(KEY_SUB_EXPIRY)));
            user.put("subCreated", cursor.getString(cursor.getColumnIndex(KEY_SUB_CREATED)));

            user.put("sessionId", cursor.getString(cursor.getColumnIndex(KEY_SID)));
            user.put("refreshToken", cursor.getString(cursor.getColumnIndex(KEY_RTOKEN)));
            user.put("userId", cursor.getString(cursor.getColumnIndex(KEY_USERID)));
            user.put("createAt", cursor.getString(cursor.getColumnIndex(KEY_CREATE_AT)));
            user.put("updateAt", cursor.getString(cursor.getColumnIndex(KEY_UPDATE_AT)));
            user.put("name", cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            user.put("email", cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            user.put("gender", cursor.getString(cursor.getColumnIndex(KEY_GENDER)));
            user.put("dob", cursor.getString(cursor.getColumnIndex(KEY_DOB)));
            user.put("phone", cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            user.put("pref", cursor.getString(cursor.getColumnIndex(KEY_PREF)));
        }
        cursor.close();
        db.close();
        return user;
    }

    public int getRowCount(){
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }

    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }
}
