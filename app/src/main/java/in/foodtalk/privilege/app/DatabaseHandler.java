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
                + KEY_USERID +" TEXT"+ ")";
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    // adding new credantial to login table
    public void addUser(LoginValue loginValue){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SID, loginValue.sId);
        values.put(KEY_RTOKEN, loginValue.rToken);
        values.put(KEY_USERID, loginValue.uId);
        values.put(KEY_CREATE_AT, loginValue.updateAt);
        values.put(KEY_USERID, loginValue.updateAt);


        db.insert(TABLE_LOGIN, null, values);
        db.close();

        Log.d(TAG, "add user");
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0){
            user.put("sessionId", cursor.getString(cursor.getColumnIndex(KEY_SID)));
            user.put("sessionId", cursor.getString(cursor.getColumnIndex(KEY_RTOKEN)));
            user.put("sessionId", cursor.getString(cursor.getColumnIndex(KEY_USERID)));
            user.put("sessionId", cursor.getString(cursor.getColumnIndex(KEY_CREATE_AT)));
            user.put("sessionId", cursor.getString(cursor.getColumnIndex(KEY_UPDATE_AT)));
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
