package projects.PI.nodes.nodeImplementations;
import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import projects.PI.nodes.messages.INFMessage;
import projects.PI.nodes.timers.MessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

public class PINode extends Node {
	private boolean reached = false;
	public static int sentINF = 0;
	
	public List<INFMessage> reachedList = new ArrayList<INFMessage>();
		
	DecimalFormat deci = new DecimalFormat("0.0000");

	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		int sender;
		
		while(inbox.hasNext()) {
			Tools.appendToOutput("\n n neighboors:"+this.outgoingConnections.size());
			Message msg = inbox.next();
			sender = inbox.getSender().ID;
			//Tools.appendToOutput("\n ID : "+ inbox.getReceiver().ID + "\n");
			//N� recebeu uma mensagem INF	
			if(msg instanceof INFMessage) {
				//Verifica se � a primeira vez que o n� recebe INF
				//if(this.reachedList.contains((INFMessage) msg)) System.out.println("contains ");
				//if(!this.reachedList.contains((INFMessage) msg))  System.out.println("DOESNT contains  ");
				//System.out.println(reachedList.size());
				
				if(!this.reachedList.contains((INFMessage) msg) && this.ID % 3 == 1){
					this.setColor(Color.GREEN);
					reachedList.add((INFMessage)msg);
					//this.reached = true;
					Tools.appendToOutput("\n\n TIME: "+ deci.format(Global.currentTime));
					Tools.appendToOutput("\n Node " + this.ID +" recebeu INF de"+ sender);
					MessageTimer infMSG = new MessageTimer(msg);
					infMSG.startRelative(1,this);
						
				}else if (!this.reachedList.contains((INFMessage) msg) && this.ID % 3 != 1) {
					this.setColor(Color.GREEN);
					//this.reached = true;
					reachedList.add((INFMessage)msg);
					Tools.appendToOutput("\n\n TIME: "+ deci.format(Global.currentTime));
					Tools.appendToOutput("\n Node " + this.ID +" recebeu INF de"+ sender);
				}
			
			}
			
		}
		
	}
		

    @Override
	public void init() {
		//Considerando que o n� 1 tem a mensagem inf
    	
		if (this.ID==1){
			for(int i=0; i<1000; i++){
				this.setColor(Color.RED);
				INFMessage msg = new INFMessage(this.ID, i);
				MessageTimer infMSG = new MessageTimer (msg);
				reachedList.add(msg);
		  		infMSG.startRelative(0.1, this);
			}
    	}

	}
    
	@Override
	public void postStep() {
		// TODO Auto-generated method stub
	}

	@Override
	public void preStep() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		// TODO Auto-generated method stub
		if (this.ID == 1) highlight = true;
		super.drawNodeAsDiskWithText(g, pt, highlight, Integer.toString(this.ID), 8, Color.WHITE);
		
		
	}
	
	@Override
	public void neighborhoodChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkRequirements() throws WrongConfigurationException {
		// TODO Auto-generated method stub
		
	}

}