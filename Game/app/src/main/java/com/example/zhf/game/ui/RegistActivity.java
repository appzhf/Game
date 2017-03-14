package com.example.zhf.game.ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.zhf.game.R;
import com.example.zhf.game.dao.UserInfoDao;
import com.example.zhf.game.entity.Obj;
import com.example.zhf.game.entity.UserInfo;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class RegistActivity extends Activity implements OnClickListener,OnItemSelectedListener{
	EditText name,pwd,repwd;
	RadioButton male,female;
	QuickContactBadge icon;
	Button regist,reg;
	UserInfoDao dao;
	UserInfo ui;
	Gson gson;
	Spinner sheng,shi;
	List<String> shengs;//保存所有的省
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.regist);
		super.onCreate(savedInstanceState);
		icon = (QuickContactBadge) findViewById(R.id.icon);
		icon.setOnClickListener(this);
		name = (EditText) findViewById(R.id.name);
		pwd = (EditText) findViewById(R.id.pwd);
		repwd = (EditText) findViewById(R.id.repwd);
		male = (RadioButton) findViewById(R.id.male);
		female = (RadioButton) findViewById(R.id.female);
		regist = (Button) findViewById(R.id.regist);
		regist.setOnClickListener(this);
		reg = (Button) findViewById(R.id.reg);
		reg.setOnClickListener(this);
		dao = new UserInfoDao(this, 3);
		sheng = (Spinner) findViewById(R.id.sheng);
		shi = (Spinner) findViewById(R.id.shi);
		gson = new Gson();
		//读IO文件
		getFile();
		sheng.setOnItemSelectedListener(this);
		shi.setOnItemSelectedListener(this);
		
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.regist:
			ui = new UserInfo();
			ui.setName(name.getText().toString());
			ui.setPwd(pwd.getText().toString());
			ui.setSex(male.isChecked()?"男":"女");
			ui.setAddress(citiy);
			ui.setIcon(imager);
			if(pwd.getText().toString().equals(repwd.getText().toString())
					&&name.getText().length()!=0&&pwd.getText().length()!=0){
				
				if(dao.regist(ui)>0){
					Intent data = new Intent();
					data.putExtra("result", name.getText().toString());
					setResult(1, data);
					finish();
				}else{
					Toast.makeText(this, "注册失败",Toast.LENGTH_SHORT).show();
				}
			}else{
				Builder ab = new Builder(this);
				ab.setIcon(R.drawable.p_1)
				.setMessage("用户名或密码不合法")
				.setPositiveButton("重新输入", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						name.setText("");
						pwd.setText("");
						repwd.setText("");
					}
				}).show();
			}
			break;
		case R.id.reg:
			name.setText("");
			pwd.setText("");
			repwd.setText("");
			break;
		case R.id.icon:
			Intent intent = new Intent(this, SelectIconAcitivty.class);
			startActivityForResult(intent, 2);
			break;
		default:
			break;
		}
	}
	
	int imager=R.drawable.p_1;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==2&&resultCode==2){
			imager=data.getIntExtra("imager", R.drawable.p_1);
			icon.setImageResource(imager);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	Obj obj;
	public void getFile(){
		
		try {
			String s="";
			InputStream is = getResources().openRawResource(R.raw.data);
			byte b[] = new byte[1024];
			while (true) {
				int len = is.read(b);
				if(len==-1){
					break;
				}
				s=s+new String(b,0,len,"gbk");
				//System.out.println(s);
			}
			
			obj=gson.fromJson(s, Obj.class);
			
			shengs = new ArrayList<String>();
			for (int i = 0; i < obj.getData().size(); i++) {
				shengs.add(obj.getData().get(i).getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//添加数组适配器
		sheng.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, shengs));
		
	}
	String citiy="";
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		switch (arg0.getId()) {
		case R.id.sheng:
			shi.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, obj.getData().get(arg2).getCities()));
			break;
		case R.id.shi:
			citiy= obj.getData().get(sheng.getSelectedItemPosition()).getCities().get(arg2);
			System.out.println("citiy:"+citiy);
			break;
		default:
			break;
		}
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
}
