package cn.tedu.shoot;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
public class World extends JPanel{
	public static final int WIDTH = 400;//窗口宽
	public static final int HEIGHT = 700;//窗口高
	
	public static final int START = 0;//启动状态
	public static final int RUNNING = 1;//运行状态
	public static final int PAUSE = 2;//暂停状态
	public static final int GAME_OVER = 3;//游戏结束状态
	private int state = START;//当前状态（默认状态）
	
	private static BufferedImage start;//启动图
	private static BufferedImage pause;//暂停图
	private static BufferedImage gameover;//游戏结束图
	static {//初始化静态图片
		start = FlyingObject.readImage("start.png");
		pause = FlyingObject.readImage("pause.png");
		gameover = FlyingObject.readImage("gameover.png");
	}
	
	private Sky sky = new Sky();//天空对象
	private Hero hero = new Hero();//英雄机对象
	private FlyingObject[] enemies = {};//敌人数组（小蜜蜂、小敌机、大敌机			
	private Bullet[] bullets = {};//子弹数组	
	
	//生成敌人对象
	public FlyingObject nextOne() {
		Random rand = new Random();//随机数对象
		int type = rand.nextInt(20);
		if(type<5) {
			return new Bee();
		}else if(type<12) {
			return new Airplane();
		}else {
			return new Bigairplane();
		}
	}
	
	//敌人入场
	int enterIndex = 0;//敌人入场计数	
	public void enterAction() {//每10毫秒走一次
		enterIndex++;
		if(enterIndex%40==0) {//每400毫秒走一次
			FlyingObject obj = nextOne();//
			enemies = Arrays.copyOf(enemies,enemies.length+1);//扩容
			enemies[enemies.length-1] = obj;
		}
	}
	
	//子弹入场
	int shootIndex = 0;//射击计数
	public void shootAction() {//每10毫秒走一次
		shootIndex++;
		if(shootIndex%30==0) {//每300毫秒走一次
			Bullet[] bs = hero.shoot();//获取子弹对象
			bullets = Arrays.copyOf(bullets,bullets.length+bs.length);//扩容（bs有几个就加几个）
			System.arraycopy(bs,0,bullets,bullets.length-bs.length,bs.length);//数组的追加
		}
	}
	//飞行物移动
	public void stepAction() {
		sky.step();//天空动
		for(int i=0;i<enemies.length;i++) {
			enemies[i].step();//敌人动
		}
		for(int i=0;i<bullets.length;i++) {
			bullets[i].step();//子弹动
		}
	}
	//删除越界敌人
	public void outOfBoundsAction() {
		int index = 0;//index角色：1.下标 2.不越界敌人个数
		FlyingObject[] enemyLives = new FlyingObject[enemies.length];//不越界敌人数组
		for(int i=0;i<enemies.length;i++) {
			FlyingObject f = enemies[i];//获取每一个敌人
			if(!f.outOfBounds()&&!f.isRemove()) {//不越界并且非删除
				enemyLives[index] = f;
				index++;
			}
		}
		enemies = Arrays.copyOf(enemyLives,index);
		//删除越界子弹
		index = 0;//1.下标归零2.个数归零
		Bullet[] bulletLives = new Bullet[bullets.length];
		for(int i=0;i<bullets.length;i++) {
			Bullet b = bullets[i];
			if(!b.outOfBounds()&&!b.isRemove()) {//不越界并且非删除
				bulletLives[index] = b;
				index++;
			}
		}
		bullets = Arrays.copyOf(bulletLives,index);
	}
	//子弹与敌人（小敌机、大敌机、小蜜蜂）碰撞
	int score = 0;//玩家的得分
	public void bulletBangAction() {//每10毫秒走一次
		for(int i=0;i<bullets.length;i++) {
			Bullet b = bullets[i];//获取每个子弹
			for(int j=0;j<enemies.length;j++) {
				FlyingObject f = enemies[j];//获取每个敌人
				if(b.isLife()&&f.isLife()&&f.hit(b)) {//子弹活着并且敌人活着并且敌人撞上子弹
					f.goDead();//敌人死
					b.goDead();//子弹死
					
					if(f instanceof Enemy) {//若被撞对象为小敌机、大敌机
						Enemy e = (Enemy)f;//将被撞对象强转为得分
						score +=e.getScore();//玩家得分
					}
					if(f instanceof Award) {//若被撞对象为小蜜蜂
						Award a = (Award)f;//将被撞对象强转为奖励
						int type =a.getAwardType();//获取奖励类型
						switch(type) {//根据不同奖励类型来获取不同的奖励
						case Award.DOUBLE_FIRE://得火力值
							hero.addDoublie();
							break;
						case Award.LIFE://得命
							hero.addLive();
							break;
						}
					}
				}
			}
		}
	}
	//英雄机与敌人碰撞
	public void heroBangAction() {//每10毫秒走一次
		for(int i=0;i<enemies.length;i++) {
			FlyingObject f = enemies[i];//获取每一个敌人
			if(hero.isLife()&&f.isLife()&&f.hit(hero)) {//英雄机活、敌人活、敌人撞上英雄机
				f.goDead();//敌人死
				hero.subtractLife();//英雄机减命
				hero.clearDoublieFire();//英雄机清空火力值
			}
		}
	}
	//检测游戏结束
	public void checkGameOverAction() {//每10毫秒走一次
		if(hero.getLife()<=0) {//游戏结束
			state = GAME_OVER;//将当前状态改为游戏结束			
		}
	}
	
	//启动程序的执行
	public void action() {
		//创建侦听器
		MouseAdapter l = new MouseAdapter() {
			//重写鼠标移动事件
			public void mouseMoved(MouseEvent e) {
				if(state==RUNNING) {//仅在运行状态下执行
					int x = e.getX();//获取鼠标x坐标
					int y = e.getY();
					hero.moveTo(x, y);//英雄机随鼠标移动
				}
			}
			//重写鼠标点击事件
			public void mouseClicked(MouseEvent e) {
				switch(state) {
				case START:
					state=RUNNING;
					break;
				case GAME_OVER:
					score = 0;//清理现场
					sky = new Sky();
					hero = new Hero();
					enemies = new FlyingObject[0];
					bullets = new Bullet[0];
					state=START;
					break;
				}
			}
			//重写鼠标移出
			public void mouseExited(MouseEvent e) {
				if(state==RUNNING) {//运行状态时
					state=PAUSE;//修改为暂停状态
				}
			}
			//重写鼠标移入
			public void mouseEntered(MouseEvent e) {
				if(state==PAUSE) {//暂停状态时
					state=RUNNING;//修改为运行状态
				}
			}
		};
		this.addMouseListener(l);//处理鼠标操作
		this.addMouseMotionListener(l);//鼠标滑动
		
		Timer timer = new Timer();//定时器对象
		int interval = 10;//定时间隔(10毫秒）
		timer.schedule(new TimerTask() {
			public void run() {//定时干的事
				if(state==RUNNING) {//仅在运行状态下执行
					enterAction();//敌人入场
					shootAction();//子弹入场
					stepAction();//飞行物移动
					outOfBoundsAction();//删除越界敌人与子弹
					bulletBangAction();//子弹与敌人的碰撞
					heroBangAction();//英雄机与敌人碰撞
					checkGameOverAction();//检测游戏结束					
				}
				repaint();//重新去调用paint（）方法
			}
		},interval,interval);//定时计划表		
	}
	//重写paint()画对象  g：画笔
	public void paint(Graphics g) {
		sky.paintObject(g);//画天空
		hero.paintObject(g);//画英雄机
		for(int i=0;i<enemies.length;i++) {//遍历所有敌人
			enemies[i].paintObject(g);
		}
		for(int i=0;i<bullets.length;i++) {//遍历所有子弹
			bullets[i].paintObject(g);
		}
		//左上角的命和分数
		g.drawString("SCORE(总分):"+score,10,25);
		g.drawString("LIFE（生命）:"+hero.getLife(),10,45);
		
		switch(state) {
		case START:
			g.drawImage(start,0,0,null);
			break;
		case PAUSE:
			g.drawImage(pause,0,0,null);
			break;
		case GAME_OVER:
			g.drawImage(gameover,0,0,null);
			break;
		}
	}

	public static void main(String[] args) {		
		JFrame frame = new JFrame();
		World world = new World();
		frame.add(world);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH,HEIGHT);
		frame.setLocationRelativeTo(null); 
		frame.setVisible(true); 
		
		world.action();
	

	}

}
