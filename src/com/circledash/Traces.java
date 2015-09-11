package com.circledash;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.RectF;

public class Traces {
    //General status
    public static float minR = 54, maxR = 108;
    public static void SetR(float min, float max)
    {
        minR = min; maxR = max;
        V=max/27;
        Width=max/9;
        nearDis=max/5;
    }
    static float V = 4F;
    static float Width = 12;
    
    public float X, Y, R;
    //rect is for the ellipse.
    public RectF rect()
    {
        return new RectF(X - R + Map.X0, Y - R + Map.Y0, X + R + Map.X0, Y + R + Map.Y0);
    }             
    //Its V
    public float Vx, Vy;
    //chained means it's in the chain of the snake
    public Boolean Chained = false;

    //any trace can't be too far from the center
    static float firstMaxDis = maxR * 2;
    //trace would delay before get started.
    int cntDelay = 0;
    int maxDelay = 70;

    //Simple function to check whether a point is on this trace
    public Boolean OnEdge(PointF p)
    {
        double dR = Calc.Dis(p.x, p.y, X, Y) - R;
        if (Math.abs(dR) < Width) return true;
        return false;
    }

    static float nearDis = 20F;
    //Simple function to check whether two traces are too close to each other
    public Boolean NearTrace(Traces Tr)
    {
        if (Math.abs(R - Tr.R) > nearDis) return false;

        if (Calc.Dis(X, Y, Tr.X, Tr.Y) > nearDis) return false;

        return true;
    }

    //CONSTRUCT
    public Traces()
    {
        Reset();
    }
    public Traces(int ID)
    {
        switch (ID)
        {
            case 0:
                Reset();
                SetCenter();
                break;
        }
    }

    static Random random = new Random();        
    //reset everything
    void Reset()
    {
        R = minR+random.nextFloat()*(maxR-minR);
        //Get a good (x,y) and (Vx,Vy)
        do
        {
            double th = Math.PI * random.nextDouble();
            int side = random.nextInt(4);
            int L = random.nextInt(Map.A);
            switch (side)
            {
                case 0: //Up
                    X = L;
                    Y = 0;
                    Vx = V * (float)Math.cos(th);
                    Vy = V * (float)Math.sin(th);
                    break;
                case 1: //Down
                    X = L;
                    Y = Map.A;
                    Vx = V * (float)Math.cos(th);
                    Vy = -V * (float)Math.sin(th);
                    break;
                case 2: //Left
                    X = 0;
                    Y = L;
                    Vy = V * (float)Math.cos(th);
                    Vx = V * (float)Math.sin(th);
                    break;
                case 3: //Right
                    X = Map.A;
                    Y = L;
                    Vy = V * (float)Math.cos(th);
                    Vx = -V * (float)Math.sin(th);
                    break;
            }
        } while (Calc.DisLineToTrace(Vx, Vy, X, Y, Map.M, Map.M) > firstMaxDis);

        ColorID = random.nextInt(360);
        nowColor = MyColor.HSI(ColorID, 0.4);
        pen.setColor(nowColor);
        pen.setAntiAlias(true);
        pen.setStyle(Style.STROKE);
        
        Chained = false;

        cntDelay = random.nextInt(maxDelay);

    }
    //Set this trace as the center !
    public void SetCenter()
    {
        X = Map.M;
        Y = Map.M;
        Vx = 0;
        Vy = 0;
    }
    //Modify this trace by the center.
    public void SetBy(Traces Center)
    {
        X += Map.M - Center.X;
        Y += Map.M - Center.Y;
        Vx -= Center.Vx;
        Vy -= Center.Vy;

        if (Vx * Vx + Vy * Vy < V * V / 4)
        {
            Vx *= 2;
            Vy *= 2;
        }
    }

    //the color and draw the trace
    //TO-DO: STARS HERE!
    public int nowColor;
    public int ColorID;
    Paint pen = new Paint();
    public void Draw(Canvas g)
    {
    	pen.setStrokeWidth(Width);
        g.drawOval(Map.mapRect(rect()), pen);

    }

    

    public Boolean refreshed = false;
    //refresh normally
    public void Refresh()
    {
        if (refreshed) return;
        if (cntDelay > 0) { --cntDelay; return; }

        X += Vx; Y += Vy;
        
        if (X < 0 || X > Map.A || Y < 0 || Y > Map.A) Reset();

        refreshed = true;
    }
    //refresh and don't leave Tr. Change the MA of Tr at the same time.
    public void Refresh(Traces Tr, MovingArc MA)
    {
        if (refreshed) return;
        if (cntDelay > 0) { --cntDelay; return; }

        //try normal Move
        float pX = X + Vx, pY = Y + Vy;
        if (check(pX, pY, Tr, MA))
        {
            X = pX; Y = pY;
            MA.LastPos = Calc.DegFromYX(Y - Tr.Y, X - Tr.X);
            MA.LastDis = Calc.Dis(Y - Tr.Y, X - Tr.X);            
            refreshed = true;
            return;
        }

        //try Move and pull back
        float newPos = Calc.DegFromYX(pY - Tr.Y, pX - Tr.X);
        pX = Tr.X + (MA.LastDis) * Calc.Cos(newPos);
        pY = Tr.Y + (MA.LastDis) * Calc.Sin(newPos);
        if (check(pX, pY, Tr, MA))
        {
            X = pX; Y = pY;
            MA.LastPos = newPos;
            MA.LastDis = Calc.Dis(pY - Tr.Y, pX - Tr.X);
            refreshed = true;
            return;
        }

        //not Move
        if (MA.LastDis > Tr.R + R - Width / 2 ) MA.LastDis = Tr.R + R - Width / 2 ;
        X = Tr.X + (MA.LastDis) * Calc.Cos(MA.LastPos);
        Y = Tr.Y + (MA.LastDis) * Calc.Sin(MA.LastPos);
        refreshed = true;
    }
    //whether new position(x,y) leave Tr
    Boolean check(float x, float y, Traces Tr, MovingArc MA)
    {
        float d = Calc.Dis(x, y, Tr.X, Tr.Y);
        if (!Calc.TwoTraces(x, y, R, Tr.X, Tr.Y, Tr.R)) return false;
        if (d > Tr.R + R - Width / 2) return false;
        if (Math.abs(d - Math.abs(Tr.R - R)) < Width / 2) return false;
       
        if (MA.sweepAngle > 90) return true;

        AngleRecord AR = Calc.TwoTracesAngles(Tr.X, Tr.Y, Tr.R, x, y, R);
        float a2;
        if (MA.LinkingEndID == 1) a2 = AR.ang11; else a2 = AR.ang12;
        if (MA.sweepAngle * Calc.DegDec(MA.startAngle, a2) > 0) return true;

        return false;
    }
}
