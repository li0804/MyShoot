package cn.tedu.shoot;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Bee extends FlyingObject implements Award{
	private static BufferedImage[] images;
	static {
		images = new BufferedImage[5];
		for(int i=0;i<images.length;i++) {
			images[i] = readImage("bee"+i+".png");
		}
	}

	private int xSpeed;//移动速度
	private int ySpeed;
	private int awardType;//奖励类型
	
	public Bee(){
		super(60,50);
		xSpeed = 1;
		ySpeed = 2;
		Random rand = new Random();
		awardType = rand.nextInt(2);
	}
	//重写step()
	public void step() {
		x+=xSpeed;//x+向左或向右
		y+=ySpeed;//y+向下
		if(x<=0||x>=World.WIDTH-this.width) {//若到两边了
			xSpeed*=-1;//切换方向
		}
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
	//重写getAwardType()获取奖励
	public int getAwardType() {
		return awardType;
	}

}
