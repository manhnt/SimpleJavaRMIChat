package dist.chat.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dist.chat.client.ChatClient;
import dist.chat.utils.Utils;

public class ClientControlPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int IP_CHAR_NUMBER = 15;

	private JTextField mServerIpTxt;
	private JTextField mClientNameTxt;
	private JButton mConnectBtn;
	private ChatMainFrame mMainGUI;
	
//	private ChatClient mClient;

	public static final String CONNECT = "Connect";
	public static final String DIS_CONN = "Disconnect";

	public ClientControlPanel(ChatMainFrame mainUI) {
		super();
		this.mMainGUI = mainUI;
		createUI();
	}

	public void updateWithConnState(boolean isConnected) {
		if (isConnected) {
			mConnectBtn.setText(DIS_CONN);
			mConnectBtn.removeActionListener(connBtnClicked);
			mConnectBtn.addActionListener(disconnBtnClicked);
			mServerIpTxt.setEnabled(false);
			mClientNameTxt.setEnabled(false);
			
//			mMainGUI.setupUIForChat();		
		} else {
			mConnectBtn.setText(CONNECT);
			mConnectBtn.removeActionListener(disconnBtnClicked);
			mConnectBtn.addActionListener(connBtnClicked);
			mServerIpTxt.setEnabled(true);
			mClientNameTxt.setEnabled(true);
			
//			mMainGUI.resetUI();
		}
	}

	private void createUI() {
		JLabel name = new JLabel("Your name");
		mClientNameTxt = new JTextField(10);
		add(name);
		add(mClientNameTxt);

		JLabel server = new JLabel("Server IP");
		mServerIpTxt = new JTextField(IP_CHAR_NUMBER);
		mServerIpTxt.addActionListener(connBtnClicked);
		add(server);
		add(mServerIpTxt);

		mConnectBtn = new JButton(CONNECT);
		mConnectBtn.addActionListener(connBtnClicked);

		add(mConnectBtn);
	}

	private ActionListener connBtnClicked = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String name = mClientNameTxt.getText();
			if (name.trim().isEmpty()) {
				JOptionPane
						.showMessageDialog(null, "Please enter your name");
				return;
			} else if (!Utils.isValidIp4Addr(mServerIpTxt.getText().trim())) {
				JOptionPane.showMessageDialog(null,
						"Please enter a valid IP address");
				return;
			} else {
				connectServer();
			}
		}
	};

	private ActionListener disconnBtnClicked = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			mMainGUI.disconnClientFromServ();
		}
	};

	protected void connectServer() {
		ChatClient client;
		try {
			client = new ChatClient(mClientNameTxt.getText().trim());
			mMainGUI.setChatClient(client);
			mMainGUI.connectClient2Serv(mServerIpTxt.getText().trim());
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println("Can't create ChatClient");
		}
		
	}

	public void setMainGUI(ChatMainFrame gui) {
		this.mMainGUI = gui;
	}
}
