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
						model.addMessage("��û�к��������������, �޷����ͶԻ�");
						return;
					}

					if(!view.messageField.getText().equals("")){
						model.addMessage("�û������˵��" + view.messageField.getText());
						model.playerTypedMessage += "e" + view.messageField.getText() + ";";
						view.messageField.setText("");
					}else{
						model.addMessage("�Ի����ݲ���Ϊ��");
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
							model.addMessage("�û��������ͣ����Ϸ");
						}else{
							model.gamePaused = false;
							model.addMessage("�û������ȡ������ͣ");
						}
					}
				}
			}
		);

		//handel help button actions
		view.help.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					model.addMessage("-------------------------------̹�˴�ս 1.0--------------------------------------------");
					model.addMessage("����: �� s ������,  �����̵ķ����������̹�˵��ƶ�");
					model.addMessage("�������û�з�Ӧ�� 1. �رմ�д����; 2. �� tab���л� ");
					model.addMessage("�����ƽ����������ʹ�öԻ�����.");
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
					model.addMessage("��ʾ����\"tab\"�����������л��ڿ��ƽ���ͶԻ�����");
					model.addMessage("��ʾ�����س�������ֱ�ӷ������ĶԻ�");
					helpMessageCount--;
				}

				if(e.getKeyCode()==e.VK_ENTER){
					if(!view.messageField.getText().equals("")){
						model.addMessage("�û������˵��" + view.messageField.getText());
						model.playerTypedMessage += "e" + view.messageField.getText() + ";";
						view.messageField.setText("");
					}else{
						model.addMessage("�Ի����ݲ���Ϊ��");
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
							model.addMessage("�û������˵��" + view.messageField.getText());
							model.playerTypedMessage += "e" + view.messageField.getText() + ";";
							view.messageField.setText("");
						}
					}

					if(e.getKeyChar() == 'y' && model.gameOver && !model.clientVoteYes){
						model.clientVoteYes = true;
						model.addMessage("�ȴ���������ҵĻ�Ӧ...");
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

