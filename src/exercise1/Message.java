package exercise1;

import java.io.Serializable;

public class Message implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ts;
	private int pid;
	private String mes;
	private boolean is_ack;
	
	public Message(int t, int p, String message,boolean is_ack)
	{
		this.ts = t;
		this.pid = p;
		this.mes = message;
		this.is_ack = is_ack;
	}
	
	public String get_mes(){
		return this.mes;
	}
	
	public int get_ts(){
		return this.ts;
	}
	
	public int get_pid(){
		return this.pid;
	}
	
	public boolean get_is_ack(){
		return this.is_ack;
	}
}