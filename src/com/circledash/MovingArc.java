package com.circledash;

public class MovingArc {
    //necessary information for a Arc
    public float startAngle, sweepAngle;
    //0 means head/tail, 1 means a11/a21 linked, 2 means a12/a22 linked.
    public int LinkingStartID = 0, LinkingEndID = 0;
    //record the last Position, an angle to the next Trace linked.
    public float LastPos, LastDis;   

    public float Ss()
    {
    	if (sweepAngle>0) return 1;
    	if (sweepAngle<0) return -1;
    	if (sweepAngle==0) return 0;
    	return 0;
    }
    
    public void Reset()
    {
        startAngle = 0;
        sweepAngle = 0;
        LastPos = 0;
        LastDis = 0;
        LinkingStartID = 0;
        LinkingEndID = 0;
    }

    public String ToString()
    {
        return startAngle +
            " (" + sweepAngle + ")" ;
    }

    public Boolean HaveAngle(float a)
    {
 //   	if (Math.abs(Calc.DegDec(Calc.DegFromDeg(a), startAngle))<0.1) return true;
    	
        float d = Calc.DegTo(startAngle, Calc.DegFromDeg(a), Math.signum(sweepAngle));
        return Math.abs(d) < Math.abs(sweepAngle);
    }
//       public Boolean Intersect(MovingArc MA)
//       {
//           if (MA.HaveAngle(startAngle)) return true;
//           if (MA.HaveAngle(startAngle + sweepAngle)) return true;

//           if (HaveAngle(MA.startAngle)) return true;
//           if (HaveAngle(MA.startAngle + sweepAngle)) return true;

//           return false;
//       }

    public Boolean Fine()
    {
    	//check NaN
        if (startAngle!=startAngle) return false;
        if (sweepAngle!=sweepAngle) return false;

        return true;
    }
}
