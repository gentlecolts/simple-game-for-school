import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;


public class MainClass {

	public static double scale;
	public static ArrayList<Ball> enemies;
	public static DodgeWindow[][] windows;
	static Thread mainThread;
	
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
		enemies=new ArrayList<Ball>(startEnemies);
		
		for(int i=0;i<startEnemies;i++) {
			enemies.add(new EnemyBall(Math.random()*resolution,Math.random()*resolution));
		}
		
		windows=new DodgeWindow[xWindows][yWindows];
		
		for(int i=0;i<xWindows;i++) {
			for(int j=0;j<yWindows;j++) {
				windows[i][j]=new DodgeWindow(i,j);
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
					
					for(int i=0;i<enemies.size();i++) {
						enemies.get(i).update(1000/30);
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
