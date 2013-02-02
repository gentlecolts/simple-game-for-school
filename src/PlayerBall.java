
public class PlayerBall extends Ball {
	static PlayerBall player=new PlayerBall();		//so that the camera knows what to follow
	
	/**
	 * There can only be one player.
	 */
	private PlayerBall() {
		super();
		xPos=MainClass.resolution/2;
		yPos=MainClass.resolution/2;
		
		density=100;
		radius=30;
	}

	@Override
	public void setAccel(double dt) {
		// TODO Auto-generated method stub
		
	}
	
}
