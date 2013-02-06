import java.awt.Color;


public class AbsorbBall extends SplitterBall {
	public AbsorbBall(double x, double y) {
		super(x, y);
		col=Color.red;
	}

	public void bounce(Ball b, int i) {
		radius+=b.radius;
		MainClass.balls.set(i,null);
		
		if(b instanceof SplitterBall){
			Ball ball;
			int j=0;
			for(j=0;j<MainClass.balls.size();j++){
				ball=MainClass.balls.get(j);
				if(ball==this){
					break;
				}
			}
			
			if(j<MainClass.balls.size()){
				SplitterBall tmp=(SplitterBall)MainClass.balls.get(j);
				MainClass.balls.set(j,tmp);
			}
		}
	}

}
