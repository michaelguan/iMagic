package com.guantao;
import android.content.Context;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class PrivateDataBase extends SQLiteOpenHelper
{
	private static PrivateDataBase Instance;

	public static PrivateDataBase getInstance(Context context)
	{
		if(Instance==null)
		{
			Instance = new PrivateDataBase(context, "accountsdb", null, 1);
		}
		return Instance;
	}
	
	private PrivateDataBase(Context context, String name, CursorFactory factory,
			int version)
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("create table if not exists USERS (name varchar,password varchar,role varchar,notes varchar)" );
		db.execSQL("insert into USERS (name,password,role,notes) values (?,?,?,?)", new String[] {"guantao",SecurityTools.EncryptMD5("123456"),"default","管涛"});
		db.execSQL("create table if not exists ACCOUNTS (user_name_1 varchar,password_1 varchar,user_name_2 varchar,password_2 varchar,user_name_3 varchar,password_3 varchar,owner varchar,kind varchar,notes varchar,create_date varchar)");
		db.execSQL("create table if not exists ACCOUNT_KINDS (name_en varchar,name_zh varchar,web_address varchar,notes varchar)");
		db.execSQL("insert into ACCOUNTS (user_name_1,password_1,owner,kind) values (?,?,?,?)", new String[] {"guantao2011","147258369","管涛","同花顺"});
		db.execSQL("insert into ACCOUNTS (user_name_1,password_1,owner,kind) values (?,?,?,?)", new String[] {"guantao2011","147258369","管涛","gmail"});
		db.execSQL("insert into ACCOUNTS (user_name_1,password_1,owner,kind) values (?,?,?,?)", new String[] {"michaelguan","147258369","管","博库书城"});
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("drop table if exists USERS");
		onCreate(db);
	}

}
