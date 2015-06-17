package dist.chat.gui;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dist.chat.client.ChatClient;
import dist.chat.client.ChatClient.ClientStatusListener;
import dist.chat.client.IChatClient;
import dist.chat.server.ChatServer;
import dist.chat.server.IChatServer;
import dist.chat.utils.Utils;

public class ChatMainFrame extends JFrame implements ClientStatusListener {

	private static final long serialVersionUID = 1L;
	
	private JButton mOpenRoomBtn;
	private JButton mJoinRoomBtn;
	private JPanel mRoomCtrlPanel;
	private ClientControlPanel mClientCtrlPanel;
	private ServerControlPanel mServerCtrlPanel;
	private ChatAreaPanel mChatAreaPanel;
	
	private ChatClient mClient;
	private IChatServer mServer;
	private boolean isClientMode = false;
	
	private String mRmiServerName;

	public ChatMainFrame() {
		super();
		createUI();
	}
	
	public ChatMainFrame(String title) {
		super(title);
		createUI();
	}

	private void createUI() {
		mOpenRoomBtn = new JButton("Create new room");
		mOpenRoomBtn.addActionListener(openRoomClicked);
		
		mJoinRoomBtn = new JButton("Join a room");
		mJoinRoomBtn.addActionListener(joinRoomClicked);
		
		mRoomCtrlPanel = new JPanel();
		mRoomCtrlPanel.add(mOpenRoomBtn);
		mRoomCtrlPanel.add(mJoinRoomBtn);
		
		add(mRoomCtrlPanel, BorderLayout.NORTH);
	}
	
	private ActionListener openRoomClicked = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// Remove old GUI element if present
			if (mClientCtrlPanel != null) {
				remove(mClientCtrlPanel);
			}
			if (mChatAreaPanel != null) {
				remove(mChatAreaPanel);
			}
			// Create new GUI element for this session
			if (mServerCtrlPanel == null) {
				String ipAddr = Utils.getLocalIpAddr();
				if (ipAddr == null) {
					JOptionPane.showMessageDialog(null, "Could not get your local IP", "Error", 
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				mServerCtrlPanel = new ServerControlPanel(ChatMainFrame.this, ipAddr);
			}
			add(mServerCtrlPanel, BorderLayout.CENTER);
			pack();
		}
	};
	
	private ActionListener joinRoomClicked = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// Remove old GUI element if present
			if (mServerCtrlPanel != null) {
				remove(mServerCtrlPanel);
			}
			if (mChatAreaPanel != null) {
				remove(mChatAreaPanel);
			}
			
			if (mClientCtrlPanel == null) {
				mClientCtrlPanel = new ClientControlPanel(ChatMainFrame.this);
			}
			
			add(mClientCtrlPanel, BorderLayout.CENTER);
			pack();
		}
	};

	public boolean startServer(String name, String ipAddr) {
		try {
			LocateRegistry.createRegistry(1099);
			mServer = new ChatServer(ipAddr, name);
			mRmiServerName = "rmi://" + ipAddr + "/myabc";
			Naming.rebind(mRmiServerName, mServer);
			System.out.println("[System] Chat Server is ready.");
			isClientMode = false;
			((ChatServer)mServer).setGUI(this);

			return true;
		} catch (Exception e) {
			System.out.println("Can't start server");
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not create a new room", "Error", JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}
	
	public void stopServer() {
		Registry registry;
		
		try {
			registry = LocateRegistry.getRegistry();
			registry.unbind(mRmiServerName);
			UnicastRemoteObject.unexportObject(mServer, false);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (NotBoundException e) {
			try {
				throw new RemoteException(
						"Could not unregister service, quiting anyway", e);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

		try {
			mServer.publishMsg("[System] Chat server is stopped");
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}				
		System.out.print("Shutting down...");
		try {
			Thread.sleep(2000);
			ArrayList<IChatClient> clients = mServer.getClients();
			for (IChatClient c : clients) {
				c.onDisconnect();
			}
		} catch (InterruptedException e) {
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		System.out.println("The server has been stopped");
		System.exit(0);
	}

	public void setupClientChatUI() {
		mChatAreaPanel = new ChatAreaPanel(ChatMainFrame.this);
		add(mChatAreaPanel, BorderLayout.SOUTH);
		mOpenRoomBtn.setEnabled(false);
		mJoinRoomBtn.setEnabled(false);
		updateMemberListForClient();

		pack();
	}

	public void setupServerChatUI() {
		mChatAreaPanel = new ChatAreaPanel(ChatMainFrame.this);
		add(mChatAreaPanel, BorderLayout.SOUTH);
		mJoinRoomBtn.setEnabled(false);
		mOpenRoomBtn.setEnabled(false);
		updateMemberListForServer();
		
		pack();
	}

	public void resetUI() {
		if (mClientCtrlPanel != null) {
			remove(mClientCtrlPanel);
		}
		if (mServerCtrlPanel != null) {
			remove(mServerCtrlPanel);
		}
		if (mChatAreaPanel != null) {
			remove(mChatAreaPanel);
		}
		
		mOpenRoomBtn.setEnabled(true);
		mJoinRoomBtn.setEnabled(true);
		pack();
	}

	public void updateMemberListForServer() {
		try {
			ArrayList<IChatClient> clients = mServer.getClients();
			ArrayList<String> memNames = new ArrayList<String>();
			memNames.add(mServer.getName());
			for (IChatClient c : clients) {
				memNames.add(c.getName());
			}
			
			mChatAreaPanel.showMemberList(memNames);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void updateMemberListForClient() {
		// update member list only in case of client
		try {
			ArrayList<IChatClient> clients = mClient.getServer().getClients();
			ArrayList<String> memNames = new ArrayList<String>();
			for (IChatClient c : clients) {
				memNames.add(c.getName());
			}
			
			memNames.add(mClient.getServer().getName());
			
			mChatAreaPanel.showMemberList(memNames);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println("Exception occurs when getting clients list from serv");
		}
	}

	public void setChatClient(ChatClient mClient) {
		this.mClient = mClient;
		mClient.setGUI(this);
		mClient.addClientStatusListener(this);
	}

	public void disconnClientFromServ() {
		mClient.disconnect();
	}

	public void connectClient2Serv(String servIp) {
		mClient.connect(servIp);
	}

	@Override
	public void onConnectSuccess() {
		mClientCtrlPanel.updateWithConnState(true);
		isClientMode = true;
		setupClientChatUI();
	}

	@Override
	public void onConnectFail() {
		mClientCtrlPanel.updateWithConnState(false);
		isClientMode = false;
		resetUI();
	}

	@Override
	public void onDisconnect() {
		mClientCtrlPanel.updateWithConnState(false);
		isClientMode = false;
		resetUI();
	}

	public void displayMsg(String msg) {
		mChatAreaPanel.displayChatMsg(msg);
	}

	public void sendMsg(String msg) {
		if (isClientMode) {
			mClient.sendMessage(msg);
		} else {
			try {
				String tMsg = "[" + mServer.getName() + "]" + Utils.SEPARATOR + msg;
				mServer.publishMsg(tMsg);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
}
