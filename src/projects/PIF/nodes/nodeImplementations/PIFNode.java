package projects.PIF.nodes.nodeImplementations;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import projects.PIF.nodes.messages.FEEDBACKMessage;
import projects.PIF.nodes.messages.INFMessage;
import projects.PIF.nodes.timers.PIF_FeedbackTimer;
import projects.PIF.nodes.timers.MessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

public class PIFNode extends Node {
	//private boolean reached = false;
	private int nextHopToSource;
	private int nodeLevel = 0;
	public static int sentINF = 0;
	public static int sentFeedback = 0;
	public static int receivedFeedback = 0;
	
	public enum TNO {TNO_FEEDBACK };
	private PIF_FeedbackTimer feedbackTimer;
	
	public List<INFMessage> reachedList = new ArrayList<INFMessage>();
	
	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		int sender;
		
		while(inbox.hasNext()) {
			Message msg = inbox.next();
			sender = inbox.getSender().ID;
			
			//N� recebeu uma mensagem INF	
			if(msg instanceof INFMessage) {
				System.out.println("Node: "+this.ID+" recebeu INF do Node  "+sender);
				INFMessage msgINF = (INFMessage) msg;
				
				if(!this.reachedList.contains((INFMessage) msg))
				{
					this.setColor(Color.GREEN);
					reachedList.add((INFMessage)msg);
					//this.reached = true;	
					this.nextHopToSource = msgINF.getSenderID();
					msgINF.setSenderID(this.ID);
					MessageTimer infMSG = new MessageTimer(msgINF);
					infMSG.startRelative(0.1,this);
					
					//Agenda o FEEDBACK
					feedbackTimer = new PIF_FeedbackTimer(this, TNO.TNO_FEEDBACK);
					feedbackTimer.tnoStartRelative(10, this, TNO.TNO_FEEDBACK);	
				}
			}
			
			//Mensagem de Confirma��o
			if(msg instanceof FEEDBACKMessage) {
				FEEDBACKMessage msgFeedback = (FEEDBACKMessage) msg;
				if (this.ID != 1){
					if(msgFeedback.getDestinationID() == this.ID){
						msgFeedback.setDestinationID(this.nextHopToSource);
						MessageTimer feedbackMSG = new MessageTimer(msgFeedback);
						feedbackMSG.startRelative(0.1,this);
						System.out.println("Node: "+this.ID+" Recebeu Feedback do Node "+ msgFeedback.getSourceFeedbackID() + " encaminhada pelo Node " +msgFeedback.getSenderID());	
					}
				}else{ 
					System.out.println("Source node recebeu Feedback do Node "+ msgFeedback.getSourceFeedbackID());
					receivedFeedback = receivedFeedback + 1;
				}
			}
		}
	}
			
		
	public void feedbackStart(){
		MessageTimer feedbackMSG = new MessageTimer (new FEEDBACKMessage(this.ID, this.ID, this.nextHopToSource));
	  	feedbackMSG.startRelative(0.1, this);		
	}
	
	public void timeout(TNO tno){
		switch(tno){
			case TNO_FEEDBACK:
				feedbackStart();
				break;	
		}
	}

    @Override
	public void init() {
		//Considerando que o n� 1 tem a mensagem inf
    
		if (this.ID==1){
			for(int i=0; i<1000; i++){
				this.setColor(Color.RED);
				this.nextHopToSource = this.ID;

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
		//if (this.ID == 1) highlight = true;
		super.drawNodeAsDiskWithText(g, pt, highlight, Integer.toString(this.ID), 6, Color.WHITE);
		
		
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