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
	
	public static double cameraOffsetX() {
		double offset=(-PlayerBall.player.xPos)*MainClass.scale+MainClass.screenWidth/2;
		
		if(offset>0)
			offset=0;
		
		else if(offset+MainClass.resolution*MainClass.xWindows/MainClass.yWindows*MainClass.scale<MainClass.screenWidth)
			offset=MainClass.screenWidth-MainClass.resolution*MainClass.xWindows/MainClass.yWindows*MainClass.scale;
		
		return offset;
	}
	
	public static double cameraOffsetY() {
		double offset=(-PlayerBall.player.yPos)*MainClass.scale+MainClass.screenHeight/2;
		
		if(offset>0)
			offset=0;
		
		else if(offset+MainClass.resolution*MainClass.scale<MainClass.screenHeight)
			offset=MainClass.screenHeight-MainClass.resolution*MainClass.scale;
		
		return offset;
	}
	
	public double screenSpaceX() {
		return xPos*MainClass.scale+MainClass.edgePaddingX+cameraOffsetX();
	}
	
	public double screenSpaceY() {
		return yPos*MainClass.scale+MainClass.edgePaddingY+cameraOffsetY();
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
		
		if(xPos-radius<MainClass.wallThickness/2*MainClass.scale || xPos+radius>MainClass.resolution*MainClass.xWindows/MainClass.yWindows-MainClass.wallThickness/2*MainClass.scale) {
			wallBounce(false);
		}
		
		if(yPos-radius<MainClass.wallThickness/2*MainClass.scale || yPos+radius>MainClass.resolution-MainClass.wallThickness/2*MainClass.scale) {
			wallBounce(true);
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
		
		double unitNormalX=(b.xPos-xPos)/dist,unitNormalY=(b.yPos-yPos)/dist;		//make tangent and normal vecotrs
		double unitTangentX=-unitNormalY,unitTangentY=unitNormalX;
		
		double normalVel1=xVel*unitNormalX+yVel*unitNormalY;
		double normalVel2=b.xVel*unitNormalX+b.yVel*unitNormalY;
		
		double tangentVel1=xVel*unitTangentX+yVel*unitTangentY;
		double tangentVel2=b.xVel*unitTangentX+b.yVel*unitTangentY;
		
		
		double normalVel1Prime=(normalVel1*(mass()-b.mass())+2*b.mass()*normalVel2)/(mass()+b.mass());
		double normalVel2Prime=(normalVel2*(b.mass()-mass())+2*mass()*normalVel1)/(mass()+b.mass());
		
		bounce(b, unitNormalX*normalVel1Prime+unitTangentX*tangentVel1,unitNormalY*normalVel1Prime+unitTangentY*tangentVel1);
		b.bounce(this, unitNormalX*normalVel2Prime+unitTangentX*tangentVel2,unitNormalY*normalVel2Prime+unitTangentY*tangentVel2);
		
		double angle=Math.atan2(yPos-b.yPos, xPos-b.xPos);
		double cosA=Math.cos(angle);
		double sinA=Math.sin(angle);
		
		double diff=(radius+b.radius-dist)/2;
		
		double cosD=cosA*diff;
		double sinD=sinA*diff;
		
		xPos+=cosD;
		yPos+=sinD;
		b.xPos-=cosD;
		b.yPos-=sinD;
	}
	
	public double mass() {
		return radius*radius*density;
	}
	
	/**
	 * Bounce off another ball; this just sets velocity, but gives a hook for being overridden so player collision can damage health.
	 * @param nXVel
	 * @param nYVel
	 */
	public void bounce(Ball b, double nXVel, double nYVel) {
		xVel=nXVel;
		yVel=nYVel;
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
		return distSq(b)<(radius+b.radius)*(radius+b.radius);
	}
	
	/**
	 * Draws the ball to  window
	 * @param g	the graphics object
	 * @param w the window
	 */
	public void draw(Graphics2D g, DodgeWindow w) {
		double posInWindowX=screenSpaceX()-w.getLocationOnScreen().x, posInWindowY=screenSpaceY()-w.getLocationOnScreen().y;

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
