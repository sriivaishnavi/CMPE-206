import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.text.*;


class SocketConnection{
String 	registrationDate;
String 	targetHostIP;
String 	targetAddressName;
int	targetPortNumber;
Socket 	targetSocket;


  SocketConnection() {
	//Date date = new Date();
	
	registrationDate= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
  }
}

public class MasterBot {
  public static String userCommand;
  public static ArrayList<SocketConnection> connectionList;
  public static void main(String[] args) throws Exception {
      connectionList = new ArrayList<SocketConnection>();
	if (args.length<2){
	  System.out.println("Port number should be provided for server.");
	 System.exit(-1);	
	}
	    try {
	String port="";
        if (args[0].equals("-p")){	
      		port=args[1];
	}
	else{
	System.out.println("port number should be provided for server.");
	System.exit(-1);
	}
      SocketThread clientThread = new SocketThread(Integer.parseInt(port));
      clientThread.start();   
	while(true){
		System.out.print(">");
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
		 userCommand= userInput.readLine();

		      if (userCommand !="" && ! userCommand.equals("list") ) {
 			String[] serverCommand = userCommand.split(" ");
					if(serverCommand[0].equals("connect")){
						if(serverCommand.length<4) {
							System.out.println("minimum 3 arguments required ");
						}
						else{
						if(serverCommand[1] != null && serverCommand[2]!= null && serverCommand[3]!= null){    	
							
							int numConn = 1;
							boolean keepAlive = false;
							String urlPath = "";
							
							if(serverCommand.length>4){
								
								for(int index = 4 ; index < serverCommand.length; index ++) {
									String command = serverCommand[index];
									if(command.contains("NumberOfConnections:")) {
										int colonIndex = command.indexOf(":");
										
										String numConnectionsString = command.substring(colonIndex + 1);
										numConn = Integer.parseInt(numConnectionsString);
									} else if (command.contains("url")) {
										
									   int equalIndex = command.indexOf("=");
										
										urlPath = command.substring(equalIndex + 1);
										
									} else if(command.contains("keepalive")) {
										keepAlive = true;
									}
								}
								
							}
							else
								numConn=1;
							
							
							if(serverCommand.length>5) {
								
							}
							
							Iterator<SocketConnection> i = connectionList.iterator();
							int arg2Type;
							
							if (serverCommand[1].matches("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$") == true){
								arg2Type=1;
							}
							else if(serverCommand[1].equals("all") ){	arg2Type=3; }
							else {arg2Type=2;}

							String connectCommand = "connect "+ serverCommand[2] + " "+ serverCommand[3] + " "+numConn 
														+" " + keepAlive;
							
							if(urlPath != "") {
								connectCommand = connectCommand + " " + urlPath;
							}
							
							switch (arg2Type) {
							    case 1:  
								while (i.hasNext()) {
								   	SocketConnection currentSock = i.next(); 
									if(currentSock.targetHostIP.equals(serverCommand[1])){
									try{
									 PrintWriter   Output = new PrintWriter(new OutputStreamWriter(currentSock.targetSocket.getOutputStream()));
									Output.println(connectCommand);
  									Output.flush();
									}
									 catch (Exception e) {
									System.out.println("Error connecting "+ currentSock.targetHostIP+" " + currentSock.targetPortNumber);
									}
									}
									}
								     break;
							    case 2:  
								while (i.hasNext()) {
								   	SocketConnection currentSock = i.next(); 
									if(currentSock.targetAddressName.equals(serverCommand[1])){
									try{
									 PrintWriter   Output = new PrintWriter(new OutputStreamWriter(currentSock.targetSocket.getOutputStream()));
									Output.println(connectCommand);
  									Output.flush();
									}
									 catch (Exception e) {
									System.out.println("Error connecting  "+ currentSock.targetHostIP+" " + currentSock.targetPortNumber);
									}
									}
									}
								     break;
							    case 3: 
								while (i.hasNext()) {
							   	SocketConnection currentSock = i.next();
									try{
									 PrintWriter   Output = new PrintWriter(new OutputStreamWriter(currentSock.targetSocket.getOutputStream()));
									Output.println("connect "+ serverCommand[2] + " "+ serverCommand[3] + " "+numConn);
  									Output.flush();
									}
									 catch (Exception e) {
									System.out.println(connectCommand);
									}	
									}
								     break;
							}


			
						}
						else{
							System.out.println(" no  proper arguments for connect");
						}
					}
					}
					else if(serverCommand[0].equals("disconnect")){

						if(serverCommand.length<3) {
							System.out.println("expects minimum 2 arguments");
						}
						else{
						if(serverCommand[1] != null && serverCommand[2]!= null){    	
							int disPort=0;
							if(serverCommand.length>3){
								disPort= Integer.parseInt(serverCommand[3]);
							}

							Iterator<SocketConnection> i = connectionList.iterator();
							int arg2Type;
							
							if (serverCommand[1].matches("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$") == true){
								arg2Type=1; }
							else if(serverCommand[1].equals("all") ) {	arg2Type=3; }
							else {	arg2Type=2;}

							switch (arg2Type) {
							    case 1:  
								while (i.hasNext()) {
								   	SocketConnection currentSock = i.next(); 
									if(currentSock.targetHostIP.equals(serverCommand[1])){
									try{
									 PrintWriter   Output = new PrintWriter(new OutputStreamWriter(currentSock.targetSocket.getOutputStream()));
									Output.println("disconnect "+ serverCommand[2] + " "+ disPort);
  									Output.flush();
									}
									 catch (Exception e) {
									System.err.println("Error connecting "+ currentSock.targetHostIP+" " + currentSock.targetPortNumber);
									}
									}
									}
								     break;
							    case 2:  
								while (i.hasNext()) {
								   	SocketConnection currentSock = i.next(); 
									if(currentSock.targetAddressName.equals(serverCommand[1])){
									try{
									 PrintWriter   Output = new PrintWriter(new OutputStreamWriter(currentSock.targetSocket.getOutputStream()));
									Output.println("disconnect "+ serverCommand[2] + " "+ disPort);
  									Output.flush();
									}
									 catch (Exception e) {
									System.err.println("Error connecting "+ currentSock.targetHostIP+" " + currentSock.targetPortNumber);
									}
									}
									}
								     break;
							    case 3: 
								while (i.hasNext()) {
							   	SocketConnection currentSock = i.next();
									try{
									 PrintWriter   Output = new PrintWriter(new OutputStreamWriter(currentSock.targetSocket.getOutputStream()));
									Output.println("disconnect "+ serverCommand[2] + " "+ disPort);
  									Output.flush();
									}
									 catch (Exception e) {
									System.err.println("Error connecting"+ currentSock.targetHostIP+" " + currentSock.targetPortNumber);
									}	
									}
								     break;
							}


			
						}
						else{
							System.out.println(" proper arguments required  for disconnect");
						}
						}	
					}

			}
		 if(userCommand.equals("list")){

					for(int i=0;i<connectionList.size(); i++){
		  					System.out.println(connectionList.get(i).targetAddressName+" "+connectionList.get(i).targetHostIP + " "+ port + " "+ connectionList.get(i).registrationDate);

					}
			}

	    } 
		}

		catch (Exception e) {
	      	e.printStackTrace();
		System.exit(-1);	    
}

  }
	
}


class SocketThread extends Thread {
  int portnum;
  SocketConnection sockList;
  SocketThread(int port) {
    portnum = port;
  }
  public void run() {
	    try {
		   ServerSocket m_ServerSocket = new ServerSocket(portnum);
		    while (true) {
			      Socket clientSocket = m_ServerSocket.accept();
				    sockList=new SocketConnection();
				    sockList.targetHostIP=clientSocket.getInetAddress().getHostAddress();
				    sockList.targetAddressName=clientSocket.getInetAddress().getHostName();
				    sockList.targetPortNumber=clientSocket.getPort();
				    sockList.targetSocket=clientSocket;
				    MasterBot.connectionList.add(sockList);
		    }
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
  }
}


