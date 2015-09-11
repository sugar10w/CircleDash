package com.circledash;

import android.graphics.Point;

public class Calc {
    //Deg and Rad
    static public float DegFromDeg(double r)
    {
        float d = (float)r;
        while (d < 0) d += 360;
        while (d > 360) d -= 360;
        return d;
    }
    static public float DegFromRad(double r)
    {
        float d = (float)r / (float)Math.PI * 180;
        return DegFromDeg(d);
    }        
    static public float RadFromDeg(double d)
    {
        return  (float)d / 180 * (float)Math.PI;
    }

    //return the detraction of two Deg.  Ensure Abs( ) < 180.
    static public float DegDec(float st, float ed)
    {
        if (Math.abs(ed - st) <= 180) return ed - st;
        if (ed <= 180) ed += 360; else st += 360;
        return ed - st;
    }
    //return the detraction of two Deg.  Ensure the sign is same with s.
    static public float DegTo(float st, float ed, float f)
    {
        float sw = ed - st;
        if (f < 0 && sw > 0) sw -= 360;
        else if (f > 0 && sw < 0) sw += 360;
        return sw;
    }

    //Cos,Sin in Deg
    static public float Cos(double x)
    {
        return (float)Math.cos(RadFromDeg(x));
    }
    static public float Sin(double x)
    {
        return (float)Math.sin(RadFromDeg(x));
    }

    //Atan in Deg
    static public float DegFromYX(double y, double x)
    {
        return DegFromRad(Math.atan2(y, x));
    }
    static public float DegFromPoint(Point p)
    {
        return DegFromRad(Math.atan2(p.y, p.x));
    }

    //Distance
    static public float Dis(float x, float y)
    {
        return (float)Math.sqrt(x * x + y * y);
    }
    static public float Dis(float x0, float y0, float x1, float y1)
    {
        return (float)Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0));
    }

    //Distance between Line and Point
    static public float DisLineToTrace(float dx, float dy, float x0, float y0, float x1, float y1)
    {
        //(dx,dy)X(x1-x0,y1-y0) / dis()
        return (float)Math.abs(dx * (y1 - y0) - dy * (x1 - x0)) / Dis(dx, dy);
    }
    static public float DisLineToTrace(float A, float B, float C, float x0, float y0)
    {
        //A*x+B*y+C=0
        return (float)Math.abs(A * x0 + B * y0 + C) / Dis(A, B);
    }

    //Whether two circles intersect
    static public Boolean TwoTraces(Traces t1,Traces t2)
    {
    	if (t1==null || t2==null) return false;
        return TwoTraces(t1.X, t1.Y, t1.R, t2.X, t2.Y, t2.R);
    }
    static public Boolean TwoTraces(float x1, float y1, float r1, float x2, float y2, float r2)
    {
        float d = Dis(x1, y1, x2, y2);
        if (d < Math.abs(r1 - r2)) return false;
        if (d > r1 + r2) return false;
        return true;
    }

    //Get two intersection points of two Traces. 
    //Attention, t1 and t2 can't be swapped! t1 is to the head and t2 is to the tail.
    static public AngleRecord TwoTracesAngles(Traces t1, Traces t2) 
    {
        return TwoTracesAngles(t1.X, t1.Y, t1.R, t2.X, t2.Y, t2.R);
    }
    static public AngleRecord TwoTracesAngles(float x1, float y1, float r1, float x2, float y2, float r2)
    {
        float dis1, delta1, dCenter1,
            ax1, ay1, ax2, ay2,
            a11, a12, a21, a22,
            A, B, C;

        //public line
        A = 2 * (-x1 + x2);
        B = 2 * (-y1 + y2);
        C = x1 * x1 - x2 * x2 + y1 * y1 - y2 * y2 - r1 * r1 + r2 * r2;

        //a1_
        dCenter1 = DegFromYX(-y1 + y2, -x1 + x2);
        dis1 = DisLineToTrace(A, B, C, x1, y1);
        //closer and same side
        if (DisLineToTrace(A, B, C, x1, y1) < DisLineToTrace(A, B, C, x2, y2)
            && (( A * x1 + B * y1 + C >= 0 && A * x2 + B * y2 + C >= 0 )
            	|| ( A * x1 + B * y1 + C <= 0 &&  A * x2 + B * y2 + C <= 0  ))
            )
            dis1 = -dis1;
        delta1 = DegFromRad(Math.acos(dis1 / r1));

        a11 = DegFromDeg(dCenter1 - delta1);
        a12 = DegFromDeg(dCenter1 + delta1);

        //a2_
        ax1 = x1 + r1 * Cos(a11); ay1 = y1 + r1 * Sin(a11);
        ax2 = x1 + r1 * Cos(a12); ay2 = y1 + r1 * Sin(a12);
        a21 = DegFromYX(ay1 - y2, ax1 - x2);
        a22 = DegFromYX(ay2 - y2, ax2 - x2);

        return new AngleRecord(a11, a12, a21, a22);
    }
}
