import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JFrame;


public class DodgeWindow extends JFrame {
	int xIndex,yIndex;
	
	/**
	 * Creates a window
	 * @param x	the horizontal grid position
	 * @param y	the vertical grid position
	 */
	public DodgeWindow(int x,int y) {
		xIndex=x;
		yIndex=y;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.setSize(screenSize.getWidth()-MainClass.edgePaddingX*2, screenSize.getHeight()-MainClass.edgePaddingY*2);
		setSize((int)screenSize.getWidth()/MainClass.xWindows-MainClass.windowPaddingX,(int)screenSize.getHeight()/MainClass.yWindows-MainClass.windowPaddingY);
		setLocation((int)screenSize.getWidth()*x/MainClass.xWindows+MainClass.edgePaddingX,(int)screenSize.getHeight()*y/MainClass.yWindows+MainClass.edgePaddingY);
		setResizable(false);
		
		setVisible(true);
	}
	
	/**
	 * Finds all balls located in the window with camera centered on the player and draws them
	 */
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Graphics2D g2=(Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		for(int i=0;i<MainClass.balls.size();i++) {
			Ball b=MainClass.balls.get(i);
//			System.out.println("ball location: "+(int)(b.xPos*MainClass.scale)+","+(int)(b.yPos*MainClass.scale));
//			System.out.println("position: "+getLocationOnScreen().x+","+getLocationOnScreen().y);
//			System.out.println(""+(b.xPos*MainClass.scale+b.radius*MainClass.scale>getLocationOnScreen().x)+","+
//					(b.xPos*MainClass.scale-b.radius*MainClass.scale<getLocationOnScreen().x+getWidth())+","+
//					(b.yPos*MainClass.scale+b.radius*MainClass.scale>getLocationOnScreen().y)+","+
//					(b.yPos*MainClass.scale+b.radius*MainClass.scale<getLocationOnScreen().y+getHeight()));
//			if(b.xPos*MainClass.scale+b.radius*MainClass.scale>getLocationOnScreen().x
//					&& b.xPos*MainClass.scale-b.radius*MainClass.scale<getLocationOnScreen().x+getWidth()
//					&& b.yPos*MainClass.scale+b.radius*MainClass.scale>getLocationOnScreen().y
//					&& b.yPos*MainClass.scale+b.radius*MainClass.scale<getLocationOnScreen().y+getHeight()) {
				b.draw((Graphics2D)g, this);
//			}
		}
	}
	
	/**
	 * Close the window
	 */
	public void close() {
		
	}
	
}
