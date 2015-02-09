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
		InetAddress sip = null;
		try{
		    sip = InetAddress.getByName(sName);
		}catch (UnknownHostException e){
		    //do nothing
		}
		Socket client = null;
		OutputStream toServer = null;
		try{
		    if(sip != null){
			client = new Socket(sip, port);
		    }else{
			client = new Socket(sName, port);
		    }
			toServer = client.getOutputStream();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		DataOutputStream out = new DataOutputStream(toServer);
		byte[] b = new byte[1000];
		long count = 0;
		long startTime = System.currentTimeMillis();
		//System.out.println("time limit " + time*1000);
		while(System.currentTimeMillis() - startTime < time){
		//	System.out.println("duration = " + (System.currentTimeMillis()-startTime));
			try {
				out.write(b);
				count++;
			}catch (IOException e) {		
				e.printStackTrace();
				System.exit(-1);
			}
		}
		try {
			client.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("closing err");
			System.exit(-1);
		}
		double rate = 0.0;
		if(time != 0){
			rate = count*1000*8*1.0/time*1000;
		}
		//summary of sending
		System.out.println("receive=" + count +" KB " + "rate=" + rate + " Mbps"); 
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
		ServerSocket server;
		Socket connection;
		try {
			server = new ServerSocket(port);
			server.setSoTimeout(0); //infinte timeout
			connection = server.accept();
			DataInputStream in = new DataInputStream(connection.getInputStream());
			byte[] buffer = new byte[10000];
			int count = 0;
			long sum = 0;
			long startTime = System.currentTimeMillis();
			while((count = in.read(buffer)) != -1){
				sum += count;
			}
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			in.close();
			server.close();
			connection.close();
			double rate = 0; 
			if(sum > 0){	
				rate = sum*8*1.0/duration*1000;
			}
			System.out.println("received=" + sum/1000 + " KB " + "rate=" + rate + " Mbps");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//while connection not closed by client
	}
}
