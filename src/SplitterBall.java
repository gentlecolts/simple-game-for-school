import java.awt.Color;


public class SplitterBall extends SpecialBall{
	public SplitterBall(double x, double y) {
		super(x, y);
		col=Color.magenta;
	}

	public void bounce(Ball b, int i) {
		SplitterBall copy=
				new SplitterBall(
						xPos+sgn(xPos-b.xPos)*radius,
						yPos+sgn(yPos-b.yPos)*radius);
		radius/=2;
		radius=Math.min(radius, 5);
		
		copy.radius=radius;
		
		MainClass.balls.add(copy);
		this.xVel*=-1;
	}
	
	int sgn(double x){
		if(x>0){return 1;}
		if(x<0){return -1;}
		return 0;
	}
}
