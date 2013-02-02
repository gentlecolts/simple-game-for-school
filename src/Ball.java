import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;


public abstract class Ball {
	public double xPos,yPos,xVel,yVel,xAccel,yAccel;
	public double radius;
	public double density;
	public int id;
	static int idCount=0;
	
	public Ball() {
		id=idCount;
		idCount++;
	}
	
	/**
	 * Move the ball smoothly, check collisions
	 * @param dt	the change in time so that movement isn't jerky
	 */
	public void update(double dt) {
		xPos+=xVel*dt;
		yPos+=yVel*dt;
		
		xVel+=xAccel*dt;
		yVel+=yAccel*dt;
		
		setAccel(dt);
		
		for(int i=0;i<MainClass.balls.size();i++) {
			if(id<MainClass.balls.get(i).id && collides(MainClass.balls.get(i))) {
				bounce(MainClass.balls.get(i));
			}
		}
	}
	
	/**
	 * Set the acceleration; varies by enemy or player, so abstract
	 * @param dt	the change in time
	 */
	public abstract void setAccel(double dt);
	
	/**
	 * Bounce it off of another ball by calling the other bounce on each
	 * @param b	the other ball
	 */
	public void bounce(Ball b) {
		//mv=mv
		double dist=Math.sqrt(distSq(b));
		double angle=Math.atan2(yPos-b.yPos, xPos-b.xPos);
		double cosA=Math.cos(angle);
		double sinA=Math.sin(angle);
		
		double rotXVel1=cosA*xVel+sinA*yVel;
		double rotYVel1=cosA*yVel-sinA*xVel;
		double rotXVel2=cosA*b.xVel+sinA*b.yVel;
		double rotYVel2=cosA*b.yVel-sinA*b.xVel;
		
		double momentum=rotXVel1*mass()+rotXVel2*b.mass();
		
		double totVelX=xVel-b.xVel;
		
		rotXVel1=(momentum-b.mass()*totVelX)/(mass()+b.mass());
		rotXVel2=totVelX+rotXVel1;
		
		bounce(b,rotXVel2,rotYVel2,sinA,cosA);
		b.bounce(this,rotXVel1,rotXVel1,sinA,cosA);
		
		double diff=(radius+b.radius-dist)/2;
		
		double cosD=cosA*diff;
		double sinD=cosD*diff;
		
		xPos-=cosD;
		yPos-=sinD;
		b.xPos+=cosD;
		b.yPos+=sinD;
	}
	
	public double mass() {
		return radius*radius*density;
	}
	
	/**
	 * Bounce off another ball
	 * @param b
	 * @param totVelX
	 * @param totVelY
	 */
	public void bounce(Ball b, double rotXVel1, double rotYVel1, double sinA, double cosA) {
		xVel=cosA*rotXVel1-sinA*rotYVel1;
		yVel=cosA*rotYVel1+sinA*rotXVel1;
	}
	
	/**
	 * Reflect off the walls
	 * @param topOrSide which wall
	 */
	public void wallBounce(boolean topOrSide) {
		if(topOrSide)	//top
			yVel*=-1;
		else
			xVel*=-1;
	}
	
	/**
	 * Checks collision
	 * @param b
	 * @return whether b and this collide
	 */
	public boolean collides(Ball b) {
		return distSq(b)<radius*radius+b.radius*b.radius;
	}
	
	/**
	 * Draws the ball to  window
	 * @param g	the graphics object
	 * @param w the window
	 */
	public void draw(Graphics2D g, DodgeWindow w) {
		double posInWindowX=xPos*MainClass.scale-w.getLocationOnScreen().x, posInWindowY=yPos*MainClass.scale-w.getLocationOnScreen().y;

		draw(g,posInWindowX,posInWindowY);
	}
	
	/**
	 * Does the actual drawing
	 * @param g				the graphics object
	 * @param posInWindowX	position x
	 * @param posInWindowY	position y
	 */
	public void draw(Graphics2D g, double posInWindowX, double posInWindowY) {
		g.setColor(new Color(0,0,0));
		
		g.fill(new Ellipse2D.Double(posInWindowX-radius*MainClass.scale,posInWindowY-radius*MainClass.scale,radius*2*MainClass.scale,radius*2*MainClass.scale));
	}
	
	public double distSq(Ball o) {
		return (o.xPos-xPos)*(o.xPos-xPos)+(o.yPos-yPos)*(o.yPos-yPos);
	}
	
}
