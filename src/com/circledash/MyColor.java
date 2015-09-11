package com.circledash;

import android.graphics.Color;

public class MyColor {
    //get a color here:
    //H = 0     red
    //H = 120   green;
    //H = 240   blue;
    static public int HSI(double H, double alpha)
    {
        double R = 0, G = 0, B = 0;
        double I = 150, S = 1, r60 = Math.PI / 3;

        if (H >= 0 && H < 120)
        {
            H = H / 180 * Math.PI;
            R = I * (1 + S * Math.cos(H) / Math.cos(r60 - H));
            B = I * (1 - S);
            G = 3 * I - R - B;
        }
        else if (H >= 120 && H < 240)
        {
            H = H / 180 * Math.PI;
            G = I * (1 + S * Math.cos(H - 2 * r60) / Math.cos(r60 * 3 - H));
            R = I * (1 - S);
            B = 3 * I - R - G;
        }
        else if (H >= 240 && H < 360)
        {
            H = H / 180 * Math.PI;
            B = I * (1 + S * Math.cos(H - 4 * r60) / Math.cos(r60 * 5 - H));
            G = I * (1 - S);
            R = 3 * I - G - B;
        }

        if (R > 255) R = 255;
        if (G > 255) G = 255;
        if (B > 255) B = 255;

        return Color.argb((int)(alpha * 255), (int)R, (int)G, (int)B);
    }
}
