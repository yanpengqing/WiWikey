package com.wiwikeyandroid.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	// 继承SQLiteOpenHelper

	// 数据库版本号
	private static final int DATABASE_VERSION = 1;
	// 数据库名称
	private static final String DATABASE_NAME = "contacts.db";
	// 数据表名
	public static final String TABLE_NAME = "personInfos";

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);

	}
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// 调用时间：数据库第一次创建时onCreate()方法会被调用
		// onCreate方法有一个 SQLiteDatabase对象作为参数，根据需要对这个对象填充表和初始化数据
		// 这个方法中主要完成创建数据库后对数据库的操作
		StringBuffer sBuffer = new StringBuffer();

		sBuffer.append("CREATE TABLE [" + TABLE_NAME + "] (");
		sBuffer.append("[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
		sBuffer.append("[contact_id] INTEGER,");
		sBuffer.append("[number] TEXT,");
		sBuffer.append("[name] TEXT,");
		sBuffer.append("[date] INTEGER,");
		sBuffer.append("[duration] INTEGER,");
		sBuffer.append("[mold] INTEGER,");
		sBuffer.append("[type] INTEGER,");
		sBuffer.append("[picUrl] TEXT)");
		// 执行创建表的SQL语句
		db.execSQL(sBuffer.toString());

		// 即便程序修改重新运行，只要数据库已经创建过，就不会再进入这个onCreate方法
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 更改数据库版本的操作
		//db.execSQL("DROPB TABLE IF EXISTS" + TABLE_NAME);
		//onCreate(db);
	}
}
