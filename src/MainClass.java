import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;


public class MainClass {

	public static double scale;
	public static ArrayList<Ball> balls;
	public static DodgeWindow[][] windows;
	static Thread mainThread;
	public static PlayerKeys keys;
	
	final static int resolution=1000;
	final static int xWindows=5,yWindows=3;
	final static int windowPaddingX=10,windowPaddingY=10;
	final static int edgePaddingX=100,edgePaddingY=100;
	
	final static int startEnemies=4;
	
	/**
	 * Start everything, create the menu
	 * @param args
	 */
	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		scale=Math.min(1.0*screenSize.getWidth()/resolution, 1.0*screenSize.getHeight()/resolution);
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
				}
			}
		});
		
		mainThread.start();
	}

	/**
	 * Recreate the menu, close extra windows
	 */
	public static void gameOver() {
		
	}
	
}
