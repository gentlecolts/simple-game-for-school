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
	AtomicLong
		ltime=new AtomicLong(0),
		rtime=new AtomicLong(0),
		utime=new AtomicLong(0),
		dtime=new AtomicLong(0);
	
	//TODO vk is non numpad, is this an issue?
	static final int
		left=KeyEvent.VK_LEFT,
		right=KeyEvent.VK_RIGHT,
		up=KeyEvent.VK_UP,
		down=KeyEvent.VK_DOWN,
		w=(int)'w',
		a=(int)'a',
		s=(int)'s',
		d=(int)'d';
	public PlayerKeys(){
		this(0,0);
	}
	public PlayerKeys(int x0,int y0){
		x=new AtomicInteger(x0);
		y=new AtomicInteger(y0);
		xdub=x0;
		ydub=y0;
	}
	
	//constantly updating the x and y positions
	class moveloop extends Thread{
		public void run(){
			long t,rdt,ldt,udt,ddt;
			long dx,dy;
			while(true){
				t=System.currentTimeMillis();
				ldt=ltime.get();
				rdt=rtime.get();
				udt=utime.get();
				ddt=dtime.get();
				
				if(ldt!=0){ldt=t-ldt;}
				if(rdt!=0){rdt=t-rdt;}
				if(udt!=0){udt=t-udt;}
				if(ddt!=0){ddt=t-ddt;}
				
				dx=2*(Math.min(rdt, 1000)-Math.min(ldt,1000))/1000;
				dy=2*(Math.min(udt, 1000)-Math.min(ddt,1000))/1000;
				x.addAndGet((int)dx);
				y.addAndGet((int)dy);
			}
		}
	}
	
	//if the key is down and has not been down then set it 
	public void keyPressed(KeyEvent arg0) {
		switch(arg0.getKeyCode()){
		case left:
		case a:
			ltime.compareAndSet(0,System.currentTimeMillis());
			break;
		case right:
		case d:
			rtime.compareAndSet(0,System.currentTimeMillis());
			break;
		case up:
		case w:
			utime.compareAndSet(0,System.currentTimeMillis());
			break;
		case down:
		case s:
			dtime.compareAndSet(0,System.currentTimeMillis());
			break;
		}
	}

	//set the appropriate time to zero
	public void keyReleased(KeyEvent arg0) {
		switch(arg0.getKeyCode()){
		case left:
		case a:
			ltime.set(0);
			break;
		case right:
		case d:
			rtime.set(0);
			break;
		case up:
		case w:
			utime.set(0);
			break;
		case down:
		case s:
			dtime.set(0);
			break;
		}
	}

	//this is unused
	public void keyTyped(KeyEvent arg0){}
}
