package dist.chat.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChatClient extends Remote {

	String getName() throws RemoteException;
	void displayMsg(String msg) throws RemoteException;
	void onConnectSuccess() throws RemoteException;
	void onConnectFail() throws RemoteException;
	void onDisconnect() throws RemoteException;
	void updateMemberList() throws RemoteException;
}
