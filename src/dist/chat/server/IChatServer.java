package dist.chat.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import dist.chat.client.IChatClient;

public interface IChatServer extends Remote {

	void addClient(IChatClient client) throws RemoteException;
	void removeClient(IChatClient client) throws RemoteException;
	void publishMsg(String msg) throws RemoteException;
	ArrayList<IChatClient> getClients() throws RemoteException;
	String getIp() throws RemoteException;
	String getName() throws RemoteException;
}
