import java.awt.Graphics;
import javax.swing.JFrame;


public class DodgeWindow extends JFrame {
	
	/**
	 * Creates a window
	 * @param x	the horizontal grid position
	 * @param y	the vertical grid position
	 */
	public DodgeWindow(int x,int y) {
		
	}
	
	/**
	 * Finds all balls located in the window with camera centered on the player and draws them
	 */
	@Override
	public void paint(Graphics g) {
		for(int i=0;i<MainClass.enemies.size();i++) {
			Ball b=MainClass.enemies.get(i);
			if(b.xPos*MainClass.scale+b.radius*MainClass.scale>getLocationOnScreen().x
					&& b.xPos*MainClass.scale-b.radius*MainClass.scale<getLocationOnScreen().x+getWidth()
					&& b.yPos*MainClass.scale+b.radius*MainClass.scale>getLocationOnScreen().y
					&& b.yPos*MainClass.scale+b.radius*MainClass.scale<getLocationOnScreen().y+getHeight()) {
				
			}
		}
	}
	
	/**
	 * Close the window
	 */
	public void close() {
		
	}
	
}
