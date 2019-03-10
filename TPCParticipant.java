	
					// Name:Suraj Madhav Kawthekar	
					// Net id: 1001514614

		import java.awt.event.ActionEvent;
import java.awt.Color;

import javax.swing.BorderFactory;

import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JTextArea;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.border.Border;

import java.io.DataInputStream;

import javax.swing.JLabel;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.io.IOException;

		//References:- “Multi-Threaded Chat Application in Java | Set 1 (Client Side Programming).” GeeksforGeeks, 17 June 2017, www.geeksforgeeks.org/multi-threaded-chat-application-set-1/. code by Rishabh Mahrsee
		/*References:- Steen, Marteen Van, and Andrew S Tannenbaum. “Distributed Systems.” Distributed System Edition 3, 2017 (Distributed System Edition 3 Book mentioned in lab manual-Pg 487 and 488 pseudo commit of 2PC) 
			Third edition Version 3.01 (2017) Maarten van Steen Andrew S. Tanenbaum*/

		//References:- “Multi-Threaded Chat Application in Java | Set 2 (Client Side Programming).” GeeksforGeeks, 17 June 2017, www.geeksforgeeks.org/multi-threaded-chat-application-set-2/. code by Rishabh Mahrsee
		//This lab is built on the previous lab.So it includes all the references of the previous lab 1.
		public class TPCParticipant extends JFrame implements ActionListener
		{	
			//This is the code for declarations
			final static int ServerPort = 8888;
			//Declarations for variables
			static Socket sock;
			static JTextArea WriteMessage;
			static JTextArea DisplayMessage;
			int counter=0,res;
			static String s;
			static String txtmsg;
			public static String word;
			// DataInputStream will receive the messages from other Clients.
			// DataOutputStream will post the messages to other Clients.
			DataInputStream ClientIncoming;
			DataOutputStream ClientOutgoing;
			
			JButton button1;
			JButton button2;
			static String msg="";
			
			//This is the Constructor
			public TPCParticipant(){												
				
				//This is the Code for User Interface
				this.setTitle("Participant");																								
				this.setSize(1366, 768);																								
				this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);																			
				getContentPane().setLayout(null);									
				
				
				
				//Textbox to Display the incoming and outgoing messages
				
				JLabel label1 = new JLabel("===========TPC Participant===========",JLabel.CENTER);
				label1.setBounds(50, 15, 1250, 40);
				
				DisplayMessage = new JTextArea();																						
				DisplayMessage.setBounds(50, 50, 1250, 600);
				DisplayMessage.setEditable(false);
				add(DisplayMessage);
				
				//Button to send the message
				button1 = new JButton("COMMIT");		
				button1.setBounds(550, 680, 130, 25);
				button1.addActionListener(this);
				
				//Button to send the message
				button2 = new JButton("ABORT");		
				button2.setBounds(750, 680, 130, 25);
				button2.addActionListener(this);
				
		/*Reference https://stackoverflow.com/questions/10274750/java-swing-setting-margins-on-textarea-with-line-border*/
				
				//This is the Code to add border to the textboxes
				Border bordertop = BorderFactory.createLineBorder(Color.BLACK);		
				DisplayMessage.setBorder(BorderFactory.createCompoundBorder(bordertop, 
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
				
				
				
				//This is the code to append Gui component to frame and make it visible
				add(button1);
				add(button2);
				add(label1);
				this.setVisible(true);		
				//https://www.geeksforgeeks.org/different-ways-reading-text-file-ja
				//It will read the textfile and load it into the participant display area.
try{
				File fl = new File("D:\\softwares\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\workspace\\TwoPhaseCommit\\"+"participant"+s+".txt");
                if (fl.exists()) {
                    BufferedReader br = new BufferedReader(new FileReader(fl));
                    String st = "";
                
                    while ((st = br.readLine()) != null) {

                    	DisplayMessage.append(st + "\n");
                    }
                }
}catch(Exception e){
	e.printStackTrace();
}
			}
			
			
			
			
			// When the user will click the send message button Send message function will be called else it will 
			//give an exception.-2,3
			/*public void actionPerformed1(ActionEvent ae) {
				if (ae.getSource().equals(button1)) {																					
					try {
							SendMessage();			
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}*/
			 //Here we encode the http messages and store it in a file
		     //and pass with the DataoutputStream to the server and the participants
			String agent="HTTPTool/1.1 \\n";
			Date d= new Date();
			String date=d.toString();
			String type="text/html\r\n";
			int length=msg.length();
			String Server_host="http://localhost:8888";
			
			public String encode = "\n" + "Host:" + Server_host+ "\n" +" User-Agent:"+agent+"\n" + "Content-Type:"+type+ "Content-Length:"+length+ "\n" +
					"Date:"+date+"\n";
			
			//This function will read the message from the clients and write the message on server-2,3
			public void SendMessage()  throws UnknownHostException, IOException {
				DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
				String word = "VOTE_COMMIT";																					   // get the word to search entered by user
				dos.writeUTF(encode+word);	
				
				// This block configures the logger with handler and formatter and will
		    	//generate a log file and store the participant states in it
				//[10]
				Logger logger = Logger.getLogger("TPCParticipant");  
			    FileHandler fh;  
			    String log2= "READY";
			    try {  ;

			        // This block configure the logger with handler and formatter  
			        fh = new FileHandler("D:\\softwares\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\workspace\\TwoPhaseCommit\\log_dump.txt");  
			        logger.addHandler(fh);
			        SimpleFormatter formatter = new SimpleFormatter();  
			        fh.setFormatter(formatter);  

			        // the following statement is used to log any messages  
			        logger.info("READY");  

			    } catch (SecurityException e) {  
			        e.printStackTrace();  
			    } catch (IOException e) {  
			        e.printStackTrace();  
			    }
			}
			
			// When the user will click the send message button Send message function will be called else it will 
			//give an exception.-2,3-
			public void actionPerformed(ActionEvent ae) {																				
				if (ae.getSource().equals(button2)) {																					
					try {
								SendMessage2();		
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				else if (ae.getSource().equals(button1)) {																					
					try {
							SendMessage();			
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			//This function will read the message from the clients and write the message on server-2,3
			public void SendMessage2()  throws UnknownHostException, IOException {
				DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
				word = "VOTE_ABORT";																					   // get the word to search entered by user
				dos.writeUTF(encode+word);
				
			}
		     //code to read and append the message- 2,3
			public static void main(String args[]) throws UnknownHostException, IOException 
			{
				new TPCParticipant();
				try{
				InetAddress ip = InetAddress.getByName("localhost");
				sock = new Socket(ip, ServerPort);
				while(true) {
					
					// This block configures the logger with handler and formatter and will
			    	//generate a log file and store the participant states in it
    				//[10]
					Logger logger = Logger.getLogger("TPCParticipant");  
    			    FileHandler fh;  
    			    String log_dump= "INIT";
    			    try {  ;

    			        // This block configure the logger with handler and formatter  
    			        fh = new FileHandler("D:\\softwares\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\workspace\\TwoPhaseCommit\\log_dump.txt");  
    			        logger.addHandler(fh);
    			        SimpleFormatter formatter = new SimpleFormatter();  
    			        fh.setFormatter(formatter);  

    			        // the following statement is used to log any messages  
    			        logger.info("INIT");  

    			    } catch (SecurityException e) {  
    			        e.printStackTrace();  
    			    } catch (IOException e) {  
    			        e.printStackTrace();  
    			    }
    			    
    			  //[11,12,13,14]
    			    //This is the code for pattern matching. It will match the patter of the string passed by the coordinator and will extract the participant name and 
    			    // the desired string and will write it to the respective text files of the participant.
					DataInputStream dis = new DataInputStream(sock.getInputStream());
				    msg = dis.readUTF();
				    s ="";
					Pattern pattern = Pattern.compile(".*Client:[0-9]*");
					Matcher matcher = pattern.matcher(msg);
					if (matcher.find())
					{
					   s=matcher.group();
					}
					s=s.replace("Client:","");
					System.out.println("s:" +s );
					DisplayMessage.append(msg);	
			
					
						if(msg.contains("VOTE_REQUEST")){
							
							//[11,12,13,14]
							//This is the code for pattern matching. It will match the patter of the string passed by the coordinator and will extract the participant name and 
		    			    // the desired string and will write it to the respective text files of the participant.
							txtmsg ="";
							Pattern pattern1 = Pattern.compile(".*Message:[a-zA-Z]*");
							Matcher matcher1 = pattern1.matcher(msg);
							if (matcher1.find())
							{
							   txtmsg=matcher1.group();
							}
							txtmsg=txtmsg.replace("Message:","");
							System.out.println("TextMessage :" + txtmsg);
						
							//This will initiate the timer and the timer will run for a duration of 30 seconds.
							// The participants must receive response from coordinator within time else it will send a Need_decision message.
							//[6,7]
						final Timer timer1 = new Timer();
						TimerTask timerTask = new TimerTask() {
						//String message;
						int res=30;
						DataInputStream dis= new DataInputStream(sock.getInputStream());
							
				            @Override
				            public void run() {
				            	System.out.println(res);
				            	res--;
				            	
				                
				                	try
				                	{
				                		DataOutputStream dos= new DataOutputStream(sock.getOutputStream());
				                	    
				                	
				                	if( msg.contains("GLOBAL_ABORT")){
				                		// This block configures the logger with handler and formatter and will
				    			    	//generate a log file and store the participant states in it
				        				//[10]
				                		
				                		Logger logger = Logger.getLogger("TPCParticipant");  
				        			    FileHandler fh;  
				        			    String log_dump= "ABORT";
				        			    try {  ;

				        			        // This block configure the logger with handler and formatter  
				        			        fh = new FileHandler("D:\\softwares\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\workspace\\TwoPhaseCommit\\log_dump.txt");  
				        			        logger.addHandler(fh);
				        			        SimpleFormatter formatter = new SimpleFormatter();  
				        			        fh.setFormatter(formatter);  

				        			        // the following statement is used to log any messages  
				        			        logger.info("ABORT");  

				        			    } catch (SecurityException e) {  
				        			        e.printStackTrace();  
				        			    } catch (IOException e) {  
				        			        e.printStackTrace();  
				        			    }
				        				String Participant_backup= "GLOBAL_ABORT";
				        				
				        				//DataOutputStream dos= new DataOutputStream(sock.getOutputStream());
				        				dos.writeUTF(Participant_backup);
				        				timer1.cancel();
				        				
				                		}
				                	
				                		else if(msg.contains("GLOBAL_COMMIT")){
				                			//[10]
				                			File filef3 = new File("D:\\softwares\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\workspace\\TwoPhaseCommit\\Participant"+s+"_backup.txt");
					        				filef3.createNewFile();
					        			
					        				// This block configures the logger with handler and formatter and will
					    			    	//generate a log file and store the participant states in it
					        				//[10]
					        				Logger logger = Logger.getLogger("TPCParticipant");  
					        			    FileHandler fh;  
					        			    String log2= "COMMIT";
					        			    try {  ;

					        			        // This block configure the logger with handler and formatter  
					        			        fh = new FileHandler("D:\\softwares\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\workspace\\TwoPhaseCommit\\log_dump.txt");  
					        			        logger.addHandler(fh);
					        			        SimpleFormatter formatter = new SimpleFormatter();  
					        			        fh.setFormatter(formatter);  

					        			        // the following statement is used to log any messages  
					        			        logger.info("COMMIT");  

					        			    } catch (SecurityException e) {  
					        			        e.printStackTrace();  
					        			    } catch (IOException e) {  
					        			        e.printStackTrace();  
					        			    }
					        				
					        				String Participant_backup= "GLOBAL_COMMIT";
					        				//[10]
					        				Files.write(Paths.get("D:\\softwares\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\workspace\\TwoPhaseCommit\\Participant"+s+"_backup.txt"), txtmsg.getBytes(), StandardOpenOption.APPEND);
					                		
					        				//DataOutputStream dos= new DataOutputStream(sock.getOutputStream());
					        				dos.writeUTF(Participant_backup);
					        				timer1.cancel();
					        				
				                		}
				                	//When timer ends the participants will send a Need_Decision message
				                	//and the different condition will be handled and he response
				                	//will be recorded accordingly.[6,7]
				                	if(res<0)
				                		
				                	{
				                		//DataOutputStream dos= new DataOutputStream(sock.getOutputStream());
				                		//System.out.println("In Run before " + msg);
				                		dos.writeUTF("NEED_DECISION");
										//System.out.println("In Run after "+ msg);
				                		
				                		
										//DataInputStream dis= new DataInputStream(sock.getInputStream());
										//If message contains Need_Decision write Local_Abort
										if(msg.contains("NEED_DECISION")){
											
					        				String Participant_backup= "LOCAL_ABORT";
					        				
					                		msg="";
											//DataOutputStream dos4= new DataOutputStream(sock.getOutputStream());
											dos.writeUTF(Participant_backup);
											timer1.cancel();
										}
										else if(msg.contains("GLOBAL_ABORT")){
											//If message contains Global_Abort write Global_Abort
					        			msg="";
					        				String Participant_backup= "GLOBAL_ABORT";
					        				
											//DataOutputStream dos2= new DataOutputStream(sock.getOutputStream());
											dos.writeUTF("GLOBAL_ABORT");
											timer1.cancel();
										}
										//if msg contains Global_Abort write Golbal_Abort
										else if(msg.contains("GLOBAL_COMMIT")){
											//[10]
											File filef3 = new File("D:\\softwares\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\workspace\\TwoPhaseCommit\\Participant"+s+"_backup.txt");
					        				filef3.createNewFile();
					        			    System.out.println(s);
					        				String Participant_backup= "GLOBAL_COMMIT";
					        				Files.write(Paths.get("D:\\softwares\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\workspace\\TwoPhaseCommit\\Participant"+s+"_backup.txt"), txtmsg.getBytes(), StandardOpenOption.APPEND);
					                		msg="";
											//DataOutputStream dos3= new DataOutputStream(sock.getOutputStream());
											dos.writeUTF("COMMIT");
											timer1.cancel();
											
										}
										//if msg contains Local_Abort the wite Local_Abort
										else if(msg.contains("LOCAL_ABORT")){
											
					        			msg="";
					        				String Participant_backup= "LOCAL_ABORT";
					        				
											//DataOutputStream dos2= new DataOutputStream(sock.getOutputStream());
											dos.writeUTF("LOCAL_ABORT");
											timer1.cancel();
										}
										
										
				                	}
				                		
				                	}
				                	
				                		catch(Exception ex){
				                		ex.printStackTrace();
				                		}
				                	
				                }
				            
				        };
						

				        timer1.scheduleAtFixedRate(timerTask, 0, 1000);
				        }
				}
				
			
		}
			catch(Exception ex){
        		ex.printStackTrace();
        		}
				finally{}
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

15) Steen, Marteen Van, and Andrew S Tannenbaum. “Distributed Systems.” Distributed System Edition 3, 2017 (Distributed System Edition 3 Book mentioned in lab manual-Pg 487 and 488 pseudo commit of 2PC) 
						Third edition Version 3.01 (2017) Maarten van Steen Andrew S. Tanenbaum


		 * */
