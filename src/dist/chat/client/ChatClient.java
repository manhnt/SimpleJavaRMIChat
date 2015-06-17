package dist.chat.client;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import dist.chat.gui.ChatMainFrame;
import dist.chat.server.IChatServer;
import dist.chat.utils.Utils;

public class ChatClient extends UnicastRemoteObject implements IChatClient {

	private static final long serialVersionUID = 1L;
	private String mName;
	private IChatServer mServer;
	private boolean mConnected = false;
	
	private ChatMainFrame mGUI;
	
	private ArrayList<ClientStatusListener> mStatusListener = new ArrayList<>();
	
	public ChatClient(String pName) throws RemoteException {
		super();
		mName = pName;
	}

	@Override
	public String getName() throws RemoteException {
		return mName;
	}

	@Override
	public void displayMsg(String msg) throws RemoteException {
		System.out.println(msg);
		mGUI.displayMsg(msg);
	}

	@Override
	public void updateMemberList() throws RemoteException {
		mGUI.updateMemberListForClient();
	}

	@Override
	public void onConnectSuccess() {
		for (ClientStatusListener csl : mStatusListener) {
			csl.onConnectSuccess();
		}
	}

	@Override
	public void onConnectFail() {
		for (ClientStatusListener csl : mStatusListener) {
			csl.onConnectFail();
		}
	}
	
	@Override
	public void onDisconnect() throws RemoteException {
		for (ClientStatusListener csl : mStatusListener) {
			csl.onDisconnect();
		}
	}

	public void connect(String ip) {
		try {
			mServer = (IChatServer) Naming.lookup("rmi://" + ip + "/myabc");
//			mServer = new DummyServer();
			mServer.addClient(this);
			mConnected = true;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Could not connect to server",
					"Connection error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void disconnect() {
		if (mConnected && mServer != null) {
			try {
				mServer.removeClient(this);
				mConnected = false;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public void addClientStatusListener(ClientStatusListener lis) {
		mStatusListener.add(lis);
	}

	public void setGUI(ChatMainFrame gui) {
		mGUI = gui;
	}
	
	public void sendMessage(String msg) {
		msg = "[" + mName + "]" + Utils.SEPARATOR + msg;
		try {
			mServer.publishMsg(msg);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(null, "Could not send message", "Error", 
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	public IChatServer getServer() {
		return mServer;
	}
	
	public interface ClientStatusListener {
		public void onConnectSuccess();
		public void onConnectFail();
		public void onDisconnect();
	}
}
