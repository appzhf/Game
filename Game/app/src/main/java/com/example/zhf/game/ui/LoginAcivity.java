package com.example.zhf.game.ui;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zhf.game.R;
import com.example.zhf.game.dao.UserInfoDao;
import com.example.zhf.game.entity.UserInfo;

public class LoginAcivity extends Activity implements OnClickListener{
	Button login,regist;
	EditText name,pwd;
	UserInfoDao dao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.login);
		super.onCreate(savedInstanceState);
		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(this);
		regist = (Button) findViewById(R.id.regist);
		regist.setOnClickListener(this);
		
		name = (EditText) findViewById(R.id.name);
		pwd = (EditText) findViewById(R.id.pwd);
		dao = new UserInfoDao(this, 3);
		
		//���ݴ洢
		SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
		String loginName= sp.getString("name", null);
		if(null!=loginName){
			Intent intent = new Intent(this, GoOnActivity.class);
			ui.setName(loginName);
			startActivity(intent);
			finish();
		}
	}
	UserInfo ui = new UserInfo();
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.login:
			if(dao.login(name.getText().toString(), pwd.getText().toString())){
				Intent intent = new Intent(this, GoOnActivity.class);
					
					ui.setName(name.getText().toString());
					if(true){
						SharedPreferences.Editor se = getSharedPreferences("login", MODE_APPEND).edit();
						se.putString("name", ui.getName());
						se.commit();
						System.out.println("保存登录状态:"+ui.getName());
					}
				startActivity(intent);
				finish();
				}else{
				Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.regist:
			Intent intent = new Intent(this, RegistActivity.class);
			startActivityForResult(intent, 1);
			break;
		default:
			break;
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==1&&resultCode==1){
			name.setText(data.getStringExtra("result"));
			Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
