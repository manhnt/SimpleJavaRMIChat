package dist.chat.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ServerControlPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final String OPEN_COMMAND = "Open";
	private static final String CLOSE_COMMAND = "Close";

	private JTextField mServerName;
	private JButton mRoomCtrlBtn;
	private ChatMainFrame mMainGUI;
	private String mServerIp;
	
	public ServerControlPanel(ChatMainFrame mainUI, String ip) {
		super();
		this.mMainGUI = mainUI;
		this.mServerIp = ip;
		createUI();
	}

	private void createUI() {
		JLabel name = new JLabel("Your name");
		add(name);
		
		mServerName = new JTextField(10);
		mServerName.addActionListener(mNameAction);
		add(mServerName);
		
		JLabel ipLb = new JLabel("Your IP");
		add(ipLb);
		JTextField ipTxt = new JTextField();
		ipTxt.setText(mServerIp);
		ipTxt.setEditable(false);
		add(ipTxt);
		
		mRoomCtrlBtn = new JButton("Open Room");
		mRoomCtrlBtn.setActionCommand(OPEN_COMMAND);
		mRoomCtrlBtn.addActionListener(mCtrlBtnClicked);
		
		add(mRoomCtrlBtn);
	}
	
	private ActionListener mNameAction = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (mServerName.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Please enter your name");
				return;
			}
			if (mMainGUI.startServer(mServerName.getText().trim(), mServerIp)) {
				mRoomCtrlBtn.setText("Close room");
				mRoomCtrlBtn.setActionCommand(CLOSE_COMMAND);
				mServerName.setEditable(false);
				mMainGUI.setupServerChatUI();
			}
		}
	};
	
	private ActionListener mCtrlBtnClicked = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (mServerName.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Please enter your name");
				return;
			}
			
			if (arg0.getActionCommand().equals(OPEN_COMMAND)) {
				if (mMainGUI.startServer(mServerName.getText().trim(), mServerIp)) {
					mRoomCtrlBtn.setText("Close room");
					mRoomCtrlBtn.setActionCommand(CLOSE_COMMAND);
					mServerName.setEditable(false);
					mMainGUI.setupServerChatUI();
				}
			} else if (arg0.getActionCommand().equals(CLOSE_COMMAND)) {
				mRoomCtrlBtn.setText("Open room");
				mRoomCtrlBtn.setActionCommand(OPEN_COMMAND);
				mServerName.setEditable(true);
				mMainGUI.stopServer();
				mMainGUI.resetUI();
			}
		}
	};
	
	public void setMainGUI(ChatMainFrame gui) {
		mMainGUI = gui;
	}
}
