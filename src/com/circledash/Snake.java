package com.circledash;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;

public class Snake {
    //the handles of traces the snake is in. 
    int maxT = 20;
    public Traces[] chain = new Traces[maxT];
    //snake is in chainTail < i <= chainHead
    int chainHead, chainTail;
    //more details: chainID is the ID of the trace in the map. 
    // And chainArc[] record startAngles and sweepAngles.
    int[] chainID = new int[maxT];          
    MovingArc[] chainArc = new MovingArc[maxT];
    
    ColorCircle colorcircle;
    
    //Simple functions for the ID of chain.
    int next(int i)
    {
        ++i;
        if (i == maxT) i = 0;
        return i;
    }
    int prcd(int i)
    {
        --i;
        if (i == -1) i = maxT - 1;
        return i;
    }

    //snake's size
    static float V = 8F, minV = 8F, ConstMinV = 8F, maxV = 48F;
    static float Length;
    static float deltaLength = 20F;
    static float Width = 20;
    static float k=1;
   
    public static void setSize(float k0)
    {
    	k=k0;
    	minV=k*8; ConstMinV = k*8; maxV=k*48; 
    	deltaLength=k*20;
    	Width=k*20;
    }
    
    Random random = new Random();
    //no CONSTRUCT
    //

    //Reset
    public void Reset(int id, Traces firstChain,ColorCircle cc)
    {
        for (int i = 0; i < maxT; ++i) chain[i] = null;

        colorcircle=cc;
        colorcircle.Get(firstChain.ColorID);
        
        chainHead = 0; chainTail = maxT - 1;
        chain[0] = firstChain; chainID[0] = id;       
        
        ColorID = chain[0].ColorID;
        toColorID = ColorID;
        pen.setColor( MyColor.HSI(ColorID,1));
        pen.setAntiAlias(true);
        pen.setStyle(Style.STROKE);
        pen.setStrokeCap(Cap.ROUND);
        pen.setStrokeJoin(Join.ROUND);
        pen.setStrokeWidth(Width);        

        for (int i = 0; i < maxT; ++i) chainArc[i] = new MovingArc();

        Length = (float)Math.PI * (Traces.maxR - 1);

        chainArc[chainHead].startAngle = random.nextInt(360);
        chainArc[chainHead].sweepAngle = Length / chain[chainHead].R * 180 / (float)Math.PI;
        
        minV=ConstMinV;
        V=minV;
    }

    //Simple function to find the head
    float headAngle()
    {
        return chainArc[chainHead].startAngle;
    }
    public PointF HeadPoint()
    {
        return new PointF(chain[chainHead].R * (float)Math.cos(headAngle() / 180 * Math.PI) + Map.M,
            chain[chainHead].R * (float)Math.sin(headAngle() / 180 * Math.PI) + Map.M);
    }

    //whether can skip
    public Boolean CanSkipTo(int ID, Traces nextChain)
    {
    	if (nextChain==null) return false;
    	if (chain[chainHead]==null) return false;
    	if (Math.abs(chainArc[chainHead].sweepAngle)<1) return false;    	
    	if (ID==chainID[chainHead]) return false;
    	
        if ((
            !chain[chainHead].NearTrace(nextChain) &&
            Calc.TwoTraces(chain[chainHead], nextChain) &&
            nextChain.OnEdge(HeadPoint())
            ) == false) return false;

                
        //Try to Skip
        AngleRecord AR = Calc.TwoTracesAngles(nextChain, chain[chainHead]);
                        
        if (Math.abs(Calc.DegDec(headAngle() , AR.ang21)) < Math.abs(Calc.DegDec(headAngle() , AR.ang22)))
        {   
        	if (Math.abs(Calc.DegDec(headAngle()+chainArc[chainHead].sweepAngle , AR.ang21))<0.1) return false;
        	if (Math.abs(Calc.DegDec(headAngle() , AR.ang21))>40) return false;
        	if (checkAngle(ID, AR.ang11+5) && checkAngle(ID, AR.ang11-5) ) return false;
//            if (Math.abs(chainArc[chainHead].sweepAngle)<50 && ID==chainID[prcd(chainHead)] && chainArc[chainHead].LinkingEndID==2) return false;
        }
        else
        {
        	if (Math.abs(Calc.DegDec(headAngle()+chainArc[chainHead].sweepAngle , AR.ang22))<0.1) return false;
        	if (Math.abs(Calc.DegDec(headAngle() , AR.ang22))>40) return false;
        	if (checkAngle(ID, AR.ang12+5) && checkAngle(ID, AR.ang12-5) ) return false;
//            if (Math.abs(chainArc[chainHead].sweepAngle)<50 && ID==chainID[prcd(chainHead)] && chainArc[chainHead].LinkingEndID==1 ) return false;
        }
               
        return true;
    }
    
    float t1,t2;
    
    Boolean skipping=false;
    //Let the snake skip to that chain!
    public void SkipTo(int ID,Traces nextChain)
    {
    	if (skipping) return;
    	
    	skipping=true;
    	
    	colorcircle.Get(nextChain.ColorID);
    	
        AngleRecord AR = Calc.TwoTracesAngles(nextChain, chain[chainHead]);

        int newHead = next(chainHead);
        if (Math.abs(Calc.DegDec( headAngle() , AR.ang21)) < Math.abs(Calc.DegDec(headAngle(), AR.ang22)))
        {
            chainArc[chainHead].LinkingStartID = 1;
            chainArc[chainHead].startAngle = AR.ang21;

            chainArc[newHead].LinkingStartID = 0;
            chainArc[newHead].LinkingEndID = 1;
            chainArc[newHead].LastPos = Calc.DegFromYX(Map.M - nextChain.Y, Map.M - nextChain.X);
            chainArc[newHead].LastDis = Calc.Dis(Map.M - nextChain.Y, Map.M - nextChain.X);

//            if (!nextChain.Chained)
//            {
//                chainArc[newHead].startAngle = AR.ang11 - 0.1F;
//                chainArc[next(chainHead)].sweepAngle = +0.1F;
//            }
//            else
            {
            
            	t2=0.1F;t1=0.1F;
            	while (!checkAngle(ID, AR.ang11 + t2) && t2<360) t2+=5;            
            	while (!checkAngle(ID, AR.ang11 - t1) && t1<360) t1+=5;            
            	if (t1>60 || t1>=t2)
            	{
            		chainArc[newHead].startAngle = AR.ang11 - 0.1F;
            		chainArc[next(chainHead)].sweepAngle = +0.1F;
            	}
            	else
            	{
            		chainArc[newHead].startAngle = AR.ang11 + 0.1F;
            		chainArc[next(chainHead)].sweepAngle = -0.1F;

            	}
            
            }
            
            
//            if (!checkAngle(ID, AR.ang11 - 30))
//            {
 //               chainArc[newHead].startAngle = AR.ang11 - 0.1F;
//                chainArc[newHead].sweepAngle = 0.1F;
//                chainArc[newHead].Ss=1;
//            }
 //           else if (!checkAngle(ID, AR.ang11 + 30))
  //          {
 //               chainArc[newHead].startAngle = AR.ang11 + 0.1F;
 //               chainArc[newHead].sweepAngle = -0.1F;
 //               chainArc[newHead].Ss=-1;
 //           }
 //           else
 //          {
 //               chainArc[newHead].startAngle = AR.ang11 - 0.1F;
  //              chainArc[newHead].sweepAngle = 0.1F;
 //               chainArc[newHead].Ss=1;
  //          }
         }
        else
        {
            chainArc[chainHead].LinkingStartID = 2;
            chainArc[chainHead].startAngle = AR.ang22;

            chainArc[newHead].LinkingStartID = 0;
            chainArc[newHead].LinkingEndID = 2;
            chainArc[newHead].LastPos = Calc.DegFromYX(Map.M - nextChain.Y, Map.M - nextChain.X);
            chainArc[newHead].LastDis = Calc.Dis(Map.M - nextChain.Y, Map.M - nextChain.X);

 //           if (!nextChain.Chained)
 //           {
 //               chainArc[newHead].startAngle = AR.ang12 + 0.1F;
 //               chainArc[next(chainHead)].sweepAngle = -0.1F;
  //          }
  //          else
            {
            
            	t2=0.1F;t1=0.1F;
            	while (!checkAngle(ID, AR.ang12 + t2) && t2<360) t2+=5;            
            	while (!checkAngle(ID, AR.ang12 - t1) && t1<360) t1+=5;            
            	if (t2>60 || t2>=t1)
            	{
            		chainArc[newHead].startAngle = AR.ang12 + 0.1F;
            		chainArc[next(chainHead)].sweepAngle = -0.1F;
            	}
            	else
            	{
            		chainArc[newHead].startAngle = AR.ang12 - 0.1F;
            		chainArc[next(chainHead)].sweepAngle = +0.1F;
            	}
            
            }
        }

       
        //Add the Trace to the chain
        chainHead = next(chainHead);
        if (chainHead == chainTail) chainTail = next(chainTail);
        chain[chainHead] = nextChain; chainID[chainHead] = ID;  //chain may be not large enough.

        nextChain.Chained=true;
        
        Length += deltaLength;

        toColorID = nextChain.ColorID;
        
        skipping=false;
    }

    //Change the Color.
    float ColorID, toColorID;
    Paint pen = new Paint(Color.WHITE);
    //Draw the snake.
    public void Draw(Canvas g)
    {            
    	int i;
    	
    	//data
        Paint pen2=new Paint();
        pen2.setAntiAlias(true);
        Typeface ff=Typeface.create(Typeface.SERIF, Typeface.BOLD);
        pen2.setTypeface(ff);
        pen2.setTextSize(k*15);        
        
        String s="L="+Length + " V="+V;        
        g.drawText(s, 0, k*18, pen2);
         
        if (chain[chainHead]==null)
        {
        	g.drawText("小蛇找不到了  0)^(0 ... " , 0, k*36, pen2);
        	return;
        }
        
        i = chainHead;
        float t  = k*36;
        while (i != chainTail)
        {
        	if (chainArc[i].startAngle==0 && chainArc[i].sweepAngle==0)
        	{
        		i=prcd(i);
        		continue;
        	}
            if (chain[i] != null) 
                pen2.setColor(MyColor.HSI(chain[i].ColorID, 0.6));
            g.drawText(chainArc[i].ToString(), 0, t, pen2);
            t += k*18;
            i = prcd(i);
        }    	
        pen2.setColor(MyColor.HSI(chain[chainHead].ColorID,0.6));
        g.drawText("<"+((int)t1)+","+((int)t2)+">", 0, t, pen2);
        
    	//draw the snake   	
        Path path = new Path();
        i = chainHead;
        while (i != chainTail)
        {
            if (chain[i]!=null)
                path.addArc(Map.mapRect(chain[i].rect()),
                    chainArc[i].startAngle,
                    chainArc[i].sweepAngle);
            i = prcd(i);
        }            
        pen.setStrokeWidth(Width);
        g.drawPath(path, pen);

        Paint pen3=new Paint();
        pen3.setAntiAlias(true);
        pen3.setColor(pen.getColor());
        
        //draw the head
        Path head=new Path();
        PointF headP=HeadPoint();
        float x0,y0, 
        		x1,y1,x2,y2,x3,y3,
        		th=chainArc[chainHead].startAngle,
        		S=chainArc[chainHead].Ss();
        
        x0=headP.x+Map.X0+Map.dx+0.1F*S*Width*Calc.Sin(th);
        y0=headP.y+Map.Y0+Map.dy-0.1F*S*Width*Calc.Cos(th);
                
        x1=x0+S*Width*Calc.Sin(th);
        y1=y0-S*Width*Calc.Cos(th);
                
        x2=x0+S*Width*(-Calc.Sin(th)-Calc.Cos(th));
        y2=y0-S*Width*(-Calc.Cos(th)+Calc.Sin(th));
        
        x3=x0+S*Width*(-Calc.Sin(th)+Calc.Cos(th));
        y3=y0-S*Width*(-Calc.Cos(th)-Calc.Sin(th));
        		
        head.moveTo(x1, y1);
        head.lineTo(x2, y2);
        head.lineTo(x3, y3);
        head.lineTo(x1, y1);
        
        g.drawPath(head, pen3);
                

    }
    public String ToString()
    {
        String s = "";
        int i = chainHead;
        while (i != chainTail)
        {
            s += chainArc[i].ToString();
            i = prcd(i);
        }
        return s;
    }

    //check whether a certain angle has been used.
    Boolean checkAngle(int ID,float ang)
    {
    	ang=Calc.DegFromDeg(ang);
        for (int j = 0; j < maxT; ++j)
            if (j != chainHead && chainArc[j].Fine() && ID == chainID[j])
                if (chainArc[j].HaveAngle(ang)) return true;

        return false;
    }

    int cntStop=0;
    //Refresh the snake !
    public Boolean Refresh()
    {
    	if (skipping) {
    		++cntStop;
    		if (cntStop<3) return true;
    	}
    	cntStop=0;
    	skipping=true;
    	
        colorControl();
        speedControl();

        //Refresh all the chains first
        int i = chainHead;
        chain[i].Refresh();
        i = prcd(i);
        while (i != chainTail)
        {
            if (chain[i] != null)
                chain[i].Refresh(chain[next(i)], chainArc[next(i)]);
            else { chainTail = i; break; }
            i = prcd(i);
        }

        //Refresh the headAngle

        chainArc[chainHead].startAngle -= Calc.DegFromRad(V / chain[chainHead].R) * chainArc[chainHead].Ss();
        chainArc[chainHead].startAngle = Calc.DegFromDeg(chainArc[chainHead].startAngle);

        //Refresh chainArc        
        float L = Length;
        i = chainHead;
        //newChainTail == -1 means no new chainTail.
        int newChainTail = -1;

        Boolean keepPlayingFlag = true;
        //Check GameOver. Crashed itself.
        float lastHeadSweepAngle = chainArc[chainHead].sweepAngle;
        if (Math.abs(lastHeadSweepAngle) > 270)
        {
            float newHeadSweepAngle = 0;
            if (chain[prcd(chainHead)] == null) chainArc[chainHead].LinkingEndID = 0;
            switch (chainArc[chainHead].LinkingEndID)
            {
                case 0:
                    newHeadSweepAngle = Calc.DegFromRad(L / chain[chainHead].R) * chainArc[chainHead].Ss();
                    break;
                case 1:
                    newHeadSweepAngle = Calc.DegTo(
                        chainArc[chainHead].startAngle,
                        Calc.TwoTracesAngles(chain[chainHead], chain[prcd(chainHead)]).ang11, 
                        chainArc[chainHead].Ss());
                    break;
                case 2:
                    newHeadSweepAngle = Calc.DegTo(
                          chainArc[chainHead].startAngle,
                          Calc.TwoTracesAngles(chain[chainHead], chain[prcd(chainHead)]).ang12,
                          chainArc[chainHead].Ss());
                    break;
            }
            if (Math.abs(newHeadSweepAngle) < 90)
            {
                chainArc[chainHead].sweepAngle = 359.9999F * chainArc[chainHead].Ss();
                i = prcd(chainHead);
                L -= chain[chainHead].R * Calc.RadFromDeg(359.9999);
                keepPlayingFlag = false;
            }
        }

              
        //Get to Refresh chainArc
        while (i != chainTail)
        {
            //TO-DO: Something strange here... isNaN,isInf
            if (!chainArc[i].Fine()) L = -1;

            //if L has been used up. Free the chains left.
            if (L <= 0) 
            {
                chain[i].Chained = false; chain[i] = null; 
                chainID[i]=-1;
                chainArc[i].Reset(); 
                i = prcd(i); continue; 
            }
            
            AngleRecord AR;
            float Lneeded, newSweep;
            int S = (int) chainArc[i].Ss(); 
            
            if (chain[prcd(i)] == null) chainArc[i].LinkingEndID = 0;
            
            if (chainArc[i].LinkingEndID!=0)
            	if (!Calc.TwoTraces(chain[i],chain[prcd(i)]))
            		chainArc[i].LinkingEndID=0;
            
            //Refresh snake chain by chain
            switch (chainArc[i].LinkingEndID)
            {
                case 0:     //chainArc[i] is the Tail.
                    chainArc[i].sweepAngle = Calc.DegFromRad(L / chain[i].R) * S;
                    L=-1;
                    break;
                case 1:     //chainArc[i]'s end is linked by ang_1
                    AR=Calc.TwoTracesAngles(chain[i],chain[prcd(i)]);
                    
                    //Bug2901
                    if (Math.abs(chainArc[i].sweepAngle)>30)
                    	newSweep = Calc.DegTo(chainArc[i].startAngle, AR.ang11, S);
                    else newSweep=Calc.DegDec(chainArc[i].startAngle, AR.ang11);
                    //Bug2902
                    if (Math.abs(chainArc[i].sweepAngle)>300 && Math.abs(newSweep)<30)
                    	newSweep=359.9F*chainArc[i].Ss();                   
                    
                    Lneeded = chain[i].R * Calc.RadFromDeg(Math.abs(newSweep));
                    if (L - Lneeded <= 0)
                    {
                        //L used up here. Here's a new chainTail.                            
                        chainArc[i].sweepAngle = Calc.DegFromRad(L / chain[i].R) * S;                            
                        chainArc[i].LinkingEndID = 0;
                        L = -1;
                        newChainTail = prcd(i);
                    }
                    else
                    {
                        L -= Lneeded;
                        chainArc[i].sweepAngle = newSweep;
                        chainArc[prcd(i)].startAngle = AR.ang21;
                    }                      
                    break;
                case 2:     //chainArc[i]'s end is linked by ang_2
                    AR=Calc.TwoTracesAngles(chain[i],chain[prcd(i)]);
                    
                    //Bug2901
                    if (Math.abs(chainArc[i].sweepAngle)>30)
                    	newSweep = Calc.DegTo(chainArc[i].startAngle, AR.ang12, S);
                    else newSweep=Calc.DegDec(chainArc[i].startAngle, AR.ang12);
                    //Bug2902
                    if (Math.abs(chainArc[i].sweepAngle)>300 && Math.abs(newSweep)<30) 
                    	newSweep=359.9F*chainArc[i].Ss();       
                    
                    Lneeded = chain[i].R * Calc.RadFromDeg(Math.abs(newSweep));
                    if (L - Lneeded <= 0)
                    {
                        //L used up here. Here's a new chainTail.                            
                        chainArc[i].sweepAngle = Calc.DegFromRad(L / chain[i].R) * S;                        
                        chainArc[i].LinkingEndID = 0;
                        L = -1;
                        newChainTail = prcd(i);
                    }
                    else
                    {
                        L -= Lneeded;
                        chainArc[i].sweepAngle = newSweep;
                        chainArc[prcd(i)].startAngle = AR.ang22;
                    }
                    break;
            }
            //get to the next
            i = prcd(i);
        }

        if (newChainTail != -1) chainTail = newChainTail;

        //Check GameOver. Crashed different routes     
        if (keepPlayingFlag)
        	if (checkAngle(chainID[chainHead], chainArc[chainHead].startAngle)) 
        		keepPlayingFlag = false;
        
        skipping=false;
        
        return keepPlayingFlag;
    }

    //Change the Color
    void colorControl()
    {
        float delta = Calc.DegDec(ColorID, toColorID);
        if (delta > 20)
        {
            ColorID = Calc.DegFromDeg(ColorID + 20);
        }
        else if (delta < -20)
        {
            ColorID = Calc.DegFromDeg(ColorID - 20);
        }        
        
        pen.setColor(MyColor.HSI(ColorID,0.9));
        
        if (pen.getColor()==Color.BLACK) pen.setColor(MyColor.HSI(chain[chainHead].ColorID,0.9 ));
    }

    //Speed up!
    public void SpeedUp()
    {
        V = maxV;
    }
    public void SuperDash()
    {
    	V=maxV*0.7F;
    	minV=V;
    }
    public void StopSuperDash()
    {
    	minV=ConstMinV;
    }
    
    //speed control
    void speedControl()
    {
        if (V <= minV) { V = minV; return; }
        else V *= 0.7F;
    }
    public Boolean Dashing()
    {
        return V > maxV * 0.4;
    }
    public Boolean Running()
    {
    	return V > minV;
    }
}
