package com.circledash;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class Background {

	int cnt=5;
	float W,H;
	float[] x=new float[cnt];
	float[] y=new float[cnt];
	float[] r=new float[cnt];
	int[] c=new int[cnt];
	Random random=new Random();

	Boolean prepared=false;
	
	static Boolean Enabled=true;
	
	public void SetWH(float w0,float h0)
	{
		if (prepared) return;		
		W=w0; H=h0;
		Prepare();
		prepared=true;		
	}
	
	void Prepare()
	{
		for (int i=0;i<cnt;++i)
		{
			x[i]=random.nextFloat()*W;
			y[i]=random.nextFloat()*H;
			r[i]=W/3+random.nextFloat()*W/6;
			c[i]=random.nextInt(360);
		}
		
		pen.setAntiAlias(true);
		pen.setStyle(Style.STROKE);
		pen.setStrokeWidth(24*Map.k);
		
	}
	
	Paint pen=new Paint();
	public void Draw(Canvas g)
	{
    	g.drawColor(Color.argb(255, 240, 240, 240));		
    	
		if (!prepared) return;
		if (!Enabled) return;
		
//		if (!playing)
//		{
			pen.setStrokeWidth(24*Map.k);
			for (int i=0;i<cnt;++i)
			{
				pen.setColor(MyColor.HSI(c[i], 0.05));
				g.drawCircle(x[i], y[i], r[i], pen);
			}
//		}
//		else
//		{
//			pen.setStrokeWidth(12*Map.k);
//			for (int i=0;i<cnt;++i)
//			{
//				pen.setColor(MyColor.HSI(c[i], 0.05));
//				float x0,y0,r0;
//				if (x[i]<Map.W/4) x0=x[i]/2; else x0=(x[i]+Map.W)/2;
//				y0=y[i];
//				r0=r[i]/2;				
//				g.drawCircle(x0, y0, r0, pen);
//			}
//		}
		
	}
	
	static public void ChangeEnabled()
	{
		Enabled=!Enabled;
	}
	
}
