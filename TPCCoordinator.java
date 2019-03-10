	
					// Name:Suraj Madhav Kawthekar	
					// Net id: 1001514614

import java.awt.event.ActionEvent;
import java.awt.Color;

import javax.swing.BorderFactory;

import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.io.DataOutputStream;
import java.io.File;
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

		//References:- “Multi-Threaded Chat Application in Java | Set 2 (Client Side Programming).” GeeksforGeeks, 17 June 2017, www.geeksforgeeks.org/multi-threaded-chat-application-set-2/. code by Rishabh Mahrsee
		
		/*References:- Steen, Marteen Van, and Andrew S Tannenbaum. “Distributed Systems.” Distributed System Edition 3, 2017 (Distributed System Edition 3 Book mentioned in lab manual-Pg 487 and 488 pseudo commit of 2PC) 
						Third edition Version 3.01 (2017) Maarten van Steen Andrew S. Tanenbaum*/


		//This lab is built on the previous lab.So it includes all the references of the previous lab 1.
		public class TPCCoordinator extends JFrame implements ActionListener
		{
			//This is the code for declaring the variables
			final static int ServerPort = 8888;
			//Declarations for variables
			static Socket sock;
			private int watch=30;
			static JTextArea WriteMessage;
			static JTextArea DisplayMessage;
			int counter=0,res;
			// DataInputStream will receive the messages from other Clients.
			// DataOutputStream will post the messages to other Clients.
			static DataInputStream ClientIncoming;
			static DataInputStream dis;
			DataOutputStream ClientOutgoing;
			int size=TPCServer.clientlist.size();
			static String msg="";
			JButton button1;
			static String babloo;
			//This is the Constructor
			public TPCCoordinator(){												
				
				//This is the Code for User Interface
				this.setTitle("Coordinator");																								
				this.setSize(1366, 768);																								
				this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);																			
				getContentPane().setLayout(null);							

				
				//This is the Textbox to type your message
				JLabel label2 = new JLabel("===========Type your text Below===========",JLabel.CENTER);
				label2.setBounds(50, 505, 1250, 20);		
				
				//This is the textbox where we will write the text before sending a vote commit.
				WriteMessage = new JTextArea();																						
				WriteMessage.setBounds(50, 525, 1250, 110);
				add(WriteMessage);
				
				
				//This is the Textbox to Display the incoming and outgoing messages
				
				JLabel label1 = new JLabel("===========TwoPhaseCoordinator===========",JLabel.CENTER);
				label1.setBounds(50, 15, 1250, 40);
				
				DisplayMessage = new JTextArea();																						
				DisplayMessage.setBounds(50, 50, 1250, 450);
				DisplayMessage.setEditable(false);
				
				add(DisplayMessage);
				
				
				//This is the Button to send a vote request message
				button1 = new JButton("Request Vote");		
				button1.setBounds(625, 650, 130, 25);
				button1.addActionListener(this);
				
		/*Reference https://stackoverflow.com/questions/10274750/java-swing-setting-margins-on-textarea-with-line-border*/
				
				//Code to add border to the textboxes
				Border bordertop = BorderFactory.createLineBorder(Color.BLACK);		
				DisplayMessage.setBorder(BorderFactory.createCompoundBorder(bordertop, 
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
				
				Border borderbottom = BorderFactory.createLineBorder(Color.BLACK);		
				WriteMessage.setBorder(BorderFactory.createCompoundBorder(borderbottom, 
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
				
				//Code to append Gui component to frame and make it visible
				add(button1);
				add(label1);
				add(label2);
				this.setVisible(true);		
				
				

			}
			

			@Override
			// When the user will click the REQUEST_VOTE button SendMessage2 function will be called else it will 
			//give an exception.-[2,3]
			public void actionPerformed(ActionEvent ae) {																				
				if (ae.getSource().equals(button1)) {																					
					try {
						SendMessage2();				
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
			
			//This function will read the message from the participants and write the message on server-[2,3]
			public void SendMessage2()  throws UnknownHostException, IOException {	
				File filef2 = new File("D:\\softwares\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\workspace\\TwoPhaseCommit\\co-log.txt");
				filef2.createNewFile();			
				
				// Reference:-https://stackoverflow.com/questions/15758685/how-to-write-logs-in-text-file-when-using-java-util-logging-logger
				//It will generate a log file and store the various coordinator states in it
				//[10]
				Logger logger = Logger.getLogger("MyLog");  
			    FileHandler fh;  

			    try {  

			        // This block configures the logger with handler and formatter and will
			    	//generate a log file and store the participant states in it
			        fh = new FileHandler("D:\\softwares\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\workspace\\TwoPhaseCommit\\coor_log.txt");  
			        logger.addHandler(fh);
			        SimpleFormatter formatter = new SimpleFormatter();  
			        fh.setFormatter(formatter);  

			        // the following statement is used to log any messages  and the coordinator will wait for the participants response.
			        logger.info("WAIT");  

			    } catch (SecurityException e) {  
			        e.printStackTrace();  
			    } catch (IOException e) {  
			        e.printStackTrace();  
			    }  

		
			    //Here we encode the http messages and store it in a file
			     //and pass with the DataoutputStream to the server and the participants
			    //[5]
				String agent="HTTPTool/1.1 \\n";
				Date d= new Date();
				String date=d.toString();
				String type="text/html\r\n";
				int length=msg.length();
				String Server_host="http://localhost:8888";
				String encode="\n" + "Host:" + Server_host+ "\n" +" User-Agent:"+agent+"\n" + "Content-Type:"+type+ "Content-Length:"+length+ "\n" +
						"Date:"+date+"\n";
				
				// If the message is not null the coordinator will send the string to the participants
				// and request for a vote to commit or abort the string
				if(!WriteMessage.getText().equals("")){
					
					babloo= WriteMessage.getText();
				DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
				dos.writeUTF(encode+ "Message:"+
						 WriteMessage.getText() + "\n" + "VOTE_REQUEST");
				WriteMessage.setText("");
				
				//This will initiate the timer and the timer will run for a duration of 30 seconds.
				// The coordinator must receive response from participants within time else it will send a Global_Abort.
				//[6,7]
				final Timer timer1 = new Timer();
				TimerTask timerTask = new TimerTask() {
				//String message;
				int res=30;
					
					
		            @Override
		            public void run() {
		            	System.out.println(res);
		            	res--;
		            	
		                
		                	try
		                	{
		                	//DataInputStream dis = new DataInputStream(sock.getInputStream());
		                	// String  message = dis.readUTF();
		                	 //[10]   
		                	// if the message contains vote abort the transaction will be aborted
		                	if( msg.contains("VOTE_ABORT")){
		                		
		                		File filef2 = new File("D:\\softwares\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\workspace\\TwoPhaseCommit\\co-log.txt");
		        				filef2.createNewFile();
		        				
		        				// This block configures the logger with handler and formatter and will
		    			    	//generate a log file and store the participant states in it
		        				//[10]
		        				Logger logger = Logger.getLogger("MyLog");  
		        			    FileHandler fh;  
		        			    String log2= "GLOBAL_ABORT";
		        			    try {  ;

		        			        // This block configure the logger with handler and formatter  
		        			        fh = new FileHandler("D:\\softwares\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\workspace\\TwoPhaseCommit\\coor_log.txt");  
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
		        			    //It will display the message to the console
		        			    DataOutputStream dos= new DataOutputStream(sock.getOutputStream());
		        				dos.writeUTF(log2);
		        				timer1.cancel();
		                		}
		                	
		                	// if the message contains vote commit the transaction will be committed it will increment the counter and one the counter reaches 3 the 
		                	//transaction will be Globally_committed.
		                	
		                		else if(msg.contains("VOTE_COMMIT")){
		                			counter = counter+1;
		                			msg="";
		                		}
		                		if(counter==3){
		                			File filef2 = new File("D:\\softwares\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\workspace\\TwoPhaseCommit\\co-log.txt");
			        				filef2.createNewFile();
			        			
			        				// This block configures the logger with handler and formatter and will
			    			    	//generate a log file and store the participant states in it
			        				//[10]
			        				Logger logger = Logger.getLogger("MyLog");  
			        			    FileHandler fh;  
			        			    String log2= "GLOBAL_COMMIT";
			        			    try {  ;

			        			        // This block configure the logger with handler and formatter  
			        			        fh = new FileHandler("D:\\softwares\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\workspace\\TwoPhaseCommit\\Logfile0.txt");  
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
			        			    
			        			    
			        			    
			        			    //It will create a text file for the coordinator and will print the string once it is globally committed.
			        				//[10]
			        			    Files.write(Paths.get("D:\\softwares\\eclipse-jee-kepler-SR2-win32-x86_64\\eclipse\\workspace\\TwoPhaseCommit\\co-log.txt"), babloo.getBytes(), StandardOpenOption.APPEND);
			                		
			        				DataOutputStream dos= new DataOutputStream(sock.getOutputStream());
			        				dos.writeUTF(log2);
			        				timer1.cancel();
		                		}
		                		
		                	}
		                	
		                		catch(Exception ex){
		                		ex.printStackTrace();
		                		}
		                	
		                		//If the timer times out print global abort and send it to all the participants
		                	if(res<0)
		                		
		                	{
		                		
		        				try {
		        					DataOutputStream dos= new DataOutputStream(sock.getOutputStream());
									dos.writeUTF("GLOBAL_ABORT");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		                		timer1.cancel();
		                	}
		                	
		                	
		                	
		                	
		                }
		            
		        };
		     

		        timer1.scheduleAtFixedRate(timerTask, 0, 1000);
				
			}}
			
			
		     //code to read and append the message- 2,3
			public static void main(String args[]) throws UnknownHostException, IOException 
			{
				//new TPCCoordinator();
				//This is the coordinator logout event. the coordinator will logout once we close the window.
				//[8,9]
				final TPCCoordinator jFrame = new TPCCoordinator();
				jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
				    @Override
				    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				    	
				    	try {
				    		DataOutputStream dos= new DataOutputStream(sock.getOutputStream());
							dos.writeUTF("logout");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }
				});
				//It will read the input stream and will store it in the msg variable
				InetAddress ip = InetAddress.getByName("localhost");
				sock = new Socket(ip, ServerPort);
				dis = new DataInputStream(sock.getInputStream());
				while(true) {
					
					msg = dis.readUTF();
					DisplayMessage.append(msg);
					
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

15.	Steen, Marteen Van, and Andrew S Tannenbaum. “Distributed Systems.” Distributed System Edition 3, 2017 (Distributed System Edition 3 Book mentioned in lab manual-Pg 487 and 488 pseudo commit of 2PC) 
Third edition
Version 3.01 (2017)
Maarten van Steen
Andrew S. Tanenbaum
		 * */
