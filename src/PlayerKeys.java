import java.awt.event.*;
import java.util.concurrent.atomic.*;
/**
 * gets key presses and records the position of the player
 * @author Jack
 * 
 */
public class PlayerKeys implements KeyListener{
	//no atomic floating point data types in java
	//see http://stackoverflow.com/questions/5505460
	//FIXME: is y=0 on the top or bottom of the screen?
	AtomicInteger x,y;
	//PlayerBall player //TODO will do this iff you ask and make the x and y in PB atomic
	private double xdub,ydub;
	
	//these store the time the player started going in a direction
	//they will allow acceleration and/or floating point coords
	//all four are pressed to allow 
	//TODO determine if these should be bool or long, will need long to have acceleration
	//TODO are these being accesed elsewhere? if not they can be private longs instead
	AtomicBoolean
		lDown=new AtomicBoolean(false),
		rDown=new AtomicBoolean(false),
		uDown=new AtomicBoolean(false),
		dDown=new AtomicBoolean(false);
	
	//TODO vk is non numpad, is this an issue?
	static final int
		left=KeyEvent.VK_LEFT,
		right=KeyEvent.VK_RIGHT,
		up=KeyEvent.VK_UP,
		down=KeyEvent.VK_DOWN,
		w=KeyEvent.VK_W,
		a=KeyEvent.VK_A,
		s=KeyEvent.VK_S,
		d=KeyEvent.VK_D;
	public PlayerKeys(){
		this(0,0);
	}
	public PlayerKeys(int x0,int y0){
		x=new AtomicInteger(x0);
		y=new AtomicInteger(y0);
		xdub=x0;
		ydub=y0;
	}

	
	double getXAccel(){
		double accel=0;
		if(rDown.get()) {
			accel+=100;
		}
		if(lDown.get()) {
			accel-=100;
		}
		return accel;
	}

	double getYAccel(){
		double accel=0;
		if(uDown.get()) {
			accel-=100;
		}
		if(dDown.get()) {
			accel+=100;
		}
		
		return accel;
	}
	
	//if the key is down and has not been down then set it 
	public void keyPressed(KeyEvent arg0) {
		switch(arg0.getKeyCode()){
		case left:
		case a:
			lDown.set(true);
			break;
		case right:
		case d:
			rDown.set(true);
			break;
		case up:
		case w:
			uDown.set(true);
			break;
		case down:
		case s:
			dDown.set(true);
			break;
		}
	}

	//set the appropriate time to zero
	public void keyReleased(KeyEvent arg0) {
		switch(arg0.getKeyCode()){
		case left:
		case a:
			lDown.set(false);
			break;
		case right:
		case d:
			rDown.set(false);
			break;
		case up:
		case w:
			uDown.set(false);
			break;
		case down:
		case s:
			dDown.set(false);
			break;
		}
	}

	//this is unused
	public void keyTyped(KeyEvent arg0){}
}
