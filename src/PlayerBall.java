
public class PlayerBall extends Ball {
	static PlayerBall player=new PlayerBall();		//so that the camera knows what to follow
	
	/**
	 * There can only be one player.
	 */
	private PlayerBall() {
		
	}

	@Override
	public void setAccel(double dt) {
		// TODO Auto-generated method stub
		
	}
	
}
