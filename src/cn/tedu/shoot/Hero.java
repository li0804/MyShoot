package cn.tedu.shoot;
import java.awt.image.BufferedImage;
public class Hero extends FlyingObject{
	private static BufferedImage[] images;
	static {
		images = new BufferedImage[2];
		images[0] = readImage("hero0.png");
		images[1] = readImage("hero1.png");
	}

	private int life;
	private int doubleFire;//火力值
	
	public Hero(){
		super(97,124,140,400);
		life = 3;
		doubleFire = 0;
	}
	
	public void moveTo(int x,int y) {
		this.x = x-this.width/2;
		this.y = y-this.height/2;
	}
	//重写step()
	public void step() {
		
	}
	//重写获取图片
	int index = 0;//下标
	public BufferedImage getImage() {                    //  index=0   images[0]   index=1
		if(isLife()) {                                    //  index=1   images[1]   index=2
			return images[index++%images.length];          //  index=2   images[0]   index=3
		}                                                 //  index=3   images[1]   index=4
		return null;
	}
	//英雄机发射子弹
	public Bullet[] shoot() {
		int xStep = this.width/4;//1/4英雄机的宽
		int yStep = 20;
		if(doubleFire>0) {//双倍
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x+1*xStep,this.y-yStep);
			bs[1] = new Bullet(this.x+3*xStep,this.y-yStep);
			doubleFire-=2;//发射一次双倍火力，火力值减2
			return bs;
		}else {//单倍
			Bullet[] bs = new Bullet[1];
			bs[0] = new Bullet(this.x+2*xStep,this.y-yStep);
			return bs;
		}
	}
	//英雄机增命
	public void addLive() {
		life++;
	}
	//获取英雄机的命
	public int getLife() {
		return life;//返回命数
	}
	//英雄机减命
	public void subtractLife() {
		life--;
	}
	//英雄机增火力值
	public void addDoublie() {
		doubleFire+=40;
	}
	//清空英雄机火力值
	public void clearDoublieFire() {
		doubleFire=0;
	}
}
