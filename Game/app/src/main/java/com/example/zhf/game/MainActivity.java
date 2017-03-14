package com.example.zhf.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zhf.game.ui.LevelActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener,View.OnTouchListener {
    GameView gv;
    Button go,down,refresh;
    TextView time,num;
    int tt=60;//控制游戏时间
    int nubms=0;//游戏得分
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gv = (GameView) findViewById(R.id.gv);
        gv.setW(getWindowManager().getDefaultDisplay().getWidth());//屏幕的宽
        gv.setH(getWindowManager().getDefaultDisplay().getHeight());//屏幕的高
        gv.init();
        gv.postInvalidate();
        gv.setEnabled(false);
        gv.setOnTouchListener(this);

        go = (Button) findViewById(R.id.go);
        go.setOnClickListener(this);
        down = (Button) findViewById(R.id.down);
        down.setOnClickListener(this);
        refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(this);
        time = (TextView) findViewById(R.id.time);
        num = (TextView) findViewById(R.id.num);

        //带数据跳转
        int num = getIntent().getIntExtra("num", 0);
        gv.setBitmapW(gv.getBitmapW()+num);
        gv.setBitmapH(gv.getBitmapH()+num);
        gv.init();
        gv.postInvalidate();

        //按钮不可用
        if(tt==0){
            gv.setEnabled(false);

        }
    }

    Handler handler = new Handler();
    Thread r = new Thread(){
        public void run(){
            tt--;
            if(tt<0){
                Game();
                return;
            }
            time.setText("倒计时："+tt);
            handler.postDelayed(this, 1000);
        }
    };
    public void Game(){//弹出对话框
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setIcon(R.drawable.p_1)
                .setMessage("Game Over")
                .setTitle("游戏结束")
                .setPositiveButton("再来一局", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        tt=60;
                        nubms=0;
                        num.setText("分数："+nubms);
                        gv.setEnabled(false);
                        down.setEnabled(true);

                        gv.init();
                        gv.postInvalidate();
                        go.setText("恢复");
                        flag = true;

                    }
                }).setNegativeButton("取消", null).show();
    }
    boolean flag = true;//判断按钮暂停和开始
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.go:
//			gv.setBitmapW(gv.getBitmapW()-10);
//			gv.setBitmapH(gv.getBitmapH()-10);
//			gv.init();
//			gv.postInvalidate();

                if(flag){
                    handler.postDelayed(r, 1000);

                    gv.setEnabled(true);
                    go.setText("暂停");
                    flag = false;
                }else{
                    handler.removeCallbacks(r);
                    gv.setEnabled(false);
                    go.setText("恢复");
                    flag = true;
                }
                down.setEnabled(false);

                break;
            case R.id.down:
//			gv.setBitmapW(gv.getBitmapW()+10);
//			gv.setBitmapH(gv.getBitmapH()+10);
//			gv.init();
//			gv.postInvalidate();
                Intent intent = new Intent(this,LevelActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.refresh://刷新操作
                List<Integer> list = new ArrayList<Integer>();
                for (int i = 0; i < gv.images.length; i++) {
                    for (int j = 0; j < gv.images[i].length; j++) {
                        if(gv.images[i][j]!=-1){
                            list.add(gv.images[i][j]);
                        }
                    }
                }
                Collections.shuffle(list);
                int k=0;
                for (int i = 0; i < gv.images.length; i++) {
                    for (int j = 0; j < gv.images[i].length; j++) {
                        if(gv.images[i][j]!=-1){
                            gv.images[i][j]=list.get(k);
                            k++;
                        }
                    }
                }
                gv.postInvalidate();
                break;
            default:
                break;
        }
    }

    //带结果跳转
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1&&resultCode==1){
            gv.setBitmapW(gv.getBitmapW()+data.getIntExtra("level",0));
            gv.setBitmapH(gv.getBitmapH()+data.getIntExtra("level",0));
            gv.init();
            gv.postInvalidate();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    Point A,B;//保存点
    int count=1;
    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        switch (arg1.getAction()) {
            case MotionEvent.ACTION_UP:
                if((arg1.getX()>=gv.left&&arg1.getX()<=gv.w-gv.left)//水平范围的判断
                        &&(arg1.getY()<=gv.countY*gv.getBitmapH())){//垂直范围的判断
                    //System.out.println(arg1.getX()+":"+arg1.getY());
                    int x = (int)(arg1.getX()-gv.left)/gv.bitmapW;
                    int y = (int)arg1.getY()/gv.bitmapH;
                    if(count%2!=0){//A为一个点
                        A = new Point(x, y);
                    }else{//AB为两个点
                        if(x==A.x&&y==A.y){
                            System.out.println("AB为同一个点");
                            return true;
                        }
                        B= new Point(x, y);
                        if(gv.images[A.y][A.x]==gv.images[B.y][B.x]){
                            if(gv.images[A.y][A.x]!=-1&&gv.images[B.y][B.x]!=-1){//图片消掉再点空白就不画线
                                if(gv.lineAB(A, B)){
                                    gv.images[A.y][A.x]=-1;
                                    gv.images[B.y][B.x]=-1;
                                    gv.postInvalidate();
                                    nubms+=10;
                                    num.setText("分数："+nubms);
                                    if(nubms==(gv.countX*gv.countY/2)*10){
//									Intent intent = new Intent(this, GoOnActivity.class);
//									startActivity(intent);
                                        handler.removeCallbacks(r);
                                        AlertDialog.Builder ab = new AlertDialog.Builder(this);
                                        ab.setIcon(R.drawable.p_1)
                                                .setMessage("恭喜过关！分数："+nubms)
                                                .setPositiveButton("下一关",new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface arg0, int arg1) {

                                                        nubms=0;
                                                        num.setText("分数："+nubms);
                                                        gv.setBitmapW(gv.getBitmapW()-40);
                                                        gv.setBitmapH(gv.getBitmapH()-40);
                                                        tt+=30;//每次过关时间加30秒
                                                        handler.postDelayed(r, 1000);
                                                        go.setText("暂停");
                                                        gv.init();
                                                        gv.postInvalidate();
                                                    }
                                                }).setNegativeButton("取  消", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                finish();
                                            }
                                        })
                                                .show();
                                    }
                                    new Handler().postDelayed(new Thread(){
                                        public void run(){
                                            gv.postInvalidate();
                                        }
                                    }, 200);
                                }
                            }
                        }
                    }
                    count++;
                }
                break;

            default:
                break;
        }
        return true;
    }
}
