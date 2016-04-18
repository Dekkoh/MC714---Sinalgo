package projects.PIF.nodes.timers;

import projects.PIF.nodes.nodeImplementations.PIFNode;
import sinalgo.nodes.Node;
import sinalgo.nodes.timers.Timer;

public class PIF_FeedbackTimer extends Timer {
	
	private PIFNode.TNO tno;
	PIFNode n;
	
	private int index;
	
	public PIF_FeedbackTimer (PIFNode n, PIFNode.TNO tno, int index){
		this.tno = tno;
		this.n = n;
		this.index = index;
	}
	
	
	@Override
	public void fire() {
		// TODO Auto-generated method stub
		n.timeout(tno, index);
	}
	
	public void tnoStartRelative(double time, Node n, PIFNode.TNO tno){
		this.tno=tno;
		super.startRelative(time, n);
	}
	public void tnoStartRelative(double time, Node n, PIFNode.TNO tno, int index){
		this.index = index;
		this.tno=tno;
		super.startRelative(time, n);
	}
	
	public void tnoStartGlobalRelative(double time, PIFNode.TNO tno){
		this.tno=tno;
		super.startGlobalTimer(time);
	}
	public void tnoStartAbsolute(double time, Node n, PIFNode.TNO tno){
		this.tno=tno;
		super.startAbsolute(time, n);
	}

}




	
	
	


