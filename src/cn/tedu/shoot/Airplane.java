package cn.tedu.shoot;
import java.awt.image.BufferedImage;
import java.util.Random;
public class Airplane extends FlyingObject implements Enemy{
	private static BufferedImage[] images;
	static {
		images = new BufferedImage[5];
		for(int i=0;i<images.length;i++) {
			images[i] = readImage("airplane"+i+".png");
		}
	}

	private int speed;
	
	public Airplane(){
		super(49,36);
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
		}else if(isDead()) {                         //  10m  images[1]   返回images[1]   index=2
			BufferedImage img = images[index++];      //  20m  images[2]   返回images[2]   index=3
			if(index==images.length) {                //  30m  images[3]   返回images[3]   index=4
				state = REMOVE;                        //  40m  images[4]   返回images[4]   index=5  修改为REMOVE
			}                                         //  50m     index=6  flase   return null
			return img;
		}
		return null;//REMOVE状态时返回null
	}
	//重写getScore()得分
	public int getScore() {
		return 1;//打小敌机得1分
	}

}
