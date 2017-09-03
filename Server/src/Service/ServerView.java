package Service;

//	坦克大战连线版主机端
//	作者：胡畔
//	说明：免费软件，欢迎修改


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


//this class represents the graphic interface of the server
public class ServerView extends JFrame{
	public drawingPanel mainPanel;
	public JButton createServer, exit, pauseAndResume, help, hiddenButton;
	public JTextField messageField;
	public JButton sendMessage;

	public ServerControler controler;
	public ServerModel model;

	public ServerView(){

		super("坦克大战");


		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { }

		getContentPane().setLayout(null);

		//make main panel where the animation will be drawn
		mainPanel = new drawingPanel();
		mainPanel.setLayout(null);
		mainPanel.setBounds(0,  22, 679, 605);
		mainPanel.setBackground(new Color(128, 64, 0));
		messageField = new JTextField();
		messageField.setBounds(0,519, 560,22);
		messageField.setEnabled(false);
		sendMessage = new JButton("发送");
		sendMessage.setBounds(570,518, 62,24);
		sendMessage.setFocusable(false);
		mainPanel.add(messageField);
		mainPanel.add(sendMessage);
		getContentPane().add(mainPanel);
		mainPanel.setFocusable(true);

		//make option buttons
		createServer = new JButton("建立主机");
		createServer.setBounds(0, 0,120,22);
		getContentPane().add(createServer);
		createServer.setFocusable(false);

		pauseAndResume = new JButton("暂停/继续");
		pauseAndResume.setBounds(120, 0,120,22);
		getContentPane().add(pauseAndResume);
		pauseAndResume.setFocusable(false);

		help = new JButton("帮助");
		help.setBounds(240, 0,120,22);
		getContentPane().add(help);
		help.setFocusable(false);

		exit = new JButton("退出");
		exit.setBounds(360, 0,120,22);
		getContentPane().add(exit);
		exit.setFocusable(false);

		//setup the mian frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 130, 640, 600);
    	setVisible(true);
    	setResizable( false );

		//setup server model
		model = new ServerModel(this);

		//setup server controller
		controler = new ServerControler(this, model);


	}


	public static void main(String[] args){
		new ServerView();
	}



}