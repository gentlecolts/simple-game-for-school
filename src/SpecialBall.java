
abstract public class SpecialBall extends EnemyBall {
	public SpecialBall(double x, double y) {
		super(x, y);
	}
	
	public void update(double dt) {
		xPos+=xVel*dt;
		yPos+=yVel*dt;
		
		xVel+=xAccel*dt;
		yVel+=yAccel*dt;
		
		setAccel(dt);
		
		Ball b=null;
		for(int i=0;i<MainClass.balls.size();i++) {
			b=MainClass.balls.get(i);
			if(b!=null && id<b.id && collides(b)) {
				bounce(b,i);
			}
		}
		
		if(xPos-radius<MainClass.wallThickness/2*MainClass.scale || xPos+radius>MainClass.resolution*MainClass.xWindows/MainClass.yWindows-MainClass.wallThickness/2*MainClass.scale) {
			wallBounce(false);
		}
		
		if(yPos-radius<MainClass.wallThickness/2*MainClass.scale || yPos+radius>MainClass.resolution-MainClass.wallThickness/2*MainClass.scale) {
			wallBounce(true);
		}
	}
	
	public abstract void bounce(Ball b,int i);
}
