import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.*;
/**
 * gets key presses and records the position of the player
 * @author Jack
 * 
 */
public class PlayerKeys implements KeyListener{
	
	//these store the time the player started going in a direction
	//they will allow acceleration and/or floating point coords
	//all four are pressed to allow 
	//TODO determine if these should be bool or long, will need long to have acceleration
	//TODO are these being accesed elsewhere? if not they can be private longs instead
	AtomicInteger
		rDown=new AtomicInteger(0),
		uDown=new AtomicInteger(0),
		dDown=new AtomicInteger(0),
		lDown=new AtomicInteger(0);
	
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
		EventQueue ev = Toolkit.getDefaultToolkit().getSystemEventQueue();
		// Override EventQueue so that the keylistener is focus-agnostic
		ev.push(new EventQueue(){
			protected void dispatchEvent(AWTEvent event)  {
				super.dispatchEvent(event);
			       // all AWTEvents can be identified by type, KeyEvent, MouseEvent, etc
				if(event instanceof KeyEvent) {
					KeyEvent keyEvent=(KeyEvent)event;
					
					if(keyEvent.getID()==KeyEvent.KEY_PRESSED) {
						keyPressed(keyEvent);
					} else if(keyEvent.getID()==KeyEvent.KEY_RELEASED); {
						keyReleased(keyEvent);
					}
				}
			}
		});

	}

	/** Get what the horizontal acceleration of the player should be
	 * @return the x acceleration
	 */
	double getXAccel(){
		double accel=0;
		if(rDown.get()>0) {
			accel+=100;
		}
		if(lDown.get()>0) {
			accel-=100;
		}
		return accel;
	}

	/** Get what the vertical acceleration of the player should be
	 * @return the y acceleration
	 */
	double getYAccel(){
		double accel=0;
		if(uDown.get()>0) {
			accel-=100;
		}
		if(dDown.get()>0) {
			accel+=100;
		}
		
		return accel;
	}
	
	//if the key is down and has not been down then set it 
	public void keyPressed(KeyEvent arg0) {
		switch(arg0.getKeyCode()){
		case left:
		case a:
			lDown.set(2);
			break;
		case right:
		case d:
			rDown.set(2);
			break;
		case up:
		case w:
			uDown.set(2);
			break;
		case down:
		case s:
			dDown.set(2);
			break;
		}
	}

	//set the appropriate time to zero
	public void keyReleased(KeyEvent arg0) {
		switch(arg0.getKeyCode()){
		case left:
		case a:
			lDown.set(lDown.get()-1);
			break;
		case right:
		case d:
			rDown.set(rDown.get()-1);
			break;
		case up:
		case w:
			uDown.set(uDown.get()-1);
			break;
		case down:
		case s:
			dDown.set(dDown.get()-1);
			break;
		}
	}

	//this is unused
	public void keyTyped(KeyEvent arg0){}
}
