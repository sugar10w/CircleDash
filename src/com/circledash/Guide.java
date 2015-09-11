package com.circledash;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

public class Guide {

	static int phrases=3,sentences=12;
	static String[][] st = new String[phrases][sentences];
	static int[][] cnt=new int[phrases][sentences];
	static int[] cntSentences=new int[phrases];
	static float k=1;
	
	public static Boolean usingGuide=false; 
	
	static int nowPhrases=0,nowSentences=0,nowCnt=0;
	
	//CONSTRUCT
	public Guide()
	{
	//--------------------------	
		st[0][0]="~~~~~~~新手点这里~~~~~~~";
		st[0][1]="您好，我是超级向导~~  \r\n欢迎页面，触碰任意位置 进入游戏。";		
		st[0][2]="正在转圈的箭头，就是游戏的主角小蛇~~ \r\n触碰任意位置 将触发小蛇的一次冲刺。";	
		st[0][3]="冲刺过程中遇到路过的圆，\r\n小蛇就会立即跳转到新的圆上。  \r\n试试看！圆不久就会围过来的。";	
		st[0][4]="在跳跃的一瞬间，小蛇的蛇身会变长。\r\n作为一条贪吃蛇..它不能撞到自己！\r\n(蛇头进入同一个圆上的蛇身)\r\n想让小蛇不撞到自己，就赶紧刷屏吧！";	
		st[0][5]="每次Dash冲刺和Circle圆之间的跳跃，都会为你带来分数。\r\n左下角还有时间么?我是不是讲得太多了...==";	
		st[0][6]="加油哦~~";
		
		cntSentences[0]=0;
		
		for (int i=0;i<6;++i) cnt[0][i]=1;
		cnt[0][6]=0;
		
	//--------------------------		
		st[1][0]="";
		st[1][1]="游戏结束页面， 触摸10次  或 长按屏幕 重新开始游戏。\r\n刚刚的话太多了，为了向你道歉....";
		st[1][2]="游戏结束页面， 触摸10次  或 长按屏幕 重新开始游戏。\r\n刚刚的话太多了，为了向你道歉.... \r\n马上告诉你高分秘诀哦~！！！";
		st[1][3]="当然是，狂撸屏幕啦~";
		st[1][4]="当然是，狂撸屏幕啦~！";
		st[1][5]="当然是，狂撸屏幕啦~！！也可以试试长按屏幕 ";
		
		st[1][6]="当然是，狂撸屏幕啦~！！也可以试试长按屏幕";
		st[1][7]="当然是，狂撸屏幕啦~！！也可以试试长按屏幕\r\n圆都围上来好开心！";
		st[1][8]="当然是，狂撸屏幕啦~！！也可以试试长按屏幕\r\n圆都围上来好开心！圆少的时候也不错";
		st[1][9]="当然是，狂撸屏幕啦~！！也可以试试长按屏幕\r\n圆都围上来好开心！圆少的时候也不错\r\n若遇灵异事件，请清理内存后重新开始";
		st[1][10]="当然是，狂撸屏幕啦~！！也可以试试长按屏幕\r\n圆都围上来好开心！圆少的时候也不错\r\n若遇灵异事件，请清理内存后重新开始\r\n请保护你的屏幕~~~以及你的手指~~~";
		st[1][11]="当然是，狂撸屏幕啦~！！也可以试试长按屏幕\r\n圆都围上来好开心！圆少的时候也不错\r\n若遇灵异事件，请清理内存后重新开始\r\n请保护你的屏幕~~~以及你的手指~~~";
		
		cntSentences[1]=12;
		for (int i=0;i<=2;++i) cnt[1][i]=1;
		for (int i=3;i<=11;++i) cnt[1][i]=2;
		cnt[1][2]=10-2;
		
	//--------------------------	
		st[2][0]="..";
		st[2][1]="...";
		st[2][2]="还有";
		st[2][3]="还有 还有";
		st[2][4]="还有 还有 还有";
		
		st[2][5]="游戏结束页面，触摸10次  或 长按屏幕 重新开始游戏。";

		st[2][6]="游戏结束页面， 触摸10次  或 长按屏幕 重新开始游戏。\r\n 不要忘记截屏哦~";
		st[2][7]="游戏结束页面， 触摸10次  或 长按屏幕 重新开始游戏。\r\n 不要忘记截屏哦~~";
		st[2][8]="游戏结束页面， 触摸10次  或 长按屏幕 重新开始游戏。\r\n 不要忘记截屏哦~~~";
		st[2][9]="开心就好~~~ ";
		
		st[2][10]="";
		
		cntSentences[2]=11;
		for (int i=0;i<=9;++i) cnt[2][i]=1;	
		cnt[2][10]=0;
		
	}
	
	static public void SetSize(float k0){
		k=k0;
	}
	
	static int max=0,maxc=0,maxd=0,Gcnt=0;
	static public void NextPhrases(int c,int d,int dd)
	{
		++Gcnt;
		if (c*5+d+5*dd>max)
		{
			max=c*5+d+5*dd;
			maxc=c;
			maxd=d;
		}
		
		if (!usingGuide) return;
		
		if (nowPhrases==0 && nowSentences==6)
		{
			nowPhrases=1;
			nowSentences=0;
			nowCnt=0;
			st[1][0]="哦！ 你已经完成了你的第一次游戏~~ "
					+"\r\nCircle计5分，Dash计1分"
					+"\r\n所以小蛇越活跃，得分越高！";
		}
		else
		{
			if (nowPhrases==2 && nowSentences==10 ) return;
			st[nowPhrases][nowSentences]+="\r\n这个，我还没讲玩呢。。。 先继续点下去吧。。。";
		}
	}
	
	public void Action(){
		++nowCnt; if (nowCnt>10) nowCnt=10;		
		
		if (nowPhrases==2 && nowSentences==10 && nowCnt==0) 
		{
			st[nowPhrases][nowSentences]="";
			nowCnt=1;
		}
		
		if (nowCnt==cnt[nowPhrases][nowSentences]) 
		{
			nowCnt=0;
			++nowSentences;
			
			if (nowSentences==cntSentences[nowPhrases]) 
			{
				++nowPhrases;
				nowSentences=0;
			}
			
		}
	}
	
	Random random=new Random();
	static int r=-1;
	public void RandomGo()
	{		
		if (nowPhrases==2 && nowSentences==10) 
		{	
			++r; r%=14;
			
			switch (r)
			{
			case 0:
				st[nowPhrases][nowSentences]="Hello，点击屏幕上侧，随时听您吩咐~";				
				break;							
			case 1:
				st[nowPhrases][nowSentences]="右下角的色环，会收集你曾经过的圆环的颜色";
				break;
			case 2:
				st[nowPhrases][nowSentences]="低速状态下过不去的坎，有时可以提前连击几下冲过去。";
				break;	
			case 3:
				st[nowPhrases][nowSentences]="如果觉得有点卡，可以触碰屏幕最最右下角关闭背景。";
				break;	
			case 4:
				st[nowPhrases][nowSentences]="如果小蛇忽然从屏幕上消失...那就找不到了...刷刷Dash吧...";
				break;	
			case 5:
				st[nowPhrases][nowSentences]="你当前的最高分在这里:\r\n"+max+",Circle="+maxc+",Dash="+maxd;
				break;	
			case 6:
				st[nowPhrases][nowSentences]="哇哦，你已经完成了"+ColorCircle.cnt+"个色环呢。";
				break;
			case 7:
				st[nowPhrases][nowSentences]="你能看到色环还缺什么颜色么？";				
				break;
			case 8:
				st[nowPhrases][nowSentences]="诶哟，你已经玩了"+Gcnt+"盘了，注意视力哦";
				break;		
			case 9:
				st[nowPhrases][nowSentences]="想知道色环等级有什么用么？\r\nDuang~Duang~Duang~~~";
				break;
			case 10:
				st[nowPhrases][nowSentences]="小蛇会限制所在圆环的运动哦，具体的自己观察吧。";
				break;	
			case 11:
				st[nowPhrases][nowSentences]="谢谢你一直点到这里...";
				break;				
			case 12:
				st[nowPhrases][nowSentences]="我也不知道该说什么了啦~";				
				break;
			case 13:
				st[nowPhrases][nowSentences]="";				
				break;	
			}
			nowCnt=-7;
			
		}
		
		
	}
	
	static int len=24;

	public void Draw(Canvas g){
		Paint pen=new Paint();
		pen.setAntiAlias(true);
		pen.setTextSize(15*k);
		
		Typeface ff=Typeface.create(Typeface.SERIF, Typeface.NORMAL);
		pen.setTypeface(ff);
		
		String s=st[nowPhrases][nowSentences];
		
		
		float l=Map.W/3F;
		float h=15*k;
		
//		for (i=0;i<s.length()/len;++i)
//		{
//			g.drawText(s, i*len, (i+1)*len, l, h, pen);
//			h+=15*k;
//		}
//		g.drawText(s, i*len, s.length(), l,h,pen);
		int t;
		while (true)
		{
			t=s.indexOf("\r\n");
			if (t==-1) 
			{
				g.drawText(s, 0, s.length(), l,h,pen);
				break;
			}
			g.drawText(s, 0, t, l,h,pen);
			h+=15*k;
			s=s.substring(t+2);
		}
	}
	
}
