import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

public class MainClass {

	public static double scale, startScale;
	public static ArrayList<Ball> balls;
	public static DodgeWindow[][] windows;
	static Thread mainThread;
	public static PlayerKeys keys;
	static long lastLevelTime, startTime;
	
	final static int resolution=1000;
	final static int xWindows=5,yWindows=3;
	final static int windowPaddingX=10,windowPaddingY=10;
	final static int edgePaddingX=100,edgePaddingY=100;
	final static int wallThickness=30;
	static int screenWidth,screenHeight;
	
	final static int startEnemies=4;
	static int level=0, score=0;
	
	static JFrame topBarWindow, menu;
	static boolean gameRunning=false;
	
	/**
	 * Start everything, create the topBarWindow
	 * @param args
	 */
	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		screenWidth=(int)Math.min(screenSize.getWidth()-edgePaddingX*2, (screenSize.getHeight()-edgePaddingY*2)/yWindows*xWindows);
		screenHeight=(int)Math.min(screenSize.getHeight()-edgePaddingY*2, (screenSize.getWidth()-edgePaddingX*2)/xWindows*yWindows);
		
		scale=Math.min(1.0*screenWidth/resolution, 1.0*screenHeight/resolution);
		startScale=scale;
		
		menu=new JFrame();
		
		JButton button=new JButton("Start!");
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!gameRunning)
					startGame();
			}
		});
		menu.add(button);
		
		menu.pack();
		menu.setVisible(true);
		
		
		//startGame();	//ADD MENUS
	}
	
	public static double xResolution() {
		return resolution*xWindows/yWindows;
	}
	
	/**
	 * Start the actual game, create the game windows
	 */
	public static void startGame() {
		gameRunning=true;
		menu.setVisible(false);
		
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
				//windows[i][j].addKeyListener(keys);
			}
		}
		
		topBarWindow=new JFrame();
		topBarWindow.setFocusable(true);
		topBarWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		topBarWindow.setSize((windows[0][0].getWidth()+windowPaddingX/2+2)*windows.length,0);
		Point tmp=windows[0][0].getLocationOnScreen();
		tmp.y-=44;
		tmp.x-=5;
		topBarWindow.setLocation(tmp);
		//topBarWindow.addKeyListener(keys);
		topBarWindow.requestFocus();
		topBarWindow.setVisible(true);
		
		mainThread=new Thread(new Runnable(){
			public void run() {
				lastLevelTime=System.currentTimeMillis();
				startTime=lastLevelTime;
				
				while(true) {
					try {
						Thread.sleep(1000/30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for(int i=0;i<xWindows;i++) {
						for(int j=0;j<yWindows;j++){
							windows[i][j].repaint();
						}
					}
					
					for(int i=0;i<balls.size();i++) {
						balls.get(i).update(1/30.0);
					}
					
					if(System.currentTimeMillis()>lastLevelTime+30000) {
						levelUp();
					} else if(System.currentTimeMillis()<lastLevelTime+300 && level!=0) {
						scale*=1+(.1/(1000/30));
					}
				}
			}
		});
		
		mainThread.start();
	}
	
	public static void levelUp() {
		level++;
		lastLevelTime=System.currentTimeMillis();
		
		windows[(int) (Math.random()*windows.length)][(int) (Math.random()*windows[0].length)].close();
	}

	/**
	 * Recreate the topBarWindow, close extra windows
	 */
	public static void gameOver() {
		gameRunning=false;
		for(int i=0;i<xWindows;i++) {
			for(int j=0;j<yWindows;j++) {
				windows[i][j].close();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		menu=new JFrame();
		menu.add(new JLabel("Score: "+score),BorderLayout.NORTH);
		JButton button=new JButton("Play again!");
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!gameRunning)
					startGame();
			}
		});
		menu.add(button);
		
		menu.pack();
		menu.setVisible(true);
	}
	
}
