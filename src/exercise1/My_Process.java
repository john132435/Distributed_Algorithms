package exercise1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
	public static LinkedList<Message> fifo;
	
	public My_Process(){}; 
	
	public static void main(String args[]) {
		
		LinkedList<Message> ls;
		 
		  try {
		  
		   System.out.print("File to check is "+args[2]);
		   String f = "C:/Users/Kostas/workspace/rmi/bin/exercise1/Messages"+args[2]+".txt";
		   System.out.print("String to check is " + f);
		         File file = new File(f);
		         ls = LoadMessages(file);
		         fifo = ls;
		         for (int i=0; i<fifo.size(); i++) System.out.print("INDEX"+i);
		       }
		       catch(IOException ioe) {
		          ioe.printStackTrace();
		       }
		       System.out.print("SIZE = "+fifo.size());
		
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
	
	public static LinkedList<Message> LoadMessages(File f) throws IOException {
		   int index;
		   String ts, pid;
		   int t,p;
		   Message m;
		   LinkedList<Message> list = new LinkedList<Message>();
		   FileInputStream fis = new FileInputStream(f);
		   
		   //Construct BufferedReader from InputStreamReader
		   BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		   
		   String line = null;
		   while ((line = br.readLine()) != null) {
		    try{
		    if (line==null) 
		    	System.out.println("FOUND");
		     index = line.indexOf(',');
		     if (index>0){ 
		     ts = line.substring(0,index);
		     pid = line.substring(index+1,line.length());
		    
		    t = Integer.parseInt(ts);
		    p = Integer.parseInt(pid);
		    System.out.println("timestamp = "+t+",pid = "+p);
		    
		    m = new Message(t, p,"Message from "+p,false);
		    //m.print();
		    list.add(m);
		     }
		    }catch (Exception e) {
		              System.err.println("Load exception: " + e.toString());
		              e.printStackTrace();
		          }
		   }
		   
		   br.close();
		   
		   return list;
		  }
	 
}
