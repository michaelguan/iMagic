package com.guantao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity
{
	public static final String PEFER_NAME="LOGINPEF";
	private Button loginButton;
	private EditText text_name;
	private EditText text_password;
	private PrivateDataBase pdb;
	private Cursor loginCursor;
	private CheckBox spBox;
	private CheckBox autobBox;
	private ImageView headiView;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginpage);
		headiView = (ImageView)findViewById(R.id.faceImg);
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_move_in) ;
		headiView.startAnimation(animation);
		text_name = (EditText)findViewById(R.id.login_edit_account);
		text_password = (EditText)findViewById(R.id.login_edit_pwd);
		
		text_password.setOnFocusChangeListener(new OnFocusChangeListener()
		{
			
			public void onFocusChange(View v, boolean hasFocus)
			{				
				if (hasFocus)
				{
					text_password.setText("");
				}
			}
		});
		spBox = (CheckBox)findViewById(R.id.login_cb_savepwd);
		autobBox= (CheckBox)findViewById(R.id.login_cb_auto);	
		
		//如果自动登陆，则必须保存密码
		autobBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					spBox.setChecked(true);
				}

			}
		});
		//如果不保存密码，也不能自动登陆
		spBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (!isChecked)
				{
					autobBox.setChecked(false);
					SharedPreferences sharedPreferences = getSharedPreferences(PEFER_NAME, 0);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.remove("username");
					editor.remove("password");	
					editor.commit();
				}				
			}
		});
		pdb = PrivateDataBase.getInstance(this);
		loginButton = (Button)findViewById(R.id.login_btn_login);
		loginButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				String passString = text_password.getText().toString();
				if (!passString.equals("") && passString.length() <=16)
				{
					text_password.setText(SecurityTools.EncryptMD5(passString));
				}
				login();
			}
		});
	}
	
	private void login()
	{
		loginCursor = pdb.getReadableDatabase().rawQuery("select * from USERS where name = ? and password = ?",
				new String[] {text_name.getText().toString().trim(),text_password.getText().toString().trim()});
		try
		{
			if (loginCursor.moveToFirst())
			{
				Intent mIntent = new Intent(LoginActivity.this,IMagicActivity.class);
				startActivity(mIntent);
			}
			else {
				Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
			}

		} catch (Exception e)
		{
			Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_LONG).show();
		}
		finally
		{
			loginCursor.close();
		}
	}
	
	@Override
	public void onResume()
	{
		//读取记住密码和自动登陆配置
		super.onResume();
		SharedPreferences sPreferences = getSharedPreferences(PEFER_NAME, 0);
		boolean isauto = sPreferences.getBoolean("autologin", false);
		boolean issp = sPreferences.getBoolean("savepass", false);
		spBox.setChecked(issp);
		autobBox.setChecked(isauto);

		String nameString = sPreferences.getString("username", "");
		String passString = sPreferences.getString("password", "");

		text_name.setText(nameString);
		text_password.setText(passString);
		if (isauto)
		{
			if (!nameString.equals("") && !passString.equals(""))
			{
				//login();
			}
			else {
				Toast.makeText(LoginActivity.this, "获取用户名和密码失败,请重新输入", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	@Override
	public void onPause()
	{
		super.onStop();
		
		//保存记住密码和自动登陆配置
		SharedPreferences sharedPreferences = getSharedPreferences(PEFER_NAME, 0);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		boolean issp = spBox.isChecked();
		editor.putBoolean("savepass", issp);
		editor.putBoolean("autologin", autobBox.isChecked());
		
		//如果设置了记住密码，则保存用户名和密码
		if (issp)
		{
			String nameString = text_name.getText().toString();
			String pasString = text_password.getText().toString();
			
			if (!nameString.equals("") && !pasString.equals(""))
			{
				editor.putString("username", nameString);
				editor.putString("password", pasString);
			}
		}
		editor.commit();
	}
}
