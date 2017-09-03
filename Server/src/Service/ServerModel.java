package Service;

import java.net.*;
import java.io.*;
import java.awt.event.*;
import java.awt.*;

public class ServerModel implements ActionListener{
	//a reference of view
	public ServerView view;

	//connection variables
	public ServerSocket serverSocket;
	public Socket clientSocket;
	public PrintWriter out;
	public BufferedReader in;
	public String inputLine, outputLine;

	//server status
	public boolean serverCreated;
	public boolean clientConnected;
	public boolean gameStarted;
	public boolean gamePaused;
	public boolean gameOver;
	public boolean serverVoteYes, serverVoteNo;
	public boolean clientVoteYes, clientVoteNo;
	public boolean pausePressed;

	//game message
	public String[] messageQueue;
	public int messageIndex;
	public String playerTypedMessage = "";

	//the actual game is running on this thread, while the main thread listen to user's input
	public Ticker t;

	//textures
	public Image[] textures;

	//game vaiables
	public static int gameFlow;
	public Actor[] actors;
	public player P1;   //the tank controled by the server player
	public player P2;   //the tank controled by the client player

	public ServerModel(ServerView thisview){

		view = thisview;
		messageQueue = new String[8];
		view.mainPanel.messageQueue = messageQueue;

		addMessage("欢迎来到坦克大战主机端!  请点击\"建立主机\"按钮开始游戏" );

		t = new Ticker(1000);
		t.addActionListener(this);

	}


	public void createServer(){

 		addMessage("正在建立主机(端口4321)");

 		try {
                    serverSocket = new ServerSocket(4321);
                    serverCreated = true;
                } catch (Exception e) {
                    addMessage("无法建立主机，请确认端口4321没有被别的程序使用");
                    System.out.println(e);
                    t.stop();
                    return;
                }
		addMessage("建立完成，等待玩家连接");

        try {
		   clientSocket = serverSocket.accept();
	       clientConnected = true;

	    	out = new PrintWriter(clientSocket.getOutputStream(), true);
		    in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));

	    } catch (Exception e) {
			addMessage("连接中出现错误，请重新建立主机");
			serverCreated = false;
			clientConnected = false;
			t.stop();

			//when something goes wrong, destory evertthing that has been created
			try{
				serverSocket.close();
				clientSocket.close();
				out.close();
				in.close();
			}catch(Exception ex){}

			return;
        }

		view.messageField.setEnabled(true);
		addMessage("玩家已连接上，开始载入游戏");

		//once the clinet is connected, then tell client computer to start loading the game
		 out.println("L1;");

		//load game texture
		textures = new Image[88];
		for(int i = 1; i < textures.length+1; i++)
			textures[i-1] = Toolkit.getDefaultToolkit().getImage("image\\" + i + ".jpg");


		//setup the first level
		actors = new Actor[400];
		level.loadLevel(this);

		P1 = new player("1P", this);
		addActor(P1);
		P2 = new player("2P", this);
		addActor(P2);



		gameStarted = true;
		view.mainPanel.actors = actors;
		view.mainPanel.gameStarted = true;

	addMessage("载入完毕，游戏开始了！");
	}

	public void actionPerformed(ActionEvent e){
		createServer();

		//if the program fail to create server then do nothing
		if(!serverCreated)
			return;

		//the game logic loop,
		try{
			while((inputLine = in.readLine()) != null){
				//process feedback message from the client
				feedbackHandler.handleInstruction(this, inputLine);

				outputLine = "";

				if(!gamePaused)
					gameFlow++;

				if(pausePressed){
					if(!gamePaused){
						outputLine+= "x0;";
					}else{
						outputLine+= "x1;";
					}
					pausePressed = false;
				}

				if(gameOver || (P1.life == 0 && P2.life == 0)){
					if(P1.freezed != 1)
						outputLine+="a;";


					if((P1.freezed != 1 || messageIndex == 1) && serverVoteYes){
						addMessage("等待用户端玩家的回应...");
					}
					if(P1.freezed != 1 || messageIndex == 0){
							addMessage("GAME OVER ! 　想再玩一次吗 ( y / n ) ?");
					}
					gameOver =  true;
					P1.freezed = 1;
					P2.freezed = 1;

					if(serverVoteNo && !serverVoteYes)
						System.exit(0);

					if(serverVoteYes){
						outputLine+="j;";
						if(clientVoteYes){
							addMessage("用户端玩家决定再玩一次，游戏重新开始了...");

							//restart game
							P1 = new player("1P", this);
							P2 = new player("2P", this);
							level.reset();
							level.loadLevel(this);
							gameOver = false;
							serverVoteYes = false;
							clientVoteYes = false;
							serverVoteNo = false;
							enemy.freezedMoment = 0;
							enemy.freezedTime = 0;
							gameFlow = 0;

							//tell the client program to restart the game
							outputLine+="L1;";
						}
					}
				}

				if(level.deathCount == 20 &&  !gameOver){
					level.winningCount++;
					if(level.winningCount == 120){
						P1.freezed = 1;
						P2.freezed = 1;
					}
					if(level.winningCount == 470){
						if(P1.life > 0)
							P1.reset();
						if(P2.life > 0)
							P2.reset();
						level.loadLevel(this);
						//tell the client program to load the next level
						outputLine+="L" +(1 +  (level.currentLevel-1)%8) + ";";
					}
					if(level.winningCount  == 500){
						P1.freezed = 0;
						P2.freezed = 0;
						level.deathCount = 0;
						level.winningCount = 0;
					}

				}

				//spawn enemy tanks
				if(!gamePaused)
					level.spawnEnemy(this);

				for(int i = 0; i < actors.length; i++){
					if(actors[i] != null)
						actors[i].move();
				}

				//delete one message from the message queue every 10 secs, (if there is any)
				if(gameFlow%300 == 0)
					removeMessage();

				//write player and level  information  to the outputLine
				outputLine+="p" + level.enemyLeft + "," + level.currentLevel + "," + P1.life + "," + P1.scores + "," +  P2.life + "," + P2.scores +";";
				outputLine+="g" + level.winningCount + ";";

				//wtire player typed message to the outputLine
				if(!playerTypedMessage.equals("")){
					outputLine+=playerTypedMessage;
					playerTypedMessage = "";
				}

				//send the final instruction-string to the client program
				out.println(outputLine);

				//call view to repaint itself
				view.mainPanel.repaint();

				//if the player switch to dialoge mode, then stop all tank actions
				if(!view.mainPanel.hasFocus()){
					P1.moveLeft = false;
					P1.moveUp = false;
					P1.moveDown = false;
					P1.moveRight = false;
					P1.fire = false;
				}

				Thread.sleep(30);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			view.messageField.setEnabled(false);
			serverVoteYes= false;
			serverVoteNo = false;
			clientVoteYes = false;
			serverCreated = false;
			gameStarted = false;
			gameOver = false;
			gameFlow = 0;
			enemy.freezedTime = 0;
			enemy.freezedMoment = 0;
			view.mainPanel.gameStarted = false;
			t.stop();
			addMessage("玩家退出了，请重新建立主机");

			//when something goes wrong in the middle of the game, destory any thing that has been created, include game variables
			try{
				out.close();
				in.close();
				clientSocket.close();
                                serverSocket.close();
			}catch(Exception exc){}

			//destory game data
			P1 = null;
			P2 = null;
			level.reset();
		}
	}

	//add a game object (eg, tanks, bullet etc...) to the game system
	public void addActor(Actor actor){
		for(int i = 0; i < actors.length; i ++ )
			if(actors[i] == null){
				actors[i] = actor;
				break;
			}
	}

	//remove a game object from the game system
	public void removeActor(Actor actor){
			for(int i = 0; i < actors.length; i ++ )
					if(actors[i] == actor){
						actors[i] = null;
						break;
			}
	}


	//display a line of message on the screen
	public void addMessage(String message){
		if(messageIndex < 8){
			messageQueue[messageIndex] = message;
			messageIndex++;
		}
		else{
			for(int  i = 0; i < 7; i++)
				messageQueue[i] = messageQueue[i+1];
			messageQueue[7] = message;
		}

		//call view to repaint the screen if game has't started
		if(!gameStarted)
			view.mainPanel.repaint();
	}

	//remove the earliest message on the screen
	public void removeMessage(){
		if(messageIndex == 0)
			return;

		messageIndex--;
		for(int  i = 0; i < messageIndex; i++)
			messageQueue[i] = messageQueue[i+1];
		messageQueue[messageIndex] = null;

		//call view to repaint the screen if game hasn't started
		if(!gameStarted)
			view.mainPanel.repaint();
	}

}