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
	public static int higherLevel = 0;
	public static int sentINF = 0;
	public static int sentFeedback = 0;
	public static int receivedFeedback = 0;
	
	public enum TNO {TNO_FEEDBACK, RESET_FEEDBACK, CANCEL_FEEDBACK }; //TNO_FEEDBACK: vai ter que passar pelo verificador de cancel, e se for aprovado, se torna RESET_FEEDBACK;
	//RESET_FEEDBACK: enum que determina se o broadcast irá ocorrer ou não.
//	Condições para o broadcast ocorrer:
//		Se o grau do nó atual for igual ao grau do maior nó de todos;
//		Se eu tiver recebido uma mensagem, e apenas uma, de algum filho de grau maior do que o meu.
	private PIF_FeedbackTimer feedbackTimer;
	
	DecimalFormat deci = new DecimalFormat("0.0000");
	//Listas de mensagens criadas por nós -> Elas são correspondentes 
	public List<INFMessage> reachedMessageList = new ArrayList<INFMessage>(); //Array que é listado o index das mensagens distintas.
	public List<FEEDBACKMessage> reachedFeedList = new ArrayList<FEEDBACKMessage>(); //Array que é listado o index da mensagem distinta, e se ele tem o menor level no grafo.
	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		int sender;
		
//		while(inbox.hasNext()) {
//			Message msg = inbox.next();
//			sender = inbox.getSender().ID;
//			
//			//N� recebeu uma mensagem INF	
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
//			//Mensagem de Confirma��o
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
			//N� recebeu uma mensagem INF	
			
			//Verifica��o de tipo de mensagem: Inf
				//Se for do tipo inf, ele se comporta 
			if(msg instanceof INFMessage) {
				System.out.println("Node: "+this.ID+" recebeu INF do Node  "+sender);
					INFMessage msgINF = (INFMessage) msg;
					
					//Setting node level with msg information
					if(this.nodeLevel > msgINF.getLevel()+1){
						this.nodeLevel = msgINF.getLevel()+1;	//Atualiza o level do node atual
						this.nextHopToSource = msgINF.getSenderID(); //Atualiza quem � o pai.			
						
					}
					//Setting the higherLevel -> Parameter to determine the level of the higher node. 
					if(this.nodeLevel > higherLevel){
						higherLevel = this.nodeLevel;
					}
					
					//Verifica��o: V� se ele envia ou n�o a mensagem de informa��o na rede.
					//Primeiro caso: Ele envia a mensagem de informa��o na rede.
					if(!this.reachedMessageList.contains((INFMessage) msg) && this.ID % 3 == 1) //novo pi, para msg
					{
						this.changeColorMessage(msgINF.getMessageID());
						reachedMessageList.add(msgINF);	
						msgINF.setSenderID(this.ID);
						
						//Modify the level carried by the message
//						msgINF.setLevel(this.nodeLevel + 1); //Antes: incluia +1 no nodeLevel. Comentei isso pq acho desnecessario, visto que o nodeLevel ja eh atualizado anteriormente,
						msgINF.setLevel(this.nodeLevel); //Agora: nao incrementa adicionalmente o nodeLevel. 
						MessageTimer infMSG = new MessageTimer(msgINF); //timer da msg. Já tinha no antigo pi
						infMSG.startRelative(0.1,this);
						
						//Agenda o FEEDBACK 
						feedbackTimer = new PIF_FeedbackTimer(this, TNO.TNO_FEEDBACK,((INFMessage) msg).getMessageID());
						feedbackTimer.tnoStartRelative(10, this, TNO.TNO_FEEDBACK, ((INFMessage) msg).getMessageID());	
					}
					//Segundo caso: ele n�o envia a mensagem de informa��o na rede. Ele temporariza o timer do feedback.
					else if(!this.reachedMessageList.contains((INFMessage) msg) && this.ID % 3 != 1){ //negação -> nao envia msg.
						//Agenda o FEEDBACK
						this.changeColorMessage(msgINF.getMessageID());
						reachedMessageList.add(msgINF); 
						
						feedbackTimer = new PIF_FeedbackTimer(this, TNO.TNO_FEEDBACK, ((INFMessage) msg).getMessageID());
						feedbackTimer.tnoStartRelative(10, this, TNO.TNO_FEEDBACK, ((INFMessage) msg).getMessageID());						
					}
				}
			
				
			//Mensagem de Confirmacao
			if(msg instanceof FEEDBACKMessage) {
				FEEDBACKMessage msgFeedback = (FEEDBACKMessage) msg;
				if (this.ID != 1){ //Metodo para reencaminhar o feedback para cima do grafo.
					
					if(msgFeedback.getLevel() ==this.nodeLevel + 1 ){
						if(!reachedFeedList.contains(msgFeedback)){
							reachedFeedList.add(msgFeedback);
							feedbackTimer = new PIF_FeedbackTimer(this, TNO.RESET_FEEDBACK,msgFeedback.getMessageID());
							feedbackTimer.tnoStartRelative(1, this, TNO.RESET_FEEDBACK, msgFeedback.getMessageID());
						}
					}
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
		
	public void feedbackStart(){ //Aki tá o comportamento do inicio do broadcast. Aparentemente...
		
		MessageTimer feedbackMSG = new MessageTimer (new FEEDBACKMessage(this.ID, this.ID, this.nextHopToSource, this.nodeLevel)); //construct correto
	  	feedbackMSG.startRelative(0.1, this);		
	}
	public void feedbackStart(int index){ //Aki tá o comportamento do inicio do broadcast. Aparentemente...
	
		MessageTimer feedbackMSG = new MessageTimer (new FEEDBACKMessage(this.ID, this.ID, this.nextHopToSource, index ,this.nodeLevel)); //construct correto
	  	feedbackMSG.startRelative(0.1, this);	
	  	this.changeColorFeedback(index);
	}
	
	public void timeout(TNO tno, int index){ //metodo que zera o contador, e inicia o Feedback Start. eh aqui que o fire do feedback timer manda.
		//TNO_FEEDBACK é usado para criar casos de quando iniciar o broadcast do feedback, baseado neste switch.
		//Dá pra criar uma arvore de casos diferente, e chamar um novo método, ignorando este timeout
		if(tno == TNO.TNO_FEEDBACK){
			tno = this.cancel(tno);
		}
		switch(tno){
			case TNO_FEEDBACK:
//				if(this.nodeLevel == higherLevel){ //Ele só vai começar o broadcast se for o nó de maior grau.
//					//agora falta criar a condição que se eu recebi uma mensagem de um nó de grau maior em 1 do que o meu, 
//					//eu iniciar o meu broadcast, e somente 1 vez. (pq pode ter mais de um broadcast de um nó de grau nodelevel+1)
				feedbackTimer = new PIF_FeedbackTimer(this, TNO.TNO_FEEDBACK, index);
				feedbackTimer.tnoStartRelative(5, this, TNO.TNO_FEEDBACK, index);
				
//				}
				break;
			case RESET_FEEDBACK: //nome incoerente. Ele vai inicializar o feedback, não resetar ele.
				feedbackStart(index);

				break;	
			case CANCEL_FEEDBACK:
				
				break;
		}
	}
	public TNO cancel(TNO tno){
		if(this.nodeLevel == higherLevel ){
			return TNO.RESET_FEEDBACK;
		}
		else
			return TNO.CANCEL_FEEDBACK;
		
	}
    @Override
	public void init() {
		//Considerando que o n� 1 tem a mensagem inf
    
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