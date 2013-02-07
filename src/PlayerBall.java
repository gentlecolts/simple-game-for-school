import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;


public class PlayerBall extends Ball {
	static PlayerBall player=new PlayerBall();		//so that the camera knows what to follow
	
	static final int maxHP=6;
	int hp=maxHP;
	
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
		
		if(yAccel==0) {
			yAccel=-Math.signum(yVel)*50;
		}
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
		g.fill(new Ellipse2D.Double(x, y,diameter,diameter));
		for(int i=0;i<3;i++) {
			g.setColor(i==0 ? Color.RED : i==1 ? Color.BLUE : Color.GREEN);
			if(hp>i) {
				g.fill(new Arc2D.Double(x, y,diameter,diameter,
					120*i,120,hp>i+3 ? Arc2D.PIE : Arc2D.CHORD));
			}
		}
		
		MainClass.score=(int)(System.currentTimeMillis()-MainClass.startTime);
		if(MainClass.topBarWindow!=null){
			MainClass.topBarWindow.setTitle("Score: "+MainClass.score);
		//g2.drawString("Score: "+MainClass.score,(int)(MainClass.xResolution()*MainClass.scale-100-getLocationOnScreen().x+MainClass.edgePaddingX),100-getLocationOnScreen().y+MainClass.edgePaddingY);
		}
	}
	
}
