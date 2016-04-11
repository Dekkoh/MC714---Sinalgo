package projects.PIF.nodes.messages;
import sinalgo.nodes.messages.Message;

public class INFMessage extends Message {
	public int senderID;
	public int messageID;
	public int level;
	public static int nbMessages;
	
	public INFMessage(int senderID) {
		this.senderID = senderID;
		nbMessages++;
	}
	
	public INFMessage(int senderID, int messageID) {
		this.senderID = senderID;
		this.messageID = messageID;
		//System.out.println("MsgID : "+this.messageID);
		nbMessages++;
	}
	
	public INFMessage(int senderID, int messageID, int level) {
		this.senderID = senderID;
		this.messageID = messageID;
		this.level = level;
		//System.out.println("MsgID : "+this.messageID);
		nbMessages++;
	}
	

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new INFMessage(this.senderID, this.messageID, this.level);
	}


	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}
	
	public int getMessageID() {
		return messageID;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public boolean equals(Object obj) {		
		return this.messageID == ((INFMessage) obj).getMessageID();
	}
	
	
}
