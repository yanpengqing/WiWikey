package com.wiwikeyandroid.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.wiwikeyandroid.modules.Contacts.model.Person;

public class DBManager {
	
	private DatabaseHelper helper;
	private SQLiteDatabase db;
	private Context context;
	
	public DBManager(Context context) {
		 Log.d("lalla", "DBManager-->Contructor");
		 helper = new DatabaseHelper(context);
		 db = helper.getWritableDatabase();
		 this.context = context;
	}
	
	 /**
     * 增
     * 
     * @param persons
     */
	
	 public void add(Person person){
		 Log.d("lallal", "DBManager --> add");
	        // 采用事务处理，确保数据完整性
		 db.beginTransaction();//开始事务
		 try{
			 
		
		 //for (Person person : persons) {
			db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME
					+ " VALUES(null,?,?,?,?,?,?,?,?)",new Object[]{person.getContact_id(),person.getNumber(),
					person.getName(),person.getDate(),person.getDuration(),person.getMold(),person.getType(),null});
		//}
		 db.setTransactionSuccessful();
		 }finally{
			 db.endTransaction();//结束事务
			 Uri notifyUri = Uri.parse("content://com.wiwikeyandroid");
			 context.getContentResolver().notifyChange(notifyUri, null);
		 }
	 }
	 
	 /**
	     * 查询所有
	     * 
	     * @return List<Person>
	     */
	 public List<Person> query(){
		 Log.d("lalal", "DBManager --> query");
		 ArrayList<Person> persons = new ArrayList<Person>();
		 Cursor cursor = queryTheCursor();
		 while(cursor.moveToNext()){
			 Person person = new Person();
			 person.setContact_id(cursor.getInt(cursor.getColumnIndex("contact_id")));
			 person.setPicUrl(cursor.getString(cursor.getColumnIndex("picUrl")));
			 person.setName(cursor.getString(cursor.getColumnIndex("name")));
			 person.setNumber(cursor.getString(cursor.getColumnIndex("number")));
			 person.setDate(cursor.getLong(cursor.getColumnIndex("date")));
			 person.setDuration(cursor.getInt(cursor.getColumnIndex("duration")));	
			 person.setMold(cursor.getInt(cursor.getColumnIndex("mold")));
			 person.setType(cursor.getInt(cursor.getColumnIndex("type")));
	         persons.add(person);
		 }
		 cursor.close();
		return persons;
		}
	 
	 /**
	     * 查询所有的记录，返回Cursor
	     * 
	     * @return Cursor
	     */
	 public Cursor queryTheCursor(){
		 Log.d("lallala", "DBManager --> queryTheCursor");
		 Cursor c =  db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME,
	                null);
		 return c;
				 
	 }
	 
	 /**
	     * 
	     * 查询某个手机号通话记录
	     * 
	     * @return List<Person>
	     */
	 public List<Person> queryByNumber(String number){
		 Log.d("lalal", "DBManager --> query");
		 ArrayList<Person> persons = new ArrayList<Person>();
		 Cursor cursor = queryTheCursorByNumber(number);
		 while(cursor.moveToNext()){
			 Person person = new Person();
			 person.setContact_id(cursor.getInt(cursor.getColumnIndex("contact_id")));
			 person.setPicUrl(cursor.getString(cursor.getColumnIndex("picUrl")));
			 person.setName(cursor.getString(cursor.getColumnIndex("name")));
			 person.setNumber(cursor.getString(cursor.getColumnIndex("number")));
			 person.setDate(cursor.getLong(cursor.getColumnIndex("date")));
			 person.setDuration(cursor.getInt(cursor.getColumnIndex("duration")));	
			 person.setMold(cursor.getInt(cursor.getColumnIndex("mold")));
			 person.setType(cursor.getInt(cursor.getColumnIndex("type")));
	         persons.add(person);
		 }
		 cursor.close();
		return persons;
		}
	 
	 
	 /**
	  * 查询某个手机号通话记录
	  * 
	  * @return Cursor
	  */
	 public Cursor queryTheCursorByNumber(String number){
		 Log.d("lallala", "DBManager --> queryTheCursor");
		 Cursor c =  db.rawQuery("SELECT * FROM personInfos where number=? ",
				 new String[] {number});
		 return c;
				 
	 }
	 
	 
	 public void update(Person person){
		 Log.d("lallala", "DBManager --> update");
		 ContentValues values = new ContentValues();
		 values.put("picUrl", person.getPicUrl());
		 values.put("name", person.getName());
		 values.put("number",person.getNumber());
		 db.update(DatabaseHelper.TABLE_NAME, values, "contact_id=?", new String[]{String.valueOf(person.getContact_id())});
	 }
	 
	 /**
	     * close database
	     */
	 public void closeDB()
	    {
	        Log.d("lalalal", "DBManager --> closeDB");
	        // 释放数据库资源
	        db.close();
	    }
	 
	 /**
	  * 根据来电号码删除全部通话记录
	  * @param person
	 * @return 
	  */
	 public boolean delete(Person person){
		 boolean isOk  = false;
		 db.beginTransaction();//开始事务
		 try{
			 db.delete(DatabaseHelper.TABLE_NAME, "number = ?", new String[]{person.getNumber() });
		//}
		 db.setTransactionSuccessful();
		 isOk = true;
		 }finally{
			 db.endTransaction();//结束事务
			 Uri notifyUri = Uri.parse("content://com.wiwikeyandroid");
			 context.getContentResolver().notifyChange(notifyUri, null);
		 }
		 return isOk; 
	 }
}
