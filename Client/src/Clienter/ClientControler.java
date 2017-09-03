package Clienter;

import javax.swing.*;
import java.awt.event.*;

//this class deal with  the input from the Client view frame
public class ClientControler{
	public boolean serverConnected;;
	public boolean gameStarted;
	public boolean gamePaused;
	public ClientView view;
	public ClientModel model;
	public int helpMessageCount = 1;


	public ClientControler(ClientView thisview, ClientModel thismodel){
		view = thisview;
		model = thismodel;

		//handel sendMessage button actions
		view.sendMessage.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if(!model.gameStarted){
						model.addMessage("还没有和主机端玩家联上, 无法发送对话");
						return;
					}

					if(!view.messageField.getText().equals("")){
						model.addMessage("用户端玩家说：" + view.messageField.getText());
						model.playerTypedMessage += "e" + view.messageField.getText() + ";";
						view.messageField.setText("");
					}else{
						model.addMessage("对话内容不能为空");
					}
				}
			}
		);

		//handel connectServer button actions
		view.connectServer.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if(!model.serverConnected){
						model.serverIP = view.IPfield.getText();
						model.t.start();
					}
				}
			}
		);

		//handel pauseAndResume button actions
		view.pauseAndResume.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if(!model.gameOver && model.gameStarted){
						model.pausePressed = true;
						if(!model.gamePaused){
							model.gamePaused = true;
							model.addMessage("用户端玩家暂停了游戏");
						}else{
							model.gamePaused = false;
							model.addMessage("用户端玩家取消了暂停");
						}
					}
				}
			}
		);

		//handel help button actions
		view.help.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					model.addMessage("-------------------------------坦克大战 1.0--------------------------------------------");
					model.addMessage("帮助: 按 s 键开火,  按键盘的方向键来控制坦克的移动");
					model.addMessage("如果按键没有反应请 1. 关闭大写功能; 2. 用 tab键切换 ");
					model.addMessage("到控制界面如果您在使用对话界面.");
					model.addMessage("--------------------------------------------------------------------------------------");
				}
			}
		);

		//handel exit button actions
		view.exit.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			}
		);


		//handel input from the keyboard
		view.messageField.addKeyListener( new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(helpMessageCount  > 0){
					model.addMessage("提示：用\"tab\"键可以自由切换于控制界面和对话界面");
					model.addMessage("提示：按回车键可以直接发送您的对话");
					helpMessageCount--;
				}

				if(e.getKeyCode()==e.VK_ENTER){
					if(!view.messageField.getText().equals("")){
						model.addMessage("用户端玩家说：" + view.messageField.getText());
						model.playerTypedMessage += "e" + view.messageField.getText() + ";";
						view.messageField.setText("");
					}else{
						model.addMessage("对话内容不能为空");
					}
				}
			}
		});

		JPanel temp = view.mainPanel;
		temp.addKeyListener( new KeyAdapter(){
				public void keyPressed(KeyEvent e){
					if(e.getKeyCode() == KeyEvent.VK_UP){
						model.moveUp = true;
						model.moveDown = false;
						model.moveLeft = false;
						model.moveRight = false;
					}
					if(e.getKeyCode() == KeyEvent.VK_DOWN ){
						model.moveDown = true;
						model.moveUp = false;
						model.moveLeft = false;
						model.moveRight = false;
					}
					if(e.getKeyCode() == KeyEvent.VK_LEFT ){
						model.moveLeft = true;
						model.moveUp = false;
						model.moveDown = false;
						model.moveRight = false;
					}
					if(e.getKeyCode() == KeyEvent.VK_RIGHT ){
						model.moveLeft = false;
						model.moveUp = false;
						model.moveDown = false;
						model.moveRight = true;
					}

					if(e.getKeyChar() == 's')
							model.fire = true;

					if(e.getKeyCode()==e.VK_ENTER){
						if(!view.messageField.getText().equals("")){
							model.addMessage("用户端玩家说：" + view.messageField.getText());
							model.playerTypedMessage += "e" + view.messageField.getText() + ";";
							view.messageField.setText("");
						}
					}

					if(e.getKeyChar() == 'y' && model.gameOver && !model.clientVoteYes){
						model.clientVoteYes = true;
						model.addMessage("等待主机端玩家的回应...");
					}

					if(e.getKeyChar() == 'n'  && model.gameOver)
						model.clientVoteNo = true;
				}

				public void keyReleased(KeyEvent e){
					if(e.getKeyCode() == KeyEvent.VK_UP)
						model.moveUp = false;
					if(e.getKeyCode() == KeyEvent.VK_DOWN )
						model.moveDown = false;
					if(e.getKeyCode() == KeyEvent.VK_LEFT )
						model.moveLeft = false;
					if(e.getKeyCode() == KeyEvent.VK_RIGHT )
						model.moveRight = false;
					if(e.getKeyChar() == 's')
							model.fire = false;
				}
			}
		);

	}
}

