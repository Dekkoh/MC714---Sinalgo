package projects.PIF.nodes.nodeImplementations;
import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
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
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

public class PIFNode extends Node {
	//private boolean reached = false;
	private int nextHopToSource;
	private int nodeLevel = 99999;
	public static int sentINF = 0;
	public static int sentFeedback = 0;
	public static int receivedFeedback = 0;
	
	public enum TNO {TNO_FEEDBACK };
	private PIF_FeedbackTimer feedbackTimer;
	DecimalFormat deci = new DecimalFormat("0.0000");
	public List<INFMessage> reachedMessageList = new ArrayList<INFMessage>();
	public List<FEEDBACKMessage> reachedFeedList = new ArrayList<FEEDBACKMessage>();
	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		int sender;
		
//		while(inbox.hasNext()) {
//			Message msg = inbox.next();
//			sender = inbox.getSender().ID;
//			
//			//Nï¿½ recebeu uma mensagem INF	
//			if(msg instanceof INFMessage) {
//				System.out.println("Node: "+this.ID+" recebeu INF do Node  "+sender);
//				INFMessage msgINF = (INFMessage) msg;
//				
//				//Setting node level with msg information
//				if(this.nodeLevel > msgINF.getLevel()+1){
//					this.nodeLevel = msgINF.getLevel()+1;
//				}
//				
//				if(!this.reachedList.contains((INFMessage) msg))
//				{
//					this.setColor(Color.GREEN);
//					reachedList.add(msgINF);
//					//this.reached = true;	
//					this.nextHopToSource = msgINF.getSenderID();
//					msgINF.setSenderID(this.ID);
//					//Modify the level carried by the message
//					msgINF.setLevel(this.nodeLevel+1);
//					MessageTimer infMSG = new MessageTimer(msgINF);
//					infMSG.startRelative(0.1,this);
//					
//					//Agenda o FEEDBACK
//					feedbackTimer = new PIF_FeedbackTimer(this, TNO.TNO_FEEDBACK);
//					feedbackTimer.tnoStartRelative(10, this, TNO.TNO_FEEDBACK);	
//				}else{
//					//Agenda o FEEDBACK
//					feedbackTimer = new PIF_FeedbackTimer(this, TNO.TNO_FEEDBACK);
//					feedbackTimer.tnoStartRelative(0.1, this, TNO.TNO_FEEDBACK);						
//				}
//			}
//			
//			//Mensagem de Confirmaï¿½ï¿½o
//			if(msg instanceof FEEDBACKMessage) {
//				FEEDBACKMessage msgFeedback = (FEEDBACKMessage) msg;
//				if (this.ID != 1){
//					if(msgFeedback.getDestinationID() == this.ID){
//						msgFeedback.setDestinationID(this.nextHopToSource);
//						MessageTimer feedbackMSG = new MessageTimer(msgFeedback);
//						feedbackMSG.startRelative(0.1,this);
//						System.out.println("Node: "+this.ID+" Recebeu Feedback do Node "+ msgFeedback.getSourceFeedbackID() + " encaminhada pelo Node " +msgFeedback.getSenderID());	
//					}
//				}else{ 
//					System.out.println("Source node recebeu Feedback do Node "+ msgFeedback.getSourceFeedbackID());
//					receivedFeedback = receivedFeedback + 1;
//				}
//			}
//		}
		//Primeira parte do PIFNode: Ele verifica se tem visinhos pra ele mandar a msg (inboc.hasnext).
		while(inbox.hasNext()) {
			Tools.appendToOutput("\n n neighboors:"+this.outgoingConnections.size());
			Message msg = inbox.next();
			sender = inbox.getSender().ID;
			//Tools.appendToOutput("\n ID : "+ inbox.getReceiver().ID + "\n");
			//Nï¿½ recebeu uma mensagem INF	
			
			//Verificação de tipo de mensagem: Inf
				//Se for do tipo inf, ele se comporta 
			if(msg instanceof INFMessage) {
				System.out.println("Node: "+this.ID+" recebeu INF do Node  "+sender);
					INFMessage msgINF = (INFMessage) msg;
					
					//Setting node level with msg information
					if(this.nodeLevel > msgINF.getLevel()+1){
						this.nodeLevel = msgINF.getLevel()+1;	//Atualiza o level do node atual
						this.nextHopToSource = msgINF.getSenderID(); //Atualiza quem é o pai.				
						
					}
					//Verificação: Vé se ele envia ou não a mensagem de informação na rede.
					//Primeiro caso: Ele envia a mensagem de informação na rede.
					if(!this.reachedMessageList.contains((INFMessage) msg) && this.ID % 3 == 1)
					{
						this.changeColorMessage(msgINF.getMessageID());
						reachedMessageList.add(msgINF);
						//this.reached = true;		
//						this.nextHopToSource = msgINF.getSenderID();
						msgINF.setSenderID(this.ID);
						//Modify the level carried by the message
//						msgINF.setLevel(this.nodeLevel + 1); //Antes: incluia +1 no nodeLevel. Comentei isso pq acho desnecessário, visto que o nodeLevel já é atualizado anteriormente,
						msgINF.setLevel(this.nodeLevel); //Agora: não incrementa adicionalmente o nodeLevel. 
						MessageTimer infMSG = new MessageTimer(msgINF);
						infMSG.startRelative(0.1,this);
						
						//Agenda o FEEDBACK
						feedbackTimer = new PIF_FeedbackTimer(this, TNO.TNO_FEEDBACK);
						feedbackTimer.tnoStartRelative(10, this, TNO.TNO_FEEDBACK);	
					}
					//Segundo caso: ele não envia a mensagem de informação na rede. Ele temporariza o timer do feedback.
					else if(!this.reachedMessageList.contains((INFMessage) msg) && this.ID % 3 != 1){ 
						//Agenda o FEEDBACK
						this.changeColorMessage(msgINF.getMessageID());
						reachedMessageList.add(msgINF);
						feedbackTimer = new PIF_FeedbackTimer(this, TNO.TNO_FEEDBACK);
						feedbackTimer.tnoStartRelative(10, this, TNO.TNO_FEEDBACK);						
					}
				}
			
				
			//Mensagem de Confirmaï¿½ï¿½o
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
		MessageTimer feedbackMSG = new MessageTimer (new FEEDBACKMessage(this.ID, this.ID, this.nextHopToSource, this.nodeLevel));
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
		//Considerando que o nï¿½ 1 tem a mensagem inf
    
//		if (this.ID==1){
//			for(int i=0; i<1000; i++){
//				this.setColor(Color.RED);
//				this.nextHopToSource = this.ID;
//
//				INFMessage msg = new INFMessage(this.ID, i, 0);
//				MessageTimer infMSG = new MessageTimer (msg);
//				reachedList.add(msg);
//		  		infMSG.startRelative(0.1, this);
//			}
//		}
    	
    	if (this.ID==1){
    		this.nextHopToSource = this.ID;
    		this.nodeLevel = 0;
			for(int i=0; i<1000; i++){
				this.setColor(Color.RED);
				INFMessage msg = new INFMessage(this.ID, i, this.nodeLevel);
				MessageTimer infMSG = new MessageTimer (msg);
				reachedMessageList.add(msg);
		  		infMSG.startRelative(0.1 + 5*i, this);
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
	public void changeColorMessage(int ID) {
		switch (ID % 5) {
		case 0:
			this.setColor(Color.GREEN);
			break;
		case 1:
			this.setColor(Color.BLUE);
			break;
		case 2:
			this.setColor(Color.YELLOW);
			break;
		case 3:
			this.setColor(Color.CYAN);
			break;
		case 4:
			this.setColor(Color.MAGENTA);
			break;
		}
	}
	public void changeColorFeedback(int ID) {
			switch (ID % 5) {
			case 0:
				this.setColor(Color.ORANGE);
				break;
			case 1:
				this.setColor(Color.BLACK);
				break;
			case 2:
				this.setColor(Color.DARK_GRAY);
				break;
			case 3:
				this.setColor(Color.LIGHT_GRAY);
				break;
			case 4:
				this.setColor(Color.PINK);
				break;
			}
		}

}