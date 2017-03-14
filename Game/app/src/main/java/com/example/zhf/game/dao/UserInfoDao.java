package com.example.zhf.game.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.zhf.game.entity.UserInfo;

public class UserInfoDao extends SQLiteOpenHelper {
	public static final String DB_NAME="userinf.db";
	public static final String TB_NAME="userinfo";
	
	public UserInfoDao(Context context, 
			int version) {
		super(context, DB_NAME,null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		arg0.execSQL("create table userinfo(_id integer primary key autoincrement,name nvarchar2(32),pwd nvarchar2(32),sex nvarchar2(5))");
		System.out.println("创建表");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		System.out.println("版本升级+"+arg1+"---->"+arg2);
		//arg0.execSQL("alter table "+TB_NAME+" add column icon integer");
		arg0.execSQL("alter table "+TB_NAME+" add column address nvarchar2(50)");
		System.out.println("修改成功");
	}
	//登录
	public boolean login(String name,String pwd){
		Cursor c= getReadableDatabase().query("userinfo", null, "name=? and pwd=?", new String[]{name,pwd}, null, null, null);
		return c.moveToNext();
	}
	//注册
	public long regist(UserInfo ui){
		ContentValues values = new ContentValues();
		values.put("name", ui.getName());
		values.put("pwd", ui.getPwd());
		values.put("sex", ui.getSex());
//		values.put("address", ui.getAddress());
//		values.put("icon", ui.getIcon());
		return getWritableDatabase().insert("userinfo", null, values);
	}
}
