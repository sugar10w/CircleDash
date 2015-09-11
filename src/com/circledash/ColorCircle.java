package com.circledash;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

public class ColorCircle {

	Bitmap bt;
	Canvas g;
	
	static Boolean Enabled=true;
	
	int delta=6, to=2, max;
	float Width=12;
	
	static int cnt;
	
	Boolean[] got;
	
	float s,m;
	
	public ColorCircle()
	{
		max=360/delta;
		got=new Boolean[max];
		
		for (int i=0;i<max;++i) got[i]=false;
		
		Width=12*Map.k*1.5F;
		
		pen.setAntiAlias(true);

		
		pen2.setAntiAlias(true);
		pen2.setTextSize(Width);
		pen2.setColor(Color.BLACK);
		Typeface ff=Typeface.create(Typeface.SERIF, Typeface.BOLD);
		pen2.setTypeface(ff);
		
		s=Width ; m=Width;
		
		rect=new RectF(0,	0, 
				2*s,	2*s);
		
		bt=Bitmap.createBitmap(2*(int)s, 2*(int)s, Config.ARGB_8888);
		
		g=new Canvas();
		g.setBitmap(bt);
		
	}
	
	RectF rect;
	Paint pen=new Paint();
	Paint pen2=new Paint();
	public void Draw(Canvas g0)
	{				
		if (!Enabled) return;
		
		Paint p=new Paint();		
		g0.drawBitmap(bt, Map.W-2.5F*s, Map.H-2.5F*s, p);
		
		
		if (cnt>0)
		{
			g0.drawText(""+cnt, Map.W-Width*2, Map.H-Width, pen2);

		}
		
	}

	static public void ChangeEnabled()
	{
		Enabled=!Enabled;
	}

	void Got(int gotID)
	{
		if (got[gotID]) return;
		
		got[gotID]=true;
		
		if (!Enabled) return;
		
		pen.setColor(MyColor.HSI(gotID*delta, 0.2));
		g.drawArc(rect, gotID*delta, delta*2, true, pen);
	}
	
	void Redraw()
	{
		if (!Enabled) return;
		
		bt=Bitmap.createBitmap(2*(int)s, 2*(int)s, Config.ARGB_8888);
		g.setBitmap(bt);
		
		for (int i=0;i<max;++i)
		{			
			pen.setColor(MyColor.HSI(i*delta, 0.05));
			g.drawArc(rect, i*delta, delta*2, true, pen);
		}
				
	}
	
	public void Get(int id)
	{
		int t=id/delta;
						
		Got(t);
		for (int i=0;i<to;++i) 
		{
			Got((t+i)%max);
			Got((t-i+max)%max);
		}
				
		int ll=0;
		for (int i=0; i<max; ++i ) if (!got[i]) ++ll;
		if (ll>2) return;
		
		++cnt;
		for (int i=0; i<max; ++i) got[i]=false;
		
		
		Redraw();
				
	}
	
}
