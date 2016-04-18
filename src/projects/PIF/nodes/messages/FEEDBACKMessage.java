package projects.PIF.nodes.messages;

import sinalgo.nodes.messages.Message;

public class FEEDBACKMessage extends Message {
	public int destinationID;
	public int senderID;
	public int sourceFeedbackID;
	public int messageID;
	public int level;

	public FEEDBACKMessage(int sourceFeedbackID, int senderID, int destinationID) {
		this.destinationID = destinationID;
		this.senderID = senderID;
		this.sourceFeedbackID = sourceFeedbackID;
	}
	public FEEDBACKMessage(int sourceFeedbackID, int senderID, int destinationID, int messageID) {
		this.destinationID = destinationID;
		this.senderID = senderID;
		this.sourceFeedbackID = sourceFeedbackID;
		this.messageID = messageID;
	}
	public FEEDBACKMessage(int sourceFeedbackID, int senderID, int destinationID, int messageID, int level) {
		this.destinationID = destinationID;
		this.senderID = senderID;
		this.sourceFeedbackID = sourceFeedbackID;
		this.messageID = messageID;
		this.level = level;
	}
	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new FEEDBACKMessage(this.sourceFeedbackID, this.senderID, this.destinationID, this.messageID, this.level);
	}

	public int getSourceFeedbackID() {
		return sourceFeedbackID;
	}

	public void setSourceFeedbackID(int sourceFeedbackID) {
		this.sourceFeedbackID = sourceFeedbackID;
	}

	public int getDestinationID() {
		return destinationID;
	}

	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}

	public void setDestinationID(int destinationID) {
		this.destinationID = destinationID;
	}
	public int getMessageID() {
		return messageID;
	}

	public void setMessagerID(int MessageID) {
		this.messageID = MessageID;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int Level) {
		this.level = Level;
	}

	public boolean equals(Object obj) { //Mudei o campo do equals pra inserir na lista de feedbacks.
		//Ele compara se o endereco da mensagem eh igual (index msg).
		//se for, entao ele diz que eh verdadeiro, e inclui na lista. senao, falso
		FEEDBACKMessage msg = (FEEDBACKMessage) obj;
		if(msg.getMessageID() == this.messageID){
		//	if(msg.getLevel() > this.level){ Mantenha comentado
				return true;
	//		} Mantenha comentado.
			
		}
		return false;
		
	}
}
