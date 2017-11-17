package exercise1;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Scanner;

public class My_Process implements Runnable, PrInter {
	public static int timestamp = 1;
	public static int process_id;
	public static LinkedList<Message> Received_Messages = new LinkedList<Message>();
	
	public My_Process(){}; 
	
	public static void main(String args[]) {
		
		System.out.println("P_id:"+args[2]);
	
		process_id = Integer.parseInt(args[2]);
		
		 //send implemented in main
	     (new Thread(new My_Process())).start();
		
	     Message m1 = new Message(timestamp,process_id,"Message from " + process_id , false);
	     timestamp++;
	     Scanner reader = new Scanner(System.in);
	     System.out.println("Press enter to send message: ");
	     String line = reader.nextLine();
	     while(!line.isEmpty()){
	    	 System.out.println("Press enter to send message: ");
		     line = reader.nextLine();
	     }
	     System.out.println("Boom");
	     //once finished
	     reader.close(); 
	     try {
	            Registry registry = LocateRegistry.getRegistry(null);
	            for(int i=1;i<4;i++){
	            	if(process_id!=i){
	            		PrInter stub = (PrInter) registry.lookup("PrInter"+i);
		 	            String response = stub.Receive_Message(m1);
		 	            System.out.println("response: " + response);
	            	}
	            }
	           
	        } catch (Exception e) {
	            System.err.println("Client exception: " + e.toString());
	            e.printStackTrace();
	        }
	     
	     while(true);
	    }

	@Override
	public void run() {
		//Receive part implemented in thread
		System.out.println("Receiver thread");
    
		try {
			My_Process obj = new My_Process();
			PrInter stub = (PrInter) UnicastRemoteObject.exportObject(obj, 0);
			
			// Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("PrInter"+process_id, stub);
            
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public String Receive_Message(Message m) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("Received msg from: " + m.get_pid() + "with ts: " + m.get_ts());
		Received_Messages.add(m);
		System.out.print("Received messages:");
		for(Message temp : Received_Messages){
			System.out.println(temp.get_pid());
		}
		return "Received";
	}
	 
}
