package cn.tedu.shoot;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
public class Sky extends FlyingObject{
	private static BufferedImage image;
	static {
		image = readImage("background.png");
	}

	private int speed;//移动速度
	private int y1;//第二张图片的y坐标
	public Sky(){
		super(World.WIDTH,World.HEIGHT,0,0);
		speed = 1;
		y1 = -World.HEIGHT;
	}
	//重写step()
	public void step() {
		y+=speed;//y+(向下）
		y1+=speed;//y1+(向下）
		if(y>=World.HEIGHT) {//若y>=窗口的高，意味着y出去了
			y=-World.HEIGHT;//则修改y为负的窗口高
		}
		if(y1>=World.HEIGHT) {//若y1>=窗口的高，意味着y1出去了
			y1=-World.HEIGHT;//则修改y1为负的窗口高
		}		
	}
	//重写image获取图片
	public BufferedImage getImage() {
		return image;
	}
	//重写paintObject()画图片
	public void paintObject(Graphics g) {
		g.drawImage(getImage(),x,y,null);//天空需要两张图片
		g.drawImage(getImage(),x,y1,null);
	}

}
