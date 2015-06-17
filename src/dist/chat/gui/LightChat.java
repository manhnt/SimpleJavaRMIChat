package dist.chat.gui;
import javax.swing.JFrame;

public class LightChat {

	public static void main(String[] args) {
		ChatMainFrame mainFrame = new ChatMainFrame("Light Chat");
		mainFrame.setLocation(300, 300);
		
		mainFrame.pack();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
	}
}
