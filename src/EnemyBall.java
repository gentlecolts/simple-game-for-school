
public class EnemyBall extends Ball {

	final static double gravConst=.000000006;
	
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

}
