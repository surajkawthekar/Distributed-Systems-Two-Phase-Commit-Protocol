		// Name:Suraj Madhav Kawthekar	
		// Net id: 1001514614

		import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;

		//References:- “Multi-Threaded Chat Application in Java | Set 1 (Client Side Programming).” GeeksforGeeks, 17 June 2017, www.geeksforgeeks.org/multi-threaded-chat-application-set-1/. code by Rishabh Mahrsee

		//References:- “Multi-Threaded Chat Application in Java | Set 2 (Client Side Programming).” GeeksforGeeks, 17 June 2017, www.geeksforgeeks.org/multi-threaded-chat-application-set-2/. code by Rishabh Mahrsee
		//This lab is built on the previous lab.So it includes all the references of the previous lab 1.

		//ClientHandler class
		class ClientHandler implements Runnable 
		{	
			//This is the code for declaring variables
			
			Scanner scan = new Scanner(System.in);
			private String name;
			final DataInputStream dis;
			final DataOutputStream dos;
			Socket sock;
			int i;
			long final_time;
			boolean isloggedin;
			String client="Client "+i;
			// This is the constructor
			public ClientHandler(Socket sock, String name,
					DataInputStream dis, DataOutputStream dos,int i) {
				this.dis = dis;
				this.dos = dos;
				this.name = name;
				this.sock = sock;
				this.i=i;
				this.final_time = final_time;
				this.isloggedin=true;
			}
			//This is the run method
			@Override
			public void run() {

				String received;
				long timestamp_fl = 0;
				try
				{
					////Reference: http://www.programfaqs.com/faq/how-to-append-text-to-an-existing-file-in-java/ 
					
					File filef1 = new File("D:\\softwares\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\workspace\\Chatbox\\log.txt");
					filef1.createNewFile();
				
				/* Reference http://www.jmarshall.com/easy/http/*/
				while (true) 
				{
						// Here we read the messages on dos and store them in received.-5
						received = dis.readUTF();
						System.out.println(received);
						
						//Here we are printing the message on the server
						
						TPCServer.text1.append(/*"\n"+User_post+"\n"+"client:"+client+"\n"+" User-Agent:"+agent+"\n" +*/"client:"+client + received+"\n"); 
						
						//The client will logout from the application
						
						if(received.equals("logout")){
							
							TPCServer.text1.append("Client"+ " " + i + " " + "has succesfully logged out from the Application");
							for(ClientHandler it:TPCServer.clientlist){
								it.dos.writeUTF(this.name+" has logged out");
							}
							this.isloggedin=false;
							TPCServer.clientlist.remove(this);
							break;
						}
						
						  
						// It will break the string into message and recipient part-2,3
						StringTokenizer st = new StringTokenizer(received, "#");
						int count = 1;
						
						/*The reference for this timer method is taken from my android project which I had done in
						  Fall 2017. The logic for this code resembles the logic for that code.
						  for the first message the duration will be 00:00. for the next message it will 
						  be the difference between the second message and the first message and so on.*/
						DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
						for (ClientHandler it : TPCServer.clientlist) {
							
					        
							int length=received.length();
					        
							it.dos.writeUTF(/*"\n"+ http+ 
									"Date:"+date+"\n"+ 
									"Content-Type:"+type+ 
									"Content-Length:"+length+*/"Client:"+ i+this.name+
									" : "+received+"\t");
					        }
	
						//Reference: http://www.programfaqs.com/faq/how-to-append-text-to-an-existing-file-in-java/ 
						String logfile="Client:" + i + " " + received+"\n";				
						Files.write(Paths.get("D:\\softwares\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\workspace\\TwoPhaseCommit\\serverbackup.txt"), logfile.getBytes(), StandardOpenOption.APPEND);
						
						
				}
				
				}catch (IOException e) {

					e.printStackTrace();
				}

				try
				{
				
					this.dis.close();
					this.dos.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		/* References
		 * 
1]The following are the citations I have used as reference in my code

1) https://stackoverflow.com/questions/15247752/gui-client-server-in-java

2)Multi-Threaded Chat Application in Java | Set 1 (Client Side Programming).” GeeksforGeeks, 17 June 2017, www.geeksforgeeks.org/multi-threaded-chat-application-set-1/. code by Rishabh Mahrsee

3)Multi-Threaded Chat Application in Java | Set 2 (Client Side Programming).” GeeksforGeeks, 17 June 2017, www.geeksforgeeks.org/multi-threaded-chat-application-set-2/. code by Rishabh Mahrsee

4)https://stackoverflow.com/questions/10274750/java-swing-setting-margins-on-textarea-with-line-border

5)Marshall, James. “HTTP Made Really Easy.” HTTP Made Really Easy, 10 Dec. 2012, www.jmarshall.com/easy/http/#http1.1s3.

6)https://stackoverflow.com/questions/13245753/implementing-a-timer-in-java

7)https://docs.oracle.com/javase/8/docs/technotes/guides/lang/Countdown.java

8)https://examples.javacodegeeks.com/desktop-java/awt/event/window-closing-event-handling/

9)https://stackoverflow.com/questions/9093448/do-something-when-the-close-button-is-clicked-on-a-jframe

10)https://stackoverflow.com/questions/15758685/how-to-write-logs-in-text-file-when-using-java-util-logging-logger

11)https://stats.idre.ucla.edu/stata/faq/how-can-i-extract-a-portion-of-a-string-variable-using-regular-expressions/

12)https://stackoverflow.com/questions/4662215/how-to-extract-a-substring-using-regex

13)https://www.geeksforgeeks.org/write-regular-expressions/

14)https://www.geeksforgeeks.org/different-ways-reading-text-file-ja

15)Steen, Marteen Van, and Andrew S Tannenbaum. “Distributed Systems.” Distributed System Edition 3, 2017 (Distributed System Edition 3 Book mentioned in lab manual-Pg 487 and 488 pseudo commit of 2PC) 
						Third edition Version 3.01 (2017) Maarten van Steen Andrew S. Tanenbaum

		 * */

