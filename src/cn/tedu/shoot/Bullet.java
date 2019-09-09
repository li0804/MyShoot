package cn.tedu.shoot;
import java.awt.image.BufferedImage;
public class Bullet extends FlyingObject{
	private static BufferedImage image;
	static {
		image = readImage("bullet.png");
	}

	private int speed;
	
	public Bullet(int x,int y){
		super(8,14,x,y);
		speed = 3;
	}
	public void step() {
		y-=speed;//y-(向上）
	}
	//重写image获取图片
	public BufferedImage getImage() {
		if(isLife()) {//活着的
			return image;//返回image
		}else if(isDead()) {//死了的
			state = REMOVE;//状态修改为REMOVE
		}
		return null;//Dead与REMOVE时返回null
	}
	//重写outOfBounts()越界检测
	public boolean outOfBounds() {
		return this.y<=-this.height;
	}

}
