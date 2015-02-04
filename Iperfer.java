import java.util.*;
import java.net.*;
import java.io.*;
public class Iperfer {
	public static void main(String[] args){
		if(args.length == 0){
			System.out.println("Error: missing or additional arguments");
			System.exit(-1);
		}
		//client mode
		//java Iperfer -c -h <server hostname> -p <server port> -t <time>
		if(args[0].equals("-c")){
			client_mode(args);
			
		}
		//server mode
		//java Iperfer -s -p <listen port>
		else if(args[0].equals("-s")){
			server_mode(args);
			
		}else{
			System.out.println("Error: unknown argument");
			System.exit(-1);
		}
	}
	
	//client mode
	//java Iperfer -c -h <server hostname> -p <server port> -t <time>
	public static void client_mode(String[] args){
		//#args is checked
		if(args.length != 7){
			System.out.println("Error: missing or additional arguments");
			System.exit(-1);
		}
		if(!args[1].equals("-h") || !args[3].equals("-p") || !args[5].equals("-t")){
			System.out.println("Error: missing or additional arguments");
			System.exit(-1);
		}
		int port = -1;
		long time = -1;
		try{
			port = Integer.valueOf(args[4]);
			time = Long.valueOf(args[6]);
			time *= 1000; //convert in millisecs
			if(port < 1024 || port > 65535){
				System.out.println("Error: port number must be in the range 1024 to 65535");
				System.exit(-1);
			}
		}catch(NumberFormatException e){
			System.out.println("Error: number format");
			System.exit(-1);
		}
		String sName = args[2];
		Socket client = null;
		OutputStream toServer = null;
		try{
			client = new Socket(sName, port);
			toServer = client.getOutputStream();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		DataOutputStream out = new DataOutputStream(toServer);
		byte[] b = new byte[1000];
		long startTime = System.currentTimeMillis();
		while(System.currentTimeMillis() - startTime < time * 1000){
			try {
				out.write(b);
			}catch (IOException e) {		
				e.printStackTrace();
				System.exit(-1);
			}
		}
		//summary of sending
	}
	
	//server mode
	//java Iperfer -s -p <listen port>
	public static void server_mode(String[] args){
		if(args.length != 3){
			System.out.println("Error: missing or additional arguments");
			System.exit(-1);
		}
		int port = -1;
		try{
			port = Integer.valueOf(args[2]);
			if(port < 1024  || port > 65535){
				System.out.println("Error: port number must be in the range 1024 to 65535");
				System.exit(-1);
			}
		}catch(NumberFormatException e){
			System.out.println("Error: number format");
			System.exit(-1);
		}
		//while connection not closed by client
	}
}
