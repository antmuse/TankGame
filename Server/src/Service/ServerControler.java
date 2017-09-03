package Service;

import javax.swing.*;
import java.awt.event.*;

//this class deal with  the input from the Server view
public class ServerControler{
	public ServerView view;
	public ServerModel model;
	public int helpMessageCount = 1;

	//a reference to player's tank

	public ServerControler(ServerView thisview,  ServerModel thismodel){
		view = thisview;
		model = thismodel;

		//handel sendMessage button actions
		view.sendMessage.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if(!model.gameStarted){
						model.addMessage("��û�кͱ���������, �޷����ͶԻ�");
						return;
					}

					if(!view.messageField.getText().equals("")){
						model.addMessage("���������˵��" + view.messageField.getText());
						model.playerTypedMessage += "m" + view.messageField.getText() + ";";
						view.messageField.setText("");
					}else{
						model.addMessage("�Ի����ݲ���Ϊ��");
					}
				}
			}
		);

		//handel createServer button actions
		view.createServer.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if(!model.serverCreated)
						model.t.start();
				}
			}
		);

		//handel pauseAndResume button actions
		view.pauseAndResume.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					model.pausePressed = true;;
					if(!model.gameOver && model.gameStarted){
						if(!model.gamePaused){
							model.gamePaused = true;
							model.addMessage("�����������ͣ����Ϸ");
						}else{
							model.gamePaused = false;
							model.addMessage("���������ȡ������ͣ");
						}
					}
				}
			}
		);

		//handel help button actions
		view.help.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					model.addMessage("-------------------------------̹�˴�ս 1.0-----------------------------------");
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
						model.addMessage("���������˵��" + view.messageField.getText());
						model.playerTypedMessage += "m" + view.messageField.getText() + ";";
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
					if(model.P1 != null){
						if(e.getKeyCode() == KeyEvent.VK_UP){
							model.P1.moveUp = true;
							model.P1.moveDown = false;
							model.P1.moveLeft = false;
							model.P1.moveRight = false;
						}
						if(e.getKeyCode() == KeyEvent.VK_DOWN ){
							model.P1.moveDown = true;
							model.P1.moveUp = false;
							model.P1.moveLeft = false;
							model.P1.moveRight = false;
						}
						if(e.getKeyCode() == KeyEvent.VK_LEFT ){
							model.P1.moveLeft = true;
							model.P1.moveUp = false;
							model.P1.moveDown = false;
							model.P1.moveRight = false;
						}
						if(e.getKeyCode() == KeyEvent.VK_RIGHT ){
							model.P1.moveLeft = false;
							model.P1.moveUp = false;
							model.P1.moveDown = false;
							model.P1.moveRight = true;
						}
						if(e.getKeyChar() == 's')
							model.P1.fire = true;

						if(e.getKeyCode()==e.VK_ENTER){
							if(!view.messageField.getText().equals("")){
								model.addMessage("���������˵��" + view.messageField.getText());
								model.playerTypedMessage += "m" + view.messageField.getText() + ";";
								view.messageField.setText("");
							}
						}

						if(e.getKeyChar() == 'y' && model.gameOver && !model.serverVoteYes){
							model.serverVoteYes = true;
							model.addMessage("�ȴ��û�����ҵĻ�Ӧ...");
						}

						if(e.getKeyChar() == 'n'  && model.gameOver)
							model.serverVoteNo = true;
					}
				}

				public void keyReleased(KeyEvent e){
						if(model.P1 != null){
							if(e.getKeyCode() == KeyEvent.VK_UP)
								model.P1.moveUp = false;
							if(e.getKeyCode() == KeyEvent.VK_DOWN )
								model.P1.moveDown = false;
							if(e.getKeyCode() == KeyEvent.VK_LEFT )
								model.P1.moveLeft = false;
							if(e.getKeyCode() == KeyEvent.VK_RIGHT )
								model.P1.moveRight = false;
							if(e.getKeyChar() == 's')
								model.P1.fire = false;
					}
				}
			}
		);

	}
}