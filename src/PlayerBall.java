import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;


public class PlayerBall extends Ball {
	static PlayerBall player=new PlayerBall();		//so that the camera knows what to follow
	
	int hp=3;
	
	final double velCap=100;
	
	/**
	 * <s>There can only be one player.</s>
	 * the playerball needs to be recreated each game
	 */
	public PlayerBall() {
		super();
		xPos=MainClass.resolution/2*MainClass.xWindows/MainClass.yWindows;
		yPos=MainClass.resolution/2;
		
		density=100;
		radius=30;
	}

	@Override
	public void setAccel(double dt) {
		xAccel=MainClass.keys.getXAccel();
		yAccel=MainClass.keys.getYAccel();
		
		if(xAccel==0) {
			xAccel=-Math.signum(xVel)*50;
		}
		
		/*if(Math.abs(xVel)>=velCap) {
			xAccel=velCap-xVel;
		}*/
		
		if(yAccel==0) {
			yAccel=-Math.signum(yVel)*50;
		}
		
		/*if(Math.abs(yVel)>=velCap) {
			yAccel=velCap-yVel;
		}*/
	}
	
	@Override
	public void bounce(Ball b, double nXVel, double nYVel) {
		super.bounce(b, nXVel, nYVel);
		
		if(((EnemyBall)b).destroyCounter==0) {
			hp--;
			if(hp<0) {
				MainClass.gameOver();
			}
		}
	
	}
	
	@Override
	public void draw(Graphics2D g, double posInWindowX, double posInWindowY) {
		double
			x=posInWindowX-radius*MainClass.scale,
			y=posInWindowY-radius*MainClass.scale,
			diameter=radius*2*MainClass.scale;

		g.setColor(Color.BLACK);
		g.fill(new Ellipse2D.Double(x+2,y+2,
				diameter-4,diameter-4));
		
		g.setColor(new Color(255,0,0));
		g.fill(new Arc2D.Double(x, y,diameter,diameter,
				0,120,hp>2 ? Arc2D.PIE : Arc2D.CHORD));
		
		g.setColor(new Color(0,255,0));
		g.fill(new Arc2D.Double(x, y,diameter,diameter,
				120,120,hp>1 ? Arc2D.PIE : Arc2D.CHORD));
		
		g.setColor(new Color(0,0,255));
		g.fill(new Arc2D.Double(x, y,diameter,diameter,
				240,120,hp>0 ? Arc2D.PIE : Arc2D.CHORD));
		
		MainClass.score=(int)(System.currentTimeMillis()-MainClass.startTime);
		MainClass.topBarWindow.setTitle("Score: "+MainClass.score);
		//g2.drawString("Score: "+MainClass.score,(int)(MainClass.xResolution()*MainClass.scale-100-getLocationOnScreen().x+MainClass.edgePaddingX),100-getLocationOnScreen().y+MainClass.edgePaddingY);
	}
	
}
