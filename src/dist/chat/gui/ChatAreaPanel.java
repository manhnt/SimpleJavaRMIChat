package dist.chat.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


public class ChatAreaPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JTextArea mChatLogArea;
	private JTextField mMsgEntryBox;
	private JButton mSendBtn;
	private JList<String> mMemberList;
	
	private ChatMainFrame mMainGUI;
	
//	private ChatClient mChatClient;
	
	/**
	 * Create the panel.
	 */
	public ChatAreaPanel(ChatMainFrame mainUI) {
		super();
		this.mMainGUI = mainUI;
		createUI();
	}
	
	private void createUI() {
		setBackground(Color.WHITE);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {2, 2};
		gridBagLayout.rowHeights = new int[] {2, 0, 2};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		mChatLogArea = new JTextArea();
		mChatLogArea.setWrapStyleWord(true);
		mChatLogArea.setBackground(new Color(224, 255, 255));
		mChatLogArea.setFont(new Font("Monospaced", Font.ITALIC, 13));
		mChatLogArea.setEditable(false);
		mChatLogArea.setBorder(BorderFactory.createEtchedBorder());
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.weightx = 3.0;
		gbc_textArea.insets = new Insets(5, 5, 5, 5);
		gbc_textArea.anchor = GridBagConstraints.WEST;
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 0;
		add(new JScrollPane(mChatLogArea), gbc_textArea);
		
		mMemberList = new JList<String>();
		mMemberList.setBorder(new TitledBorder(null, "Members", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.insets = new Insets(5, 5, 5, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 1;
		gbc_list.gridy = 0;
		add(mMemberList, gbc_list);
		
		mMsgEntryBox = new JTextField();
		mMsgEntryBox.setColumns(10);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.weightx = 3.0;
		gbc_textField.insets = new Insets(5, 5, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 1;
		add(mMsgEntryBox, gbc_textField);
		
		mSendBtn = new JButton("Send");
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.fill = GridBagConstraints.BOTH;
		gbc_btnSend.gridx = 1;
		gbc_btnSend.gridy = 1;
		add(mSendBtn, gbc_btnSend);
		
		mSendBtn.addActionListener(mSendMsg);
		mMsgEntryBox.addActionListener(mSendMsg);
		mMsgEntryBox.requestFocusInWindow();
	}

	private ActionListener mSendMsg = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String msg = mMsgEntryBox.getText().trim();
			if (!msg.isEmpty()) {
				sendMessage(msg);
				mMsgEntryBox.setText("");
				mMsgEntryBox.requestFocusInWindow();
			}
		}
	};

	protected void sendMessage(String msg) {
		mMainGUI.sendMsg(msg);
	}

	public void displayChatMsg(String msg) {
		mChatLogArea.append(msg + "\n");
		mChatLogArea.setCaretPosition(mChatLogArea.getDocument().getLength());
	}

	public ChatMainFrame getMainGUI() {
		return mMainGUI;
	}

	public void setMainGUI(ChatMainFrame mMainGUI) {
		this.mMainGUI = mMainGUI;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(500, 500);
	}

	public void showMemberList(ArrayList<String> clientNames) {
		DefaultListModel<String> memList = new DefaultListModel<String>();
		for (String c : clientNames) {
			memList.addElement(c);
		}
		mMemberList.setModel(memList);
	}

	public void disableInput() {
		mMsgEntryBox.setEnabled(false);
		mSendBtn.setEnabled(false);
	}
}
