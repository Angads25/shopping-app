package com.rgretail.grocermax.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rgretail.grocermax.bean.Product;
import com.rgretail.grocermax.utils.Constants.DatabaseConstant;

import java.util.ArrayList;

public class DataHandler {

	public static final int Version = 7;

	private static final String createTableMessageAlert = "create table if not exists "
			+ DatabaseConstant.T_MESSAGE_FREQUENCY
			+ "("
			+ DatabaseConstant.C_MSG_ID
			+ " text, "
			+ DatabaseConstant.C_MSG_TIME + " text" + ");";


	private static final String createTableSearchKeyword = "create table if not exists "
			+ DatabaseConstant.T_SEARCH_KEYWORD
			+ "("
			+ DatabaseConstant.C_KEYWORD_ID
			+ " text, "
			+ DatabaseConstant.C_KEYWORD
			+ " text primary key, "
			+ DatabaseConstant.C_KEYWORD_STATUS + " text" + ");";



	Context ctx;
	DataBaseHelper dhelper;
	SQLiteDatabase db;

	public DataHandler(Context ctx) {
		this.ctx = ctx;
		dhelper = new DataBaseHelper(ctx);
	}

	public static class DataBaseHelper extends SQLiteOpenHelper {

		public DataBaseHelper(Context ctx) {
			super(ctx, DatabaseConstant.DB_GROCERMAX_ALERT, null, Version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				createDb(db);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			try {
				try {
					db.execSQL("drop table "+ DatabaseConstant.T_MESSAGE_FREQUENCY);
					db.execSQL("drop table "+ DatabaseConstant.T_SEARCH_KEYWORD);
				} catch (Exception e) {

				}
				try {
					createDb(db);
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} catch (SQLException e) {

			}

		}

		void createDb(SQLiteDatabase db) {
			db.execSQL(createTableMessageAlert);
			db.execSQL(createTableSearchKeyword);
		}

	}

	public DataHandler Open() {
		db = dhelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dhelper.close();
	}

    public void insertDataInTable(String table,ContentValues contentValues){
        db.insert(table, null, contentValues);
    }

    public Cursor getMessageFrequencyBasedOnMessageId(String message_id){

        Cursor cr=db.rawQuery("select * from "+DatabaseConstant.T_MESSAGE_FREQUENCY+" where "+DatabaseConstant.C_MSG_ID+"='"+message_id+"' ",null);
        return  cr;
    }
    public void updateTime(String message_id,String time){
        String updateQuery="update "+DatabaseConstant.T_MESSAGE_FREQUENCY+" set "+DatabaseConstant.C_MSG_TIME+"= '"+time+"' where "+DatabaseConstant.C_MSG_ID+" ='"+message_id+"'";
        db.execSQL(updateQuery);
    }

	public ArrayList<Product>  getAllSearchKeys(){
		ArrayList<Product> manuallSuggestList=new ArrayList<>();
		Cursor cr=db.rawQuery("select * from "+DatabaseConstant.T_SEARCH_KEYWORD+" order by "+DatabaseConstant.C_KEYWORD_STATUS+" desc",null);
		while(cr.moveToNext()){
			Product p=new Product(cr.getString(cr.getColumnIndex(DatabaseConstant.C_KEYWORD)));
			manuallSuggestList.add(p);
		}
		return  manuallSuggestList;
	}

	public void insertDataInSearch(String table,ContentValues contentValues){
		try {
			//db.insertWithOnConflict(table,null,contentValues,SQLiteDatabase.CONFLICT_IGNORE); // 5 for INSERT OR REPLACE in sqlite library
			//db.insertOrThrow(table, null, contentValues);
			db.insert(table,null,contentValues);
		} catch (SQLException e) {

		}
	}






}
