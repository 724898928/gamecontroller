package com.rp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Rocker { 
		double degreesByNormalSystem = Double.NaN;//һ������ϵ�µĽǶ�
 		double rad = Double.NaN;//��ǰң�еĻ���
		int rockerColor = Color.GREEN;
		//��������Բ�ε����ĵ�������뾶 
		private static final float rockerCenterXMarginRight4ScreenWidthPercent = 0.05f;//ҡ���ұ߽����������Ļ��Ȱٷֱ�
		private static final float rockerCenterYMarginBottom4ScreenHeightPercent = 0.05f;//ҡ���ұ߽�߶��������Ļ�ٷֱ�
		private static final float rockerR4ScreenWidthPercent = 0.1f;  
		private float smallCenterX = 120, smallCenterY = 120, smallCenterR = 20;
		private float BigCenterX = 120, BigCenterY = 120, BigCenterR = 40; 
		Paint paint;  
		/*
		 * ��ΪҪ������Ӧ��Ļ��������Ҫ����Ļ��߷Ž���
		 * */
		
		public Rocker(int screenWidth,int screenHeight) {
			super();
			paint = new Paint();
			paint.setAntiAlias(true);  
			BigCenterR = screenWidth*rockerR4ScreenWidthPercent; 
			BigCenterX = smallCenterX = screenWidth - BigCenterR*1.5f - rockerCenterXMarginRight4ScreenWidthPercent*screenWidth;
			BigCenterY = smallCenterY = screenHeight - BigCenterR*1.5f - rockerCenterYMarginBottom4ScreenHeightPercent*screenHeight; 
			smallCenterR = BigCenterR/2;
		}

		void draw(Canvas canvas){ 
			paint.setColor(rockerColor);
			paint.setAlpha(0x77);
			canvas.drawCircle(BigCenterX, BigCenterY, BigCenterR, paint); 
			canvas.drawCircle(smallCenterX, smallCenterY, smallCenterR, paint);  
			paint.setColor(Color.BLACK);
			canvas.drawText("ԭ������������ϵ�µĻ���:"+rad, 20, 20, paint);
			canvas.drawText("�ɸû��ȼ���ó��Ƕ�:"+(rad*180/Math.PI), 20, 40, paint);
			canvas.drawText("ԭ������������ϵ�Ƕ�:"+degreesByNormalSystem, 20, 60, paint); 
		}
		
		
		float getDegrees(float firstX,float firstY,float secondX,float secondY ){ 
			float ret = (float)Math.atan((firstY-secondY)/(firstX-secondX))*180/(float)Math.PI; 
			if(firstX<secondX){
				ret += 180;
			}else {
				ret += 360;
			}
			ret = ret >= 360 ? ret - 360: ret;  
			return ret;
		} 
		 
		/** 
		 * СԲ����ڴ�Բ��Բ���˶�ʱ������СԲ���ĵ������λ��
		 * @param centerX 
		 *            Χ�Ƶ�Բ��(��Բ)���ĵ�X����
		 * @param centerY 
		 *            Χ�Ƶ�Բ��(��Բ)���ĵ�Y����
		 * @param R
		 * 			     Χ�Ƶ�Բ��(��Բ)�뾶
		 * @param rad 
		 *            ��ת�Ļ��� 
		 */
		public void setSmallCircleXY(float centerX, float centerY, float R, double rad) {
			//��ȡԲ���˶���X����   
			smallCenterX = (float) (R * Math.cos(rad)) + centerX;
			//��ȡԲ���˶���Y����  
			smallCenterY = (float) (R * Math.sin(rad)) + centerY;
		}
		
		/**
		 * �õ�����֮��Ļ���
		 * @param px1    ��һ�����X����
		 * @param py1    ��һ�����Y����
		 * @param px2    �ڶ������X����
		 * @param py2    �ڶ������Y����
		 * @return
		 */
		public double getRad(float px1, float py1, float px2, float py2) {
			//�õ�����X�ľ���  
			float dx = px2 - px1;
			//�õ�����Y�ľ���  
			float dy = py1 - py2;
			//���б�߳�  
			float Hypotenuse = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
			//�õ�����Ƕȵ�����ֵ��ͨ�����Ǻ����еĶ��� ���ڱ�/б��=�Ƕ�����ֵ��  
			float cosAngle = dx / Hypotenuse;
			//ͨ�������Ҷ����ȡ����ǶȵĻ���  
			float rad = (float) Math.acos(cosAngle);
			//��������λ��Y����<ҡ�˵�Y��������Ҫȡ��ֵ-0~-180  ��������
			if (py2 < py1) {
				rad = -rad;
			}
			return rad;
		}
		
		boolean WORKING = false;
		
		public void reset() {
			 smallCenterX = BigCenterX;
			 smallCenterY = BigCenterY;
			 WORKING = false;
		}

		public void update(int pointX,int pointY) {				
		    if(WORKING){ 
				//�ж��û������λ���Ƿ��ڴ�Բ��
				if (Math.sqrt(Math.pow((BigCenterX - pointX), 2) + Math.pow((BigCenterY - pointY), 2)) <= (BigCenterR )) {
					//��СԲ�����û�����λ���ƶ� 
					smallCenterX = pointX;
					smallCenterY = pointY; 
					this.rad = getRad(BigCenterX, BigCenterY, pointX, pointY); 
				}  
				else { 
					 this.rad = getRad(BigCenterX, BigCenterY, pointX, pointY); 
					 setSmallCircleXY(BigCenterX, BigCenterY, BigCenterR, rad);
				} 
				degreesByNormalSystem = getDegrees(BigCenterX,BigCenterY,smallCenterX,smallCenterY); 
		    }
		}

		public void begin(int pointX,int pointY) {
			if (Math.sqrt(Math.pow((BigCenterX - pointX), 2) + Math.pow((BigCenterY - pointY), 2)) <= (BigCenterR )) {
				WORKING = true;
				update(pointX,pointY);
			} else{
				WORKING = false;
			}
		} 
}
