package com.circledash;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;

public class MainActivity extends Activity 
{

	static Game game=new Game(); 
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyView mv=new MyView(this);
		
		mv.setOnTouchListener(
			new OnTouchListener()
			{

				@Override
				public boolean onTouch(View v, MotionEvent e) {
					if (e.getAction()==MotionEvent.ACTION_DOWN) 
					{
						game.Action(e.getX(), e.getY());
						return false;
					}
					return false;
				}
				
			}
		);
		
		mv.setOnLongClickListener(
			new OnLongClickListener()
			{
				@Override
				public boolean onLongClick(View v) {
					game.Continue();
					return true;
				}
			
			}
		);
		setContentView(mv);
	}
	
		
//	public boolean onTouchEvent(MotionEvent e){	
						
		
//		if (e.getAction()==MotionEvent.ACTION_DOWN){
//			game.Action(e.getX(),e.getY());	
//		}
		
//		return false;
//	}
	
	
	class MyView extends SurfaceView implements SurfaceHolder.Callback{

		private SurfaceHolder holder;
		private DrawThread drawThread;
		private RefreshThread refreshThread;
		private ClockThread clockThread;
		
		public MyView(Context context) {
			super(context);
			holder=this.getHolder();
			holder.addCallback(this);						
		}
		
		
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			
			drawThread=new DrawThread(holder);
			drawThread.isRun=true;
			drawThread.start();
			
			refreshThread=new RefreshThread(holder);
			refreshThread.isRun=true;
			refreshThread.start();
			
			clockThread=new ClockThread(holder);
			clockThread.isRun=true;
			clockThread.start();
			
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			
			drawThread.isRun=false;
			refreshThread.isRun=false;
			clockThread.isRun=false;
			
		}
		
	}
	
	
	class DrawThread extends Thread{
		private SurfaceHolder holder;
		public boolean isRun=false;
		public DrawThread(SurfaceHolder holder){
			this.holder=holder;
			isRun=true;
		}		
		
		Paint pen;
		
		@Override
		public void run(){
			while (isRun){
				Canvas c=null;
				try
				{
					synchronized(holder){
						c=holder.lockCanvas();							
						game.Draw(c);					
						Thread.sleep(40);
					}
				}
				catch(Exception e){
					
				}
				finally{
					if (c!=null){
						holder.unlockCanvasAndPost(c);
					}
				}
			}
		}
	}
	
	class RefreshThread extends Thread{

		public boolean isRun=false;
		public RefreshThread(SurfaceHolder holder){
			isRun=true;
		}		
		@Override
		public void run(){
			while (isRun){
				try
				{
					game.Refresh();
					Thread.sleep(40);					
				}
				catch(Exception e){
					
				}
				finally{

				}
			}
		}
	}
	
	class ClockThread extends Thread{

		public boolean isRun=false;
		public ClockThread(SurfaceHolder holder){
			isRun=true;
		}		
		@Override
		public void run(){
			while (isRun){
				try
				{
					game.Clock();
					Thread.sleep(100);					
				}
				catch(Exception e){
					
				}
				finally{

				}
			}
		}
	}
	
}
