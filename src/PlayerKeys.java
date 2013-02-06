import java.awt.*;
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
		x=new AtomicInteger(x0);
		y=new AtomicInteger(y0);
		xdub=x0;
		ydub=y0;
		
		EventQueue ev = Toolkit.getDefaultToolkit().getSystemEventQueue();
		// MyCustomEventQueue extends EventQueue and processes keyboard events in the dispatch
		ev.push(new EventQueue(){
			protected void dispatchEvent(AWTEvent event)  {
				super.dispatchEvent(event);
			       // all AWTEvents can be indentified by type, KeyEvent, MouseEvent, etc
			       // look for KeyEvents and match against you hotkeys / callbacks
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

	
	double getXAccel(){
		double accel=0;
		if(rDown.get()>0) {
			accel+=100;
		}
		if(lDown.get()>0) {
			accel-=100;
		}
		//System.out.println("lDown: "+lDown);
		return accel;
	}

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
			//System.out.println("++++++++++++++++++++++++lDown: "+lDown);
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
			//System.out.println("-----------------------lDown: "+lDown);
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
