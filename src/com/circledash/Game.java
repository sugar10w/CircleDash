package com.circledash;

import android.graphics.Canvas;

public class Game {
    
	static Map map = new Map();
    static Guide guide=new Guide();    
    
    public Game()
    {
    	map.Playing=false;
    }
    
//    public void SetWH(int W, int H)
//    {
//        map.SetWH(W, H);
//    }


    Boolean Prepared=false;
    public void Draw(Canvas g)
    {
    	if (!Prepared) 
    	{
    		Prepared=true;
    		map.SetWH(g.getWidth(), g.getHeight());
    	}
        map.Draw(g);
        if (map.Welcome || Guide.usingGuide ) guide.Draw(g);
    }

    public void Refresh()
    {
        map.Refresh();
    }

    public void Clock()
    {
        map.Clock();
    }

    
    Boolean isFirstClicked=true;
    
    int cnt=0;
    public void Action(float x,float y)
    {
    	if (y<Map.H/4F)
    	{
    		Guide.usingGuide=true;  
    		guide.RandomGo();
    	}
    	
    	if (x>Map.W-30 && y>Map.H-30)
    	{
    	//	ColorCircle.ChangeEnabled();
    		Background.ChangeEnabled();
    		return;
    	}
    	
        if (Guide.usingGuide) 
        {        	
        	guide.Action();        	
        	if (map.Welcome && isFirstClicked) 
        	{
        		isFirstClicked=false;
        		return;
        	}
        }
    	
        if (map.Playing)
        {
            map.SpeedUp();
            map.Action();
        }        
        else if (map.Welcome) Continue();
        else
        {        	
        	++cnt; cnt%=10;
           if (cnt==0) 
          {
            	map.Welcome=false;
            	map.Reset();
            	cnt=0;
            }
        }
    }
    
    public void Continue()
    {
    	if (map.Playing) 
    	{
    		map.SuperDash();
    		return;
    	}
        map.Welcome=false;
        map.Reset();
        cnt=0;
    }
    
    public Boolean Playing()
    {
        return map.Playing;
    }
}
