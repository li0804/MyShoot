package cn.tedu.shoot;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.Graphics;
public abstract class FlyingObject {
	public static final int LIFE = 0;//活着的
	public static final int DEAD = 1;//死了的
	public static final int REMOVE = 2;//删除的
	protected int state = LIFE;//当前状态（默认活着）
	
	protected int width;
	protected int height;
	protected int x;
	protected int y;
	
	//给小敌机、大敌机、小蜜蜂 提供
	public FlyingObject(int width,int height){
		this.width =width;
		this.height = height;
		Random rand = new Random();
		x = rand.nextInt(World.WIDTH-this.width);//生成随机数
		y = -this.height;
	}
	
	//给英雄机、天空、子弹 提供
	FlyingObject(int width,int height,int x,int y){
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}
	
	public abstract void step();//抽象方法
	
	//读取图片
	public static BufferedImage readImage(String fileName) {
		try{
			BufferedImage img = ImageIO.read(FlyingObject.class.getResource(fileName));
			return img;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	//获取图片
	public abstract BufferedImage getImage();
	
	//判断是否是活着
	public boolean isLife() {
		return state==LIFE;
	}
	//判断是否死了
	public boolean isDead() {
		return state==DEAD;
	}
	//判断是否删除
	public boolean isRemove() {
		return state==REMOVE;
	}
	//画图片
	public void paintObject(Graphics g) {
		g.drawImage(this.getImage(),this.x,this.y,null);
	}
	//敌人的越界检测
	public boolean outOfBounds() {
		return this.y>=World.HEIGHT;//敌人的y>=窗口的高
	}
	//碰撞检测   this：敌人  other：子弹、英雄机
	public boolean hit(FlyingObject other) {
		int x1 = this.x-other.width;//x1:敌人的x-子弹的宽
		int x2 = this.x+this.width;//x2：敌人的x+敌人的宽
		int y1 = this.y-other.height;//敌人的y-子弹的高
		int y2 = this.y+this.height;//敌人的y+敌人的高
		int x = other.x;//子弹的x
		int y = other.y;//子弹的y
		return x>=x1&&x<=x2&&y>=y1&&y<=y2;//x在x1与x2之间并且y在y1与y2之间
	}
	//飞行物死亡
	public void goDead() {
		state = DEAD;
	}

}
