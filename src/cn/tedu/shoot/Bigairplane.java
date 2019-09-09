package cn.tedu.shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Bigairplane extends FlyingObject implements Enemy{
	private static BufferedImage[] images;
	static {
		images = new BufferedImage[5];
		for(int i=0;i<images.length;i++) {
			images[i] = readImage("bigplane"+i+".png");
		}
	}

	private int speed;
	
	public Bigairplane(){
		super(69,99);
		speed = 2;
	}
	//重写step()
	public void step() {
		y+=speed;//y=(向下）
	}
	//重写获取图片
	int index = 1;//下标	
	public BufferedImage getImage() {
		if(isLife()) {
			return images[0];
		}else if(isDead()) {
			BufferedImage img = images[index++];
			if(index==images.length) {
				state = REMOVE;
			}
			return img;
		}
		return null;//REMOVE状态时返回null
	}
	//重写getScore()得分
	public int getScore() {
		return 3;//打大敌机得3分
	}
}
