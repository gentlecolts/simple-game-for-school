import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;


public abstract class Ball {
	public double xPos,yPos,xVel,yVel,xAccel,yAccel;
	public double radius;
	
	/**
	 * Move the ball smoothly, check collisions
	 * @param dt	the change in time so that movement isn't jerky
	 */
	public void update(double dt) {
		xPos+=xVel*dt;
		yPos+=yVel*dt;
		
		xVel+=xAccel*dt;
		yVel+=yAccel*dt;
		
		
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
		
		double totVelX=xVel+b.xVel,totVelY=yVel+b.yVel;
		
		bounce(b,totVelX,totVelY);
		b.bounce(this,totVelX,totVelY);
	}
	
	/**
	 * Bounce off another ball
	 * @param b
	 * @param totVelX
	 * @param totVelY
	 */
	public void bounce(Ball b, double totVelX, double totVelY) {
		double totMass=radius*radius+b.radius*b.radius;
		
		xVel=totVelX*radius*radius/totMass;
		yVel=totVelY*radius*radius/totMass;
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
		g.setColor(new Color(22,33,44));
		
		g.draw(new Ellipse2D.Double(posInWindowX-radius*MainClass.scale,posInWindowY-radius*MainClass.scale,radius*2*MainClass.scale,radius*2*MainClass.scale));
	}
	
	public double distSq(Ball o) {
		return (o.xPos-xPos)*(o.xPos-xPos)+(o.yPos-yPos)*(o.yPos-yPos);
	}
	
}
