import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;


public class EnemyBall extends Ball {

	final static double gravConst=.006;
	int destroyCounter=0;
	
	public EnemyBall(double x,double y) {
		super();
		xPos=x;
		yPos=y;
		density=1;
		
		radius=20;
	}
	
	@Override
	public void setAccel(double dt) {
		PlayerBall player=PlayerBall.player;
		int sign=collides(player) ? -1 : 1;
		if(!collides(player)) {
			double gravityAccel=gravConst*mass()*player.mass();
			double distSq=distSq(player);
			gravityAccel/=distSq;
			
			double dist=Math.sqrt(distSq);
			xAccel=sign*gravityAccel/dist*(player.xPos-xPos);
			yAccel=sign*gravityAccel/dist*(player.yPos-yPos);
		} else {
//			xAccel=-(player.xPos-xPos)/1000000;
//			yAccel=-(player.yPos-yPos)/1000000;
			
			xAccel=0;
			yAccel=0;
		}
	}
	
	@Override
	public void draw(Graphics2D g, double posInWindowX, double posInWindowY) {
		g.setColor(Color.BLACK);
		if(destroyCounter>0) {
			g.setColor(new Color(0,0,0,255-destroyCounter));
			
			destroyCounter++;
			
			if(destroyCounter==255) {
				MainClass.balls.remove(this);
			}
		}
		g.fill(new Ellipse2D.Double(posInWindowX-radius*MainClass.scale,
				posInWindowY-radius*MainClass.scale,
				radius*2*MainClass.scale,radius*2*MainClass.scale));
		
	}
	
	@Override
	public void bounce(Ball b, double nXVel, double nYVel) {
		super.bounce(b, nXVel, nYVel);
		
		if(b instanceof PlayerBall) {
			destroyCounter++;
		}
	}

}
