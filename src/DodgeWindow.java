import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;


public class DodgeWindow extends JFrame {
	int xIndex,yIndex;
	int origX,origY;
	
	/**
	 * Creates a window
	 * @param x	the horizontal grid position
	 * @param y	the vertical grid position
	 */
	public DodgeWindow(int x,int y) {
		xIndex=x;
		yIndex=y;
		
		pack();			//getInsets() only works after you call pack() or show for some reason... Goddammit Java
		origX=(int)(MainClass.scale*MainClass.resolution/MainClass.yWindows*x+MainClass.edgePaddingX+((x!=0) ? MainClass.windowPaddingX/2 : 0));
		origY=(int)((MainClass.scale*MainClass.resolution+getInsets().top)/MainClass.yWindows*y+MainClass.edgePaddingY+((y!=0) ? MainClass.windowPaddingY/2 : 0)-getInsets().top);
		
		setSize((int)(MainClass.scale*MainClass.resolution/MainClass.yWindows-((x!=0 && x!=MainClass.xWindows-1) ? MainClass.windowPaddingX : MainClass.windowPaddingX/2)),
				(int)((MainClass.scale*MainClass.resolution+getInsets().top)/MainClass.yWindows-((y!=0 && y!=MainClass.yWindows-1) ? MainClass.windowPaddingY : MainClass.windowPaddingY/2)));
		setLocation(origX,origY);
		setResizable(false);
		
		
		setVisible(true);
	}
	
	/**
	 * Finds all balls located in the window with camera centered on the player and draws them
	 */
	@Override
	public void paint(Graphics g) {
		g.setColor(new Color(140,140,140));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Graphics2D g2=(Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		
		g2.setColor(new Color(30,30,30));
		g2.setStroke(new BasicStroke(3));
		for(int i=-getWidth();i<getWidth()*2;i+=(MainClass.screenWidth-MainClass.wallThickness)/(4*MainClass.xWindows)) {
			double x=i+Ball.cameraOffsetX() % ((MainClass.screenWidth-MainClass.wallThickness)/(4*MainClass.xWindows))
					-(getLocationOnScreen().x-origX) % getWidth();
					
			g2.drawLine((int)(x), 0, (int)x, getHeight());
		}
		for(int j=-getHeight();j<getHeight()*2;j+=(MainClass.screenHeight-MainClass.wallThickness)/(4*MainClass.yWindows)) {
			double y=j+Ball.cameraOffsetY() % ((MainClass.screenHeight-MainClass.wallThickness)/(4*MainClass.yWindows))
					-(getLocationOnScreen().y-origY) % getHeight();
			
			g2.drawLine(0, (int)(y), getWidth(), (int)(y));
		}
		
		for(int i=0;i<MainClass.balls.size();i++) {
			Ball b=MainClass.balls.get(i);
			b.draw((Graphics2D)g, this);
		}
		
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(MainClass.wallThickness));
		g2.draw(new Rectangle2D.Double(0-getLocationOnScreen().x+MainClass.edgePaddingX+Ball.cameraOffsetX(), 0-getLocationOnScreen().y+MainClass.edgePaddingY+Ball.cameraOffsetY(), 
				MainClass.resolution*MainClass.xWindows/MainClass.yWindows*MainClass.scale,
				MainClass.resolution*MainClass.scale));
		
		
		if(getLocationOnScreen().x!=origX || getLocationOnScreen().y!=origY) {
			g2.setColor(Color.white);
			g2.fill(new Rectangle2D.Double(0-getLocationOnScreen().x+Ball.cameraOffsetX(), 0-getLocationOnScreen().y+Ball.cameraOffsetY(),
					MainClass.edgePaddingX, MainClass.resolution*MainClass.scale+MainClass.edgePaddingY*2));
			g2.fill(new Rectangle2D.Double(0-getLocationOnScreen().x+MainClass.edgePaddingX+Ball.cameraOffsetX(), 0-getLocationOnScreen().y+Ball.cameraOffsetY(),
					MainClass.resolution*MainClass.xWindows/MainClass.yWindows*MainClass.scale, MainClass.edgePaddingY));
			g2.fill(new Rectangle2D.Double(0-getLocationOnScreen().x+MainClass.edgePaddingX+Ball.cameraOffsetX()+MainClass.resolution*MainClass.xWindows/MainClass.yWindows*MainClass.scale,
					0-getLocationOnScreen().y+Ball.cameraOffsetY(),
					MainClass.edgePaddingX, MainClass.resolution*MainClass.scale+MainClass.edgePaddingY*2));
			g2.fill(new Rectangle2D.Double(0-getLocationOnScreen().x+MainClass.edgePaddingX+Ball.cameraOffsetX(),
					0-getLocationOnScreen().y+Ball.cameraOffsetY()+MainClass.resolution*MainClass.scale+MainClass.edgePaddingY,
					MainClass.resolution*MainClass.xWindows/MainClass.yWindows*MainClass.scale, MainClass.edgePaddingY));
		}
	}
	
	/**
	 * Close the window
	 */
	public void close() {
        WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
	}
	
}
