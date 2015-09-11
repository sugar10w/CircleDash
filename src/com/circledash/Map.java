package com.circledash;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;

public class Map {
    //my Snake here ~ 
    static Snake snake=new Snake();      
    //Background here ~
    static Background background=new Background();
    //ColorCircle here ~
    static ColorCircle colorcircle;
    
    //traces in the map.
    int maxT=12;
    Traces[] traces = new Traces[maxT];
    //Record which trace the head of the snake is in.
    int snakeHeadTrace = 0;
    //Time
    float leftTime;
    float deltaTime = 0.1F, maxTime = 30;
    //Scores!
    int cntSkip, cntDash, cntDuang;

    //It's playing
    public Boolean Playing = true;
    public Boolean Welcome=true;
    Boolean ready=false;
        
    
    //CONSTRUCT
    public Map()
    {
    	
    	
        pen.setAntiAlias(true);
        
        Reset();
    }

    static Random random = new Random();
    //Reset
    public void Reset()
    {
    	if (!ready) return;
    	
    	if (colorcircle==null) colorcircle=new ColorCircle();    	
    	
        traces[0] = new Traces(0);
        for (int i = 1; i < maxT; ++i) traces[i] = new Traces();
    	
        int c = random.nextInt(maxT);

        traces[c].Chained = true;
        snake.Reset(c, traces[c], colorcircle);

        MapMove(c);

        Playing = true;
        leftTime = maxTime;

        BoSuperDashing=false;
        
        cntSkip = 0;
        cntDash = 0;
        cntDuang = 0;
    }

    //Size of the screen. These are shared with other classes.
    public static int W = 550, H = 324, A = 874, M=437;
    public static int X0 = -162, Y0 = -275;
    //TO-DO: Try to modify the screen
    static public float k=1;
    public void SetWH(int W0, int H0)
    {
    	ready=true;
    	
        W = W0; H = H0;
        A = W + H;
        M = A / 2;
        X0 = -(A - W) / 2;
        Y0 = -(A - H) / 2;

        if (W < H) actRect = new RectF((W - H) / 2, 0, H, H);
        else actRect = new RectF(0, (H - W) / 2, W, W);

        int maxR;
        if (W < H) maxR = W / 3; else maxR = H / 3;
        Traces.SetR(maxR / 2, maxR);
        
        k=(float)maxR/108;
        Snake.setSize(k);
        Guide.SetSize(k);
        
        background.SetWH(W0, H0);
    }
    
    
    //dx,dy to move the map        
    public static float dx = 0, dy = 0;
    public static PointF mapPoint(PointF p)
    {
        return new PointF(p.x + dx, p.y + dy);
    }
    public static RectF mapRect(RectF rect)
    {    	
        return new RectF( rect.left+dx,rect.top+dy,rect.right+dx,rect.bottom+dy);
    }
    
    void dxyControl()
    {
        dx *= 0.8F;
        dy *= 0.8F;
    }
    void dxyTo(Traces t)
    {
        dx = + t.X - M + dx;
        dy = + t.Y - M + dy;
    }
    //Moving the Map !
    void MapMove(int C)
    {
        dxyTo(traces[C]);

        for (int i = 0; i < maxT; ++i)
        {
            if (i == C) continue;
            traces[i].SetBy(traces[C]);
        }
        traces[C].SetCenter();

    }

    //actRect is to remind the player to act. Use this pen to draw that.
    static RectF actRect;
    Paint pen = new Paint(Color.TRANSPARENT);
    
    
    
    //Draw everything in the map.
    public void Draw(Canvas g) 
    {            

    	background.Draw(g);    	
    	
        if (colorcircle!=null) colorcircle.Draw(g);    	
        
        if (canAct)
        {
        	pen.setTextSize(15*k);
        	pen.setColor(MyColor.HSI(traces[canActTrace].ColorID, 1));
        	g.drawText("Go!", 0, H-70, pen);        	
        }
        
        if (superDashing())
        {
        	pen.setTextSize(20*k);
        	pen.setColor(MyColor.HSI(traces[snakeHeadTrace].ColorID, 1));
        	g.drawText("Duang!!!", 100, 500, pen);
        }
        
        Typeface ff=Typeface.create(Typeface.SERIF, Typeface.BOLD);
        pen.setTypeface(ff);    	
        
        if (Welcome)
        {        	
        	        	
        	pen.setTextSize(k*50);
        	g.drawText("CircleDash!!!", k*120, k*120, pen);
        	pen.setTextSize(k*30);
        	g.drawText("       ~~~È¦È¦³å´Ì~~~", k*120, k*200, pen);
//        	g.drawText("           1.3c", k*120, k*230, pen);
        	
        	return;
        }
    	

    	        
        pen.setTextSize(15*k);
        
        String s;
        if (leftTime > 0) s = "TIME: " + leftTime;
                    else s = "TIME OUT !";
        if (leftTime > 5) 
        {
        	pen.setColor(Color.BLACK);
        	g.drawText(s, 0, H-30, pen); 
        }
        else
        {
        	pen.setColor(Color.RED);
        	g.drawText(s, 0, H-30 ,pen);
        }


        s = "Circle:" + cntSkip;
        s += "    Dash:" + cntDash;
        if (cntDuang>0) s+="   Duang:"+cntDuang;
        
        pen.setColor(Color.BLACK);
        g.drawText(s,W/2,H-30,pen);
        //g.DrawString(s, ff, Brushes.Black, 200, H - 30);

        for (int i = 0; i < maxT; ++i) 
        	if (traces[i]!=null) traces[i].Draw(g);
        if (snake!=null) snake.Draw(g);
        
        
        if (!Playing)
        {
        	String s1,s2;
        	
        	pen.setTextSize(k*50);
            if (leftTime > 0)
            {
            	if (superDashing()) s1="Duang!!!"; else s1 = "CRASH!!!"; 
            	s2 = "";
            }
            else 
            {
            	s1 = "TIME OUT";
            	s2 = "";
            }
            
            g.drawText(s1,k*120, k*140, pen);


            int score = 5 * cntSkip + cntDash + 5 * cntDuang;
            pen.setTextSize(k*30);

            s2+="Your score: "+score;
            
            g.drawText(s2,k*120, k*190, pen);

        }
        
    }

    //Check whether the snake can skip.
    Boolean canAct = false;
    int canActTrace = 0;
    
    //control the clock
    public void Clock()
    {
        if (!Playing) return;

        leftTime -= deltaTime;
        if (leftTime < 0)
        {
            Playing = false;
            leftTime = 0;
            Guide.NextPhrases(cntSkip, cntDash, cntDuang);
        }
    }

    //Refresh here
    public Boolean Refresh()
    {
    	if (Welcome) return false;
    	
        if (!Playing) return false;

        dxyControl();

        if (justSpeedUp && snake.Dashing()) Action();
        else justSpeedUp = false;

        if (superDashing()) Action();
        
        for (int i = 0; i < maxT; ++i) traces[i].refreshed = false;

        
        Boolean keepPlayingFlag = snake.Refresh();
               
        Boolean canActFlag = false;
        for (int i = 0; i < maxT; ++i)
            if (snake.CanSkipTo(i,traces[i]))
            {
                canActTrace = i;
                canActFlag = true;
                break;
            }
        canAct = canActFlag;

        if (!keepPlayingFlag  && canAct && (snake.Running() || superDashing())) 
        {
        	justSpeedUp = false;
        	snake.SkipTo(canActTrace, traces[canActTrace]);  //Give the handle to the snake.
            snakeHeadTrace = canActTrace;          //move the head to the right trace.
            MapMove(canActTrace);                  //Move the Map     
            
        	keepPlayingFlag=true;
        	
//        	keepPlayingFlag = snake.Refresh();
        }
        
        
        for (int i = 0; i < maxT; ++i) traces[i].Refresh();
        
        
        if (!keepPlayingFlag) Playing = false;

        if (!keepPlayingFlag) Guide.NextPhrases(cntSkip, cntDash,cntDuang);
        
        return keepPlayingFlag;
    }

    //speed
    Boolean justSpeedUp = false;
    public void SpeedUp()
    {
        if (!Playing) return;

        ++cntDash;

        snake.SpeedUp();
        justSpeedUp = true;
    }

    float stopClockTime=9000;
    Boolean BoSuperDashing=false;
    public void SuperDash()
    {
    	++cntDuang;
    	BoSuperDashing=true;
    	snake.SuperDash();    	
    	stopClockTime = leftTime - 1.5F - ColorCircle.cnt;
    }
//    int cntSSS=0;
    Boolean superDashing()
    {
    	if (!BoSuperDashing) 
    	{
    		return false;    	
    	}
    	if (leftTime<stopClockTime) 
    	{
    		StopSuperDash();
    		return false;
    	}
    	return true;
    }
    void StopSuperDash()
    {
		BoSuperDashing=false;
		stopClockTime=9000;
		snake.StopSuperDash();
    }
    
    //action
    public void Action()
    {
    	Welcome=false;
    	
        if (!Playing) return;         
        if (!canAct) return;

        for (int i = 0; i < maxT; ++i)
            if (snake.CanSkipTo(i,traces[i]))
            {
                justSpeedUp = false;
                snake.SkipTo(i, traces[i]);  //Give the handle to the snake.
                snakeHeadTrace = i;          //move the head to the right trace.
                MapMove(i);                  //Move the Map  
                ++cntSkip;
                return;
            }
    }


}
