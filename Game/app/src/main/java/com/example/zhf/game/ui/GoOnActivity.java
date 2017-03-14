package com.example.zhf.game.ui;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageButton;

import com.example.zhf.game.MainActivity;
import com.example.zhf.game.R;


public class GoOnActivity extends Activity implements OnClickListener{
	int ids[]={R.id.start1, R.id.start2,R.id.start3,R.id.start4};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.goon);
		super.onCreate(savedInstanceState);
		setTitle("成功");
		for (int i = 0; i < ids.length; i++) {
			ImageButton imbtn = (ImageButton) findViewById(ids[i]);
			imbtn.setOnClickListener(this);
		}
		
	}

	@Override
	public void onClick(View arg0) {
		Intent intent =new Intent(this, MainActivity.class);//��������ת
		switch (arg0.getId()) {
		case R.id.start1:
			intent.putExtra("num", 40);
			startActivity(intent);
			
			break;
		case R.id.start2:
			intent.putExtra("num", 20);
			startActivity(intent);
			finish();
			break;
		case R.id.start3:
			intent.putExtra("num", 0);
			startActivity(intent);
			finish();
			break;
		case R.id.start4:
			Builder ab = new Builder(this);
			ab.setIcon(R.drawable.p_1)
			.setMessage("亲！真的要离开我吗？")
			.setPositiveButton("退出", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					SharedPreferences.Editor se = getSharedPreferences("login", MODE_PRIVATE).edit();
					se.clear();
					se.commit();
					finish();
					
				}
			}).setNegativeButton("否", null).show();
			break;
		default:
			break;
		}
		
	}
	
}
