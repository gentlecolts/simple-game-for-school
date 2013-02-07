import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;


public class DodgeWindow extends JDialog {
	int xIndex,yIndex;
	int origX,origY;
	boolean closedByGame=false;

	  private int bufferWidth;
	    private int bufferHeight;
	    private Image bufferImage;
	    private Graphics bufferGraphics;

	/**
	 * Creates a window
	 * @param x	the horizontal grid position
	 * @param y	the vertical grid position
	 */
	public DodgeWindow(int x,int y) {
		xIndex=x;
		yIndex=y;
		//setUndecorated(true);
		pack();			//getInsets() only works after you call pack() or show for some reason... Goddammit Java
		origX=(int)(MainClass.scale*MainClass.resolution/MainClass.yWindows*x+MainClass.edgePaddingX+((x!=0) ? MainClass.windowPaddingX/2 : 0));
		origY=(int)((MainClass.scale*MainClass.resolution+getInsets().top)/MainClass.yWindows*y+MainClass.edgePaddingY+((y!=0) ? MainClass.windowPaddingY/2 : 0)-getInsets().top);
		
		setSize((int)(MainClass.scale*MainClass.resolution/MainClass.yWindows-((x!=0 && x!=MainClass.xWindows-1) ? MainClass.windowPaddingX : MainClass.windowPaddingX/2)),
				(int)((MainClass.scale*MainClass.resolution+getInsets().top)/MainClass.yWindows-((y!=0 && y!=MainClass.yWindows-1) ? MainClass.windowPaddingY : MainClass.windowPaddingY/2)));
		setLocation(origX,origY);
		setResizable(false);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				if(!closedByGame)
					System.exit(0);
			}
		});
		setFocusable(false);

		this.addWindowFocusListener(new WindowFocusListener(){
			public void windowGainedFocus(WindowEvent e) {
				if(MainClass.topBarWindow!=null) {
					MainClass.topBarWindow.requestFocus();
				}
			}
			public void windowLostFocus(WindowEvent e) {}
		});
		setVisible(true);
	}
	public void update(Graphics g){
        paint(g);
    }

    public void paint(Graphics g){
        //    checks the buffersize with the current panelsize
        //    or initialises the image with the first paint
        if(bufferWidth!=getSize().width || 
          bufferHeight!=getSize().height || 
          bufferImage==null || bufferGraphics==null)
            resetBuffer();
        
        if(bufferGraphics!=null){
            //this clears the offscreen image, not the onscreen one
            bufferGraphics.clearRect(0,0,bufferWidth,bufferHeight);

            //calls the paintbuffer method with 
            //the offscreen graphics as a param
            paintBuffer(bufferGraphics);

            //we finaly paint the offscreen image onto the onscreen image
            g.drawImage(bufferImage,0,0,this);
        }

    }

    private void resetBuffer(){
        // always keep track of the image size
        bufferWidth=getSize().width;
        bufferHeight=getSize().height;

        //    clean up the previous image
        if(bufferGraphics!=null){
            bufferGraphics.dispose();
            bufferGraphics=null;
        }
        if(bufferImage!=null){
            bufferImage.flush();
            bufferImage=null;
        }
        System.gc();

        //    create the new image with the size of the panel
        bufferImage=createImage(bufferWidth,bufferHeight);
        bufferGraphics=bufferImage.getGraphics();
    }
	/**
	 * Finds all balls located in the window with camera centered on the player and draws them
	 */
	public void paintBuffer(Graphics g) {
		g.setColor(new Color(140,140,140));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Graphics2D g2=(Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
		
		g2.setColor(new Color(30,30,30));
		g2.setStroke(new BasicStroke(3));
		for(int i=-getWidth();i<getWidth()*2;i+=(MainClass.screenWidth-MainClass.wallThickness)/(4*MainClass.xWindows)) {
			double x=i+Ball.cameraOffsetX() % ((MainClass.screenWidth-MainClass.wallThickness)/(4*MainClass.xWindows))
					-(getLocationOnScreen().x-origX) % (getWidth()+MainClass.windowPaddingX/2)-MainClass.windowPaddingX/2*xIndex;
					
			g2.drawLine((int)(x), 0, (int)x, getHeight());
		}
		for(int j=-getHeight();j<getHeight()*2;j+=(MainClass.screenHeight-MainClass.wallThickness)/(4*MainClass.yWindows)) {
			double y=j+Ball.cameraOffsetY() % ((MainClass.screenHeight-MainClass.wallThickness)/(4*MainClass.yWindows))
					-(getLocationOnScreen().y-origY) % (getHeight()+MainClass.windowPaddingY/2)-MainClass.windowPaddingY/2*yIndex;
			
			g2.drawLine(0, (int)(y), getWidth(), (int)(y));
		}
		
		Ball b=null;
		for(int i=0;i<MainClass.balls.size();i++) {
			b=MainClass.balls.get(i);
			if(b!=null){
				b.draw((Graphics2D)g, this);
			}
		}
		
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(MainClass.wallThickness));
		
		double leftX=-getLocationOnScreen().x+MainClass.edgePaddingX+Ball.cameraOffsetX();
		double topY=-getLocationOnScreen().y+MainClass.edgePaddingY+Ball.cameraOffsetY();
		double drawWidth=MainClass.resolution*MainClass.xWindows/MainClass.yWindows*MainClass.scale;
		double drawHeight=MainClass.resolution*MainClass.scale;
				
		g2.draw(new Rectangle2D.Double(leftX, topY, drawWidth, drawHeight));
		
		
		if(getLocationOnScreen().x!=origX || getLocationOnScreen().y!=origY) {
			Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
			
			g2.setColor(Color.white);
			g2.fill(new Rectangle2D.Double(leftX-MainClass.edgePaddingX, topY-MainClass.edgePaddingY,
					MainClass.edgePaddingX, drawHeight+MainClass.edgePaddingY*2));
			g2.fill(new Rectangle2D.Double(leftX, topY-MainClass.edgePaddingY,
					drawWidth, MainClass.edgePaddingY));
			g2.fill(new Rectangle2D.Double(leftX+MainClass.xResolution()*MainClass.scale,
					topY-MainClass.edgePaddingY,
					d.getWidth()-(leftX+MainClass.xResolution()*MainClass.scale), 
					drawHeight+MainClass.edgePaddingY*2));
			g2.fill(new Rectangle2D.Double(leftX, topY+MainClass.resolution*MainClass.scale, drawWidth, d.getHeight()-(topY+MainClass.resolution*MainClass.scale)));
		}
		
		/*
		if(getLocationOnScreen().x+getWidth()-MainClass.edgePaddingX>MainClass.xResolution()*MainClass.scale-100 && getLocationOnScreen().y-MainClass.edgePaddingY<100) {
			g2.setColor(Color.red);
			g2.drawString("Score: "+(int)(System.currentTimeMillis()-MainClass.startTime),(int)(MainClass.xResolution()*MainClass.scale-100-getLocationOnScreen().x+MainClass.edgePaddingX),
					100-getLocationOnScreen().y+MainClass.edgePaddingY);
		}//*/
		
	}
	
	/**
	 * Close the window
	 */
	public void close() {
		closedByGame=true;
        WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
	}
	
}
