import java.awt.Color;


public class SplitterBall extends EnemyBall{
	public SplitterBall(double x, double y) {
		super(x, y);
		col=Color.magenta;
	}
	
	/**
	 * When it collides, split it in two
	 */
	public void bounce(Ball b, int i) {
		super.bounce(b,i);
		
		split(b);
	}
	
	/**
	 * Split the ball
	 * @param b
	 */
	void split(Ball b){
		if(!(b instanceof AbsorbBall)) {
			System.out.println("BOUNCE'D");
			SplitterBall copy=
					new SplitterBall(
							xPos+sgn(xPos-b.xPos)*radius,
							yPos+sgn(yPos-b.yPos)*radius);
			radius/=2;
			radius=Math.min(radius, 10);
			
			copy.radius=radius;
			
			MainClass.balls.add(copy);
			this.xVel*=-1;
		}else{
			b.bounce(this,MainClass.balls.indexOf(this));
		}
	}
	
	int sgn(double x){
		if(x>0){return 1;}
		if(x<0){return -1;}
		return 0;
	}
}
