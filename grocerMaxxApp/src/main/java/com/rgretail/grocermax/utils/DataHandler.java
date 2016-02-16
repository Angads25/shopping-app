package com.rgretail.grocermax.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rgretail.grocermax.utils.Constants.DatabaseConstant;

public class DataHandler {

	public static final int Version = 2;

	private static final String createTableMessageAlert = "create table if not exists "
			+ DatabaseConstant.T_MESSAGE_FREQUENCY
			+ "("
			+ DatabaseConstant.C_MSG_ID
			+ " text, "
			+ DatabaseConstant.C_MSG_TIME + " text" + ");";

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
        db.insert(table,null,contentValues);
    }

    public Cursor getMessageFrequencyBasedOnMessageId(String message_id){

        Cursor cr=db.rawQuery("select * from "+DatabaseConstant.T_MESSAGE_FREQUENCY+" where "+DatabaseConstant.C_MSG_ID+"='"+message_id+"' ",null);
        return  cr;
    }
    public void updateTime(String message_id,String time){
        String updateQuery="update "+DatabaseConstant.T_MESSAGE_FREQUENCY+" set "+DatabaseConstant.C_MSG_TIME+"= '"+time+"' where "+DatabaseConstant.C_MSG_ID+" ='"+message_id+"'";
        db.execSQL(updateQuery);
    }


}
