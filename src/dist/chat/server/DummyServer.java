package dist.chat.server;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dist.chat.client.IChatClient;

public class DummyServer implements IChatServer {
	private ArrayList<IChatClient> clients = new ArrayList<>();
	
	@Override
	public void addClient(IChatClient client) throws RemoteException {
		clients.add(client);
		client.onConnectSuccess();
	}

	@Override
	public void removeClient(IChatClient client) throws RemoteException {
		client.onDisconnect();
		boolean ret = clients.remove(client);
		if (ret) {
			System.out.println("[" + client.getName() + " has disconnected]");
			publishMsg("[" + client.getName() + " has disconnected]");
		}
	}

	@Override
	public void publishMsg(String msg) throws RemoteException {
		for (IChatClient c : clients) {
			try {
				c.displayMsg(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
				System.out.println("Can't publish message on client " + c);
			}
		}
	}

	@Override
	public ArrayList<IChatClient> getClients() throws RemoteException {
		return clients;
	}

	@Override
	public String getIp() throws RemoteException {
		return "192.168.105.115";
	}

	@Override
	public String getName() throws RemoteException {
		return new String("Dummy Server");
	}

}
