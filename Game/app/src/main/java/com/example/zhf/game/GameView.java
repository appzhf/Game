package com.example.zhf.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {
	//所有图片
	int ids[]={R.drawable.p_1,R.drawable.p_2,R.drawable.p_3,R.drawable.p_4,R.drawable.p_5};
	int w,h;//屏幕的宽高
	int bitmapW,bitmapH;//图片的宽高
	int countX,countY;//水平垂直方向的各数
	int images[][];//所有权图片二维数组
	float left,top;

	List<Point> points;//点的集合
	Path path;//画线的路经
	Paint paint;//画笔

	public void init(){//初始化
		points = new ArrayList<Point>();
		path = new Path();

		countX = w/bitmapW;
		countY = (int)(h-getResources().getDimension(R.dimen.HL))/bitmapH;
		left = (w-countX*bitmapW)/2;
		//top = (h-getResources().getDimension(R.dimen.HL)-countY*bitmapH);

		//随机得到图片
		Random ran = new Random();
		if(countX*countY%2!=0){
			countY = countY-1;
		}

		images =new int[countY][countX];
		//图片加到list
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < countX*countY/2; i++) {
			list.add(ids[ran.nextInt(ids.length)]);
		}
		//复制图片
		list.addAll(list);
		//洗牌
		Collections.shuffle(list);
		for (int i = 0; i < list.size(); i++) {
			images[i/countX][i%countX]=list.get(i);
		}
		System.out.println(countX+":"+countY);
	}
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//获取图片的高宽
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.p_1);
		bitmapH = bm.getHeight();
		bitmapW = bm.getWidth();

		//画线
		paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(3);
		paint.setColor(Color.RED);

	}
	@Override
	protected void onDraw(Canvas canvas) {
		if(images==null){
			return;
		}
		for (int i = 0; i < images.length; i++) {
			for (int j = 0; j < images[i].length; j++) {
				if(images[i][j]==-1){
					continue;
				}
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), images[i][j]);
				bitmap =ThumbnailUtils.extractThumbnail(bitmap, bitmapW, bitmapH, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
				canvas.drawBitmap(bitmap, left+j*bitmapW, top+i*bitmapH, null);
			}
		}
		//画线
		if(points.size()>=2){
			path.reset();
			path.moveTo(left+points.get(0).x*bitmapW+bitmapW/2, points.get(0).y*bitmapH+bitmapH/2);
			for (int i = 1; i < points.size(); i++) {
				path.lineTo(left+points.get(i).x*bitmapW+bitmapW/2, points.get(i).y*bitmapH+bitmapH/2);

			}
			canvas.drawPath(path, paint);
			points.clear();
		}
		super.onDraw(canvas);
	}
	//判断是否可以消除的方法
	public boolean lineAB(Point A,Point B){
		//强制A在左边B在右边
		if(B.x<A.x){
			return lineAB(B, A);
		}
		return OnelineAB(A, B)||TwolineAB(A, B)||ThreelineAB(A, B);
	}
	//1条线
	public boolean OnelineAB(Point A,Point B){
		if(A.y==B.y){//水平
			//相邻
			if(B.x-A.x==1){
				points.add(A);
				points.add(B);
				return true;
			}else{
				for (int i =A.x+1; i < B.x; i++) {
					if(images[A.y][i]!=-1){//中间有图片的
						points.clear();
						return false;
					}
				}
				points.add(A);
				points.add(B);
				return true;
			}
		}else if(A.x==B.x){//垂直
			//相邻
			if(B.y-A.y==1||B.y-A.y==-1){
				points.add(A);
				points.add(B);
				return true;
			}else{
				int start =A.y>B.y?B.y:A.y;
				int end = A.y>B.y?A.y:B.y;
				for (int i =start+1; i < end; i++) {
					if(images[i][A.x]!=-1){//中间有图片的
						points.clear();
						return false;
					}
				}
				points.add(A);
				points.add(B);
				return true;
			}
		}
		points.clear();
		return false;
	}
	//2条线
	public boolean TwolineAB(Point A,Point B){
		Point c1 = new Point(B.x, A.y);
		Point c2 = new Point(A.x, B.y);
		return (images[A.y][B.x]==-1&&OnelineAB(A, c1)&&OnelineAB(c1, B))
				||(images[B.y][A.x]==-1&&OnelineAB(A, c2)&&OnelineAB(c2, B));
	}
	//3条线
	public boolean ThreelineAB(Point A,Point B){
		return ThreeSxlineAB(A, B)||ThreeZylineAB(A, B);
	}
	public boolean ThreeSxlineAB(Point A,Point B){
		int begin = 0,end = countY-1;
		for (int i = A.y-1; i >=0; i--) {
			if(images[i][A.x]!=-1){
				begin=i+1;
				break;
			}
		}
		for (int i = A.y+1; i < countY-1; i++) {
			if(images[i][A.x]!=-1){
				end=i-1;
				break;
			}
		}
		Point c;
		for (int i = begin; i <=end; i++) {
			if(i==A.y){
				continue;
			}
			c = new Point(A.x, i);
			if(TwolineAB(c, B)){
				points.add(0,A);
				return true;
			}
		}
		System.out.println("Sx+"+begin+"-------"+end);
		return false;
	}
	public boolean ThreeZylineAB(Point A,Point B){
		int begin = 0,end = countX-1;
		for (int i = A.x-1; i >=0; i--) {
			if(images[A.y][i]!=-1){
				begin=i+1;
				break;
			}
		}
		for (int i = A.x+1; i < countX-1; i++) {
			if(images[A.y][i]!=-1){
				end=i-1;
				break;
			}
		}
		Point c;
		for (int i = begin; i <=end; i++) {
			if(i==A.x){
				continue;
			}
			c = new Point(i,A.y);
			if(TwolineAB(c, B)){
				points.add(0,A);
				return true;
			}
		}
		return false;
	}



	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public int getBitmapW() {
		return bitmapW;
	}
	public void setBitmapW(int bitmapW) {
		this.bitmapW = bitmapW;
	}
	public int getBitmapH() {
		return bitmapH;
	}
	public void setBitmapH(int bitmapH) {
		this.bitmapH = bitmapH;
	}
}
