package projects.PI.nodes.messages;

import sinalgo.nodes.messages.Message;

public class INFMessage extends Message {
	public int senderID;
	public int messageID;
	
	public static int nbMessages;
	
	public INFMessage(int senderID) {
		this.senderID = senderID;
	}
	public INFMessage(int senderID, int messageID) {
		this.senderID = senderID;
		this.messageID = messageID;
	}

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new INFMessage(this.senderID);
	}

	

	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}
	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}
	
}
