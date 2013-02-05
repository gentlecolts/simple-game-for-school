import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;


public class MainClass {

	public static double scale, startScale;
	public static ArrayList<Ball> balls;
	public static DodgeWindow[][] windows;
	static Thread mainThread;
	public static PlayerKeys keys;
	static long startTime;
	
	final static int resolution=1000;
	final static int xWindows=5,yWindows=3;
	final static int windowPaddingX=10,windowPaddingY=10;
	final static int edgePaddingX=100,edgePaddingY=100;
	final static int wallThickness=30;
	static int screenWidth,screenHeight;
	
	final static int startEnemies=4;
	static int level=0;
	
	/**
	 * Start everything, create the menu
	 * @param args
	 */
	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		screenWidth=(int)Math.min(screenSize.getWidth()-edgePaddingX*2, (screenSize.getHeight()-edgePaddingY*2)/yWindows*xWindows);
		screenHeight=(int)Math.min(screenSize.getHeight()-edgePaddingY*2, (screenSize.getWidth()-edgePaddingX*2)/xWindows*yWindows);
		
		scale=Math.min(1.0*screenWidth/resolution, 1.0*screenHeight/resolution);
		startScale=scale;
		System.out.println("scale: "+scale);
		startGame();	//ADD MENUS
	}
	
	/**
	 * Start the actual game, create the game windows
	 */
	public static void startGame() {
		keys=new PlayerKeys();
		
		balls=new ArrayList<Ball>(startEnemies+1);
		
		balls.add(PlayerBall.player);
		
		for(int i=0;i<startEnemies;i++) {
			balls.add(new EnemyBall(Math.random()*resolution,Math.random()*resolution));
		}
		
		windows=new DodgeWindow[xWindows][yWindows];
		
		for(int i=0;i<xWindows;i++) {
			for(int j=0;j<yWindows;j++) {
				windows[i][j]=new DodgeWindow(i,j);
				windows[i][j].addKeyListener(keys);
			}
		}
		
		mainThread=new Thread(new Runnable(){
			public void run() {
				while(true) {
					try {
						Thread.sleep(1000/30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for(int i=0;i<xWindows;i++) 
						for(int j=0;j<yWindows;j++)
							windows[i][j].repaint();
					
					for(int i=0;i<balls.size();i++) {
						balls.get(i).update(1/30.0);
					}
					
					if(System.currentTimeMillis()>startTime+30000) {
						levelUp();
						System.out.println("LEVEL UP!");
						startTime=System.currentTimeMillis();
					}
				}
			}
		});
		
		startTime=System.currentTimeMillis();
		mainThread.start();
	}
	
	public static void levelUp() {
		level++;
		scale*=1.1;
		
		windows[(int) (Math.random()*windows.length)][(int) (Math.random()*windows[0].length)].close();
	}

	/**
	 * Recreate the menu, close extra windows
	 */
	public static void gameOver() {
		
	}
	
}
