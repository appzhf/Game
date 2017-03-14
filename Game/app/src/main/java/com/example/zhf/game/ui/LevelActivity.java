package com.example.zhf.game.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.zhf.game.GameView;
import com.example.zhf.game.R;


public class LevelActivity extends Activity implements OnClickListener{
	//int ids[]={R.id.one,R.id.two,R.id.three};
	Button two,one,three;
	GameView gv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.level);
		super.onCreate(savedInstanceState);
//		for (int i = 0; i < ids.length; i++) {
//			Button btn = (Button) findViewById(ids[i]);
//			btn.setOnClickListener(this);
//		}
		one = (Button) findViewById(R.id.one);
		one.setOnClickListener(this);
		two = (Button) findViewById(R.id.two);
		two.setOnClickListener(this);
		three = (Button) findViewById(R.id.three);
		three.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		Intent data = new Intent();
		switch (arg0.getId()) {
			case R.id.one:
				data.putExtra("level",20);
				setResult(1,data);
				finish();
				System.out.println("one");
				break;
			case R.id.two:

				data.putExtra("level", 0);
				setResult(1,data);
				finish();
				System.out.println("two");

				break;
			case R.id.three:
				data.putExtra("level", -20);
				setResult(1,data);
				finish();
				System.out.println("three");
				break;
			default:
				break;
		}
	}
}
