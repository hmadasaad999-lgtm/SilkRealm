package com.example.silkrealm;

import android.app.*;
import android.os.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import android.content.*;
import java.util.*;

public class MainActivity extends Activity {
  public void onCreate(Bundle b){super.onCreate(b); setContentView(new GameView(this));}

  static class GameView extends View {
    Paint p=new Paint(3); Random r=new Random(); float px=540,py=900;
    int level=1,xp=0,gold=0,hp=100,maxHp=100; ArrayList<Enemy> mobs=new ArrayList<>();
    float joyX=150,joyY=850; boolean joy=false;
    GameView(Context c){super(c); p.setTypeface(Typeface.create("sans",Typeface.BOLD));
      for(int i=0;i<8;i++) mobs.add(new Enemy(300+r.nextInt(900),250+r.nextInt(500)));
    }
    protected void onDraw(Canvas c){
      int w=getWidth(),h=getHeight();
      p.setStyle(Paint.Style.FILL); p.setColor(Color.rgb(30,105,70)); c.drawRect(0,0,w,h,p);
      p.setColor(Color.rgb(45,125,82)); for(int x=0;x<w;x+=80)for(int y=0;y<h;y+=80)c.drawCircle(x,y,2,p);
      // city/road
      p.setColor(Color.rgb(155,115,65)); c.drawRect(0,h-180,w,h,p);
      p.setColor(Color.rgb(215,180,105)); c.drawRect(0,h-150,w,h,p);
      // mobs
      for(Enemy e:mobs) if(e.alive){p.setColor(Color.rgb(150,70,45));c.drawCircle(e.x,e.y,28,p);
        p.setColor(Color.WHITE);c.drawCircle(e.x-9,e.y-5,4,p);c.drawCircle(e.x+9,e.y-5,4,p);
        p.setColor(Color.rgb(40,180,60));c.drawRect(e.x-25,e.y-42,e.x+25,e.y-36,p);}
      // player
      p.setColor(Color.rgb(35,75,180));c.drawCircle(px,py,32,p);
      p.setColor(Color.WHITE);c.drawRect(px-12,py-40,px+12,py-28,p);
      p.setColor(Color.DKGRAY);p.setStrokeWidth(6);c.drawLine(px+20,py-10,px+65,py-55,p);
      // UI
      p.setColor(Color.argb(190,0,0,0));c.drawRoundRect(18,18,350,112,18,18,p);
      p.setColor(Color.WHITE);p.setTextSize(24);c.drawText("Silk Realm  •  Level "+level,35,48,p);
      p.setColor(Color.rgb(180,35,35));c.drawRect(35,62,300,82,p);p.setColor(Color.GREEN);c.drawRect(35,62,35+265*hp/maxHp,82,p);
      p.setColor(Color.WHITE);p.setTextSize(18);c.drawText("XP "+xp+"/100   Gold "+gold,35,103,p);
      // controls
      p.setColor(Color.argb(90,255,255,255));c.drawCircle(joyX,joyY,75,p);
      p.setColor(Color.argb(150,255,255,255));c.drawCircle(joyX+(joy?0:0),joyY,30,p);
      p.setColor(Color.argb(170,180,45,45));c.drawCircle(w-120,h-100,55,p);
      p.setColor(Color.WHITE);p.setTextSize(22);c.drawText("ATK",w-145,h-93,p);
      p.setTextSize(17);c.drawText("Tap ATK to hit nearest monster",w-390,35,p);
    }
    public boolean onTouchEvent(android.view.MotionEvent e){
      float x=e.getX(),y=e.getY(); int w=getWidth(),h=getHeight();
      if(e.getAction()==MotionEvent.ACTION_DOWN||e.getAction()==MotionEvent.ACTION_MOVE){
        if(x<w/2){joy=true; px += (x-joyX)*0.025f; py += (y-joyY)*0.025f; invalidate();}
        if(e.getAction()==MotionEvent.ACTION_DOWN && x>w-220 && y>h-190){
          Enemy best=null; double d=1e9;
          for(Enemy m:mobs)if(m.alive){double q=Math.hypot(m.x-px,m.y-py);if(q<d){d=q;best=m;}}
          if(best!=null&&d<180){best.alive=false;xp+=25;gold+=10;
            if(xp>=100){xp-=100;level++;maxHp+=15;hp=maxHp;}
            invalidate();}
      } else if(e.getAction()==MotionEvent.ACTION_UP){joy=false;}
      }
      return true;
    }
    class Enemy{float x,y;boolean alive=true;Enemy(float a,float b){x=a;y=b;}}
  }
}