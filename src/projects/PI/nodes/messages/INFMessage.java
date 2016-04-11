package projects.PI.nodes.messages;

import sinalgo.nodes.messages.Message;

public class INFMessage extends Message {
	public int senderID;
	public int messageID;
	
	public static int nbMessages = 0;
	
	public INFMessage(int senderID) {
		this.senderID = senderID;
		System.out.println("New message created");
		//this.messageID = nbMessages;
		nbMessages++;
	}
	
	public INFMessage(int senderID, int messageID) {
		this.senderID = senderID;
		this.messageID = messageID;
		System.out.println("MsgID : "+this.messageID);
		nbMessages++;
	}
	

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new INFMessage(this.senderID, this.messageID);
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

	public int getMessageID() {
		return messageID;
	}

	@Override
	public boolean equals(Object obj) {
		
		if(this.messageID == ((INFMessage) obj).getMessageID()){
			//System.out.println("EQUAL " + this.messageID + " - " + ((INFMessage) obj).getMessageID());
			return true;
		}else
			//System.out.println("DIFFERENT" + this.messageID + " - " + ((INFMessage) obj).getMessageID());
			return false;
	}
	
	
	
}
