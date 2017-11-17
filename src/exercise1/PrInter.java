package exercise1;

import java.rmi.Remote;

public interface PrInter extends Remote{
	public String Receive_Message(Message m) throws java.rmi.RemoteException;
}
