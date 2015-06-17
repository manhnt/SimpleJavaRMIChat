package dist.chat.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import dist.chat.client.IChatClient;
import dist.chat.gui.ChatMainFrame;

public class ChatServer extends UnicastRemoteObject implements IChatServer {

	private static final long serialVersionUID = 1L;
	
	private String mIpAddr;
	private String mName;
	
	private ChatMainFrame mGUI;
	
	private ArrayList<IChatClient> clients = new ArrayList<>();

	public ChatServer(String ip, String name) throws RemoteException {
		super();
		mIpAddr = ip;
		mName = name;
	}

	@Override
	public void addClient(IChatClient client) throws RemoteException {
		System.out.println("[New client " + client.getName() + " connnected]");
		publishMsg("[" + client.getName() + " has connected]");
		clients.add(client);
		client.onConnectSuccess();
		for (IChatClient c : clients) {
			try {
				c.updateMemberList();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		mGUI.updateMemberListForServer();
	}

	@Override
	public void removeClient(IChatClient client) throws RemoteException {
		client.onDisconnect();
		boolean ret = clients.remove(client);
		if (ret) {
			System.out.println("[" + client.getName() + " has disconnected]");
			publishMsg("[" + client.getName() + " has disconnected]");
			for (IChatClient c : clients) {
				try {
					c.updateMemberList();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			mGUI.updateMemberListForServer();
		}
	}

	@Override
	public void publishMsg(String msg) {
		for (IChatClient c : clients) {
			try {
				c.displayMsg(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
				System.out.println("Can't publish message on client " + c);
			}
		}
		
		mGUI.displayMsg(msg);
	}

	@Override
	public ArrayList<IChatClient> getClients() throws RemoteException {
		return clients;
	}

	@Override
	public String getIp() throws RemoteException {
		return mIpAddr;
	}
	
	@Override
	public String getName() {
		return mName;
	}
	
	public void setGUI(ChatMainFrame gui) {
		mGUI = gui;
	}
}
