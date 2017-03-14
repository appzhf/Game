package com.example.zhf.game.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.example.zhf.game.R;


public class SelectIconAcitivty extends Activity implements OnItemClickListener{
	GridView gv;
	Integer ids[]={R.drawable.p_1,R.drawable.p_2,R.drawable.p_3,R.drawable.p_4,R.drawable.p_5};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.icon);
		super.onCreate(savedInstanceState);
		gv = (GridView) findViewById(R.id.gridview);
		//gv.setAdapter(new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, ids));
		gv.setAdapter(new MyAdapter());
		gv.setOnItemClickListener(this);
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			
			return ids.length;
		}

		@Override
		public Object getItem(int arg0) {
			
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ImageView iv;
			if(arg1==null){
				iv = new ImageView(SelectIconAcitivty.this);
				iv.setScaleType(ScaleType.FIT_XY);
				iv.setLayoutParams(new GridView.LayoutParams(100, 100));
			}else{
				iv = (ImageView) arg1;
			}
			iv.setImageResource(ids[arg0]);
			return iv;
		}
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent data = new Intent();
		data.putExtra("imager", ids[arg2]);
		setResult(2,data);
		finish();
	}
}
