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
		st[0][0]="~~~~~~~���ֵ�����~~~~~~~";
		st[0][1]="���ã����ǳ�����~~  \r\n��ӭҳ�棬��������λ�� ������Ϸ��";		
		st[0][2]="����תȦ�ļ�ͷ��������Ϸ������С��~~ \r\n��������λ�� ������С�ߵ�һ�γ�̡�";	
		st[0][3]="��̹���������·����Բ��\r\nС�߾ͻ�������ת���µ�Բ�ϡ�  \r\n���Կ���Բ���þͻ�Χ�����ġ�";	
		st[0][4]="����Ծ��һ˲�䣬С�ߵ������䳤��\r\n��Ϊһ��̰����..������ײ���Լ���\r\n(��ͷ����ͬһ��Բ�ϵ�����)\r\n����С�߲�ײ���Լ����͸Ͻ�ˢ���ɣ�";	
		st[0][5]="ÿ��Dash��̺�CircleԲ֮�����Ծ������Ϊ�����������\r\n���½ǻ���ʱ��ô?���ǲ��ǽ���̫����...==";	
		st[0][6]="����Ŷ~~";
		
		cntSentences[0]=0;
		
		for (int i=0;i<6;++i) cnt[0][i]=1;
		cnt[0][6]=0;
		
	//--------------------------		
		st[1][0]="";
		st[1][1]="��Ϸ����ҳ�棬 ����10��  �� ������Ļ ���¿�ʼ��Ϸ��\r\n�ոյĻ�̫���ˣ�Ϊ�������Ǹ....";
		st[1][2]="��Ϸ����ҳ�棬 ����10��  �� ������Ļ ���¿�ʼ��Ϸ��\r\n�ոյĻ�̫���ˣ�Ϊ�������Ǹ.... \r\n���ϸ�����߷��ؾ�Ŷ~������";
		st[1][3]="��Ȼ�ǣ���ߣ��Ļ��~";
		st[1][4]="��Ȼ�ǣ���ߣ��Ļ��~��";
		st[1][5]="��Ȼ�ǣ���ߣ��Ļ��~����Ҳ�������Գ�����Ļ ";
		
		st[1][6]="��Ȼ�ǣ���ߣ��Ļ��~����Ҳ�������Գ�����Ļ";
		st[1][7]="��Ȼ�ǣ���ߣ��Ļ��~����Ҳ�������Գ�����Ļ\r\nԲ��Χ�����ÿ��ģ�";
		st[1][8]="��Ȼ�ǣ���ߣ��Ļ��~����Ҳ�������Գ�����Ļ\r\nԲ��Χ�����ÿ��ģ�Բ�ٵ�ʱ��Ҳ����";
		st[1][9]="��Ȼ�ǣ���ߣ��Ļ��~����Ҳ�������Գ�����Ļ\r\nԲ��Χ�����ÿ��ģ�Բ�ٵ�ʱ��Ҳ����\r\n���������¼����������ڴ�����¿�ʼ";
		st[1][10]="��Ȼ�ǣ���ߣ��Ļ��~����Ҳ�������Գ�����Ļ\r\nԲ��Χ�����ÿ��ģ�Բ�ٵ�ʱ��Ҳ����\r\n���������¼����������ڴ�����¿�ʼ\r\n�뱣�������Ļ~~~�Լ������ָ~~~";
		st[1][11]="��Ȼ�ǣ���ߣ��Ļ��~����Ҳ�������Գ�����Ļ\r\nԲ��Χ�����ÿ��ģ�Բ�ٵ�ʱ��Ҳ����\r\n���������¼����������ڴ�����¿�ʼ\r\n�뱣�������Ļ~~~�Լ������ָ~~~";
		
		cntSentences[1]=12;
		for (int i=0;i<=2;++i) cnt[1][i]=1;
		for (int i=3;i<=11;++i) cnt[1][i]=2;
		cnt[1][2]=10-2;
		
	//--------------------------	
		st[2][0]="..";
		st[2][1]="...";
		st[2][2]="����";
		st[2][3]="���� ����";
		st[2][4]="���� ���� ����";
		
		st[2][5]="��Ϸ����ҳ�棬����10��  �� ������Ļ ���¿�ʼ��Ϸ��";

		st[2][6]="��Ϸ����ҳ�棬 ����10��  �� ������Ļ ���¿�ʼ��Ϸ��\r\n ��Ҫ���ǽ���Ŷ~";
		st[2][7]="��Ϸ����ҳ�棬 ����10��  �� ������Ļ ���¿�ʼ��Ϸ��\r\n ��Ҫ���ǽ���Ŷ~~";
		st[2][8]="��Ϸ����ҳ�棬 ����10��  �� ������Ļ ���¿�ʼ��Ϸ��\r\n ��Ҫ���ǽ���Ŷ~~~";
		st[2][9]="���ľͺ�~~~ ";
		
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
			st[1][0]="Ŷ�� ���Ѿ��������ĵ�һ����Ϸ~~ "
					+"\r\nCircle��5�֣�Dash��1��"
					+"\r\n����С��Խ��Ծ���÷�Խ�ߣ�";
		}
		else
		{
			if (nowPhrases==2 && nowSentences==10 ) return;
			st[nowPhrases][nowSentences]+="\r\n������һ�û�����ء����� �ȼ�������ȥ�ɡ�����";
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
				st[nowPhrases][nowSentences]="Hello�������Ļ�ϲ࣬��ʱ�����Ը�~";				
				break;							
			case 1:
				st[nowPhrases][nowSentences]="���½ǵ�ɫ�������ռ�����������Բ������ɫ";
				break;
			case 2:
				st[nowPhrases][nowSentences]="����״̬�¹���ȥ�Ŀ�����ʱ������ǰ�������³��ȥ��";
				break;	
			case 3:
				st[nowPhrases][nowSentences]="��������е㿨�����Դ�����Ļ�������½ǹرձ�����";
				break;	
			case 4:
				st[nowPhrases][nowSentences]="���С�ߺ�Ȼ����Ļ����ʧ...�Ǿ��Ҳ�����...ˢˢDash��...";
				break;	
			case 5:
				st[nowPhrases][nowSentences]="�㵱ǰ����߷�������:\r\n"+max+",Circle="+maxc+",Dash="+maxd;
				break;	
			case 6:
				st[nowPhrases][nowSentences]="��Ŷ�����Ѿ������"+ColorCircle.cnt+"��ɫ���ء�";
				break;
			case 7:
				st[nowPhrases][nowSentences]="���ܿ���ɫ����ȱʲô��ɫô��";				
				break;
			case 8:
				st[nowPhrases][nowSentences]="��Ӵ�����Ѿ�����"+Gcnt+"���ˣ�ע������Ŷ";
				break;		
			case 9:
				st[nowPhrases][nowSentences]="��֪��ɫ���ȼ���ʲô��ô��\r\nDuang~Duang~Duang~~~";
				break;
			case 10:
				st[nowPhrases][nowSentences]="С�߻���������Բ�����˶�Ŷ��������Լ��۲�ɡ�";
				break;	
			case 11:
				st[nowPhrases][nowSentences]="лл��һֱ�㵽����...";
				break;				
			case 12:
				st[nowPhrases][nowSentences]="��Ҳ��֪����˵ʲô����~";				
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
