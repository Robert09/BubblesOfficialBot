package me.robert.tb.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.google.gson.JsonObject;

import me.robert.tb.Launch;
import me.robert.tb.util.HTTPConnect;

public class ChatTab extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JButton btnLeave;
    private JButton btnSend;
    private JScrollPane scrollPane;
    private JTextPane textPane;

    private JLabel lblPlaying;
    private JLabel lblTitle;
    private JLabel lblFollowers;

    private String lastSender = "";
    private String channelName = "";

    private StyledDocument doc;

    public ChatTab(String channelName) {
	this.channelName = channelName;
	SpringLayout springLayout = new SpringLayout();
	this.setLayout(springLayout);

	this.btnLeave = new JButton("Leave");
	btnLeave.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		Launch.getWindow().removeBot(channelName);
		Launch.getWindow().channelTab.removeChannel(channelName);
	    }
	});

	springLayout.putConstraint(SpringLayout.NORTH, btnLeave, 10, SpringLayout.NORTH, this);
	springLayout.putConstraint(SpringLayout.EAST, btnLeave, -10, SpringLayout.EAST, this);
	this.add(btnLeave);

	this.textField = new JTextField();
	springLayout.putConstraint(SpringLayout.WEST, textField, 10, SpringLayout.WEST, this);
	springLayout.putConstraint(SpringLayout.SOUTH, textField, -10, SpringLayout.SOUTH, this);
	this.add(textField);
	this.textField.setColumns(10);

	this.btnSend = new JButton("Send");
	springLayout.putConstraint(SpringLayout.EAST, textField, -10, SpringLayout.WEST, btnSend);
	btnSend.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		messageChannel();
	    }
	});
	springLayout.putConstraint(SpringLayout.SOUTH, btnSend, -10, SpringLayout.SOUTH, this);
	springLayout.putConstraint(SpringLayout.EAST, btnSend, 0, SpringLayout.EAST, btnLeave);
	this.add(btnSend);

	this.scrollPane = new JScrollPane();
	springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 1, SpringLayout.SOUTH, btnLeave);
	springLayout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, this);
	springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -7, SpringLayout.NORTH, btnSend);
	springLayout.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, this);
	this.add(scrollPane);

	this.textPane = new JTextPane();
	textPane.setEditable(false);
	this.doc = textPane.getStyledDocument();
	this.scrollPane.setViewportView(textPane);

	textField.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		messageChannel();
	    }
	});
	Launch.getWindow().baseChatTab.addTab(channelName, this);

	set();

	lblPlaying = new JLabel("Playing: ");
	lblPlaying.setForeground(Color.RED);
	springLayout.putConstraint(SpringLayout.NORTH, lblPlaying, 10, SpringLayout.NORTH, this);
	springLayout.putConstraint(SpringLayout.WEST, lblPlaying, 0, SpringLayout.WEST, textField);
	add(lblPlaying);

	lblTitle = new JLabel("Title: ");
	lblTitle.setForeground(Color.RED);
	springLayout.putConstraint(SpringLayout.NORTH, lblTitle, 0, SpringLayout.NORTH, btnLeave);
	springLayout.putConstraint(SpringLayout.WEST, lblTitle, 6, SpringLayout.EAST, lblPlaying);
	add(lblTitle);

	lblFollowers = new JLabel("Followers: ");
	lblFollowers.setForeground(Color.RED);
	springLayout.putConstraint(SpringLayout.NORTH, lblFollowers, 0, SpringLayout.NORTH, btnLeave);
	springLayout.putConstraint(SpringLayout.WEST, lblFollowers, 6, SpringLayout.EAST, lblTitle);
	add(lblFollowers);
    }

    private void messageChannel() {
	Launch.getWindow().getBot(this.channelName).sendMessage("#" + channelName, textField.getText());
	append(Launch.getWindow().getBotName(), textField.getText());
	textField.setText("");
    }

    public void set() {
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		JsonObject obj = Launch.getWindow().getJson()
			.parse(HTTPConnect.getResponse("https://api.twitch.tv/kraken/channels/" + channelName))
			.getAsJsonObject();

		lblPlaying.setText("Playing: " + obj.get("game").getAsString());
		lblTitle.setText("Title: " + obj.get("status").getAsString());
		lblFollowers.setText("Followers: " + obj.get("followers").getAsInt());

		System.out.println("Done updating the title.");
	    }
	}).start();
    }

    private boolean style = false;

    public void append(String sender, String message) {
	Style style1 = doc.addStyle("style1", null);
	style1.addAttribute(StyleConstants.Foreground, new Color(0, 191, 255));

	Style style2 = doc.addStyle("style2", null);
	style2.addAttribute(StyleConstants.Foreground, new Color(244, 164, 96));

	StyleConstants.setBold(style1, true);
	StyleConstants.setBold(style2, true);

	StyleConstants.setFontSize(style1, 13);
	StyleConstants.setFontSize(style2, 13);

	try {

	    if (style && lastSender.equalsIgnoreCase(sender))
		this.doc.insertString(doc.getLength(), "[" + sender + "]: " + message + "\n", style1);
	    else if (!style && lastSender.equalsIgnoreCase(sender))
		this.doc.insertString(doc.getLength(), "[" + sender + "]: " + message + "\n", style2);
	    else if (style && !lastSender.equalsIgnoreCase(sender)) {
		this.doc.insertString(doc.getLength(), "[" + sender + "]: " + message + "\n", style2);
		style = !style;
	    } else if (!style && !lastSender.equalsIgnoreCase(sender)) {
		this.doc.insertString(doc.getLength(), "[" + sender + "]: " + message + "\n", style1);
		style = !style;
	    }

	} catch (BadLocationException e) {
	    e.printStackTrace();
	}
	this.lastSender = sender;
	this.textPane.setCaretPosition(textPane.getDocument().getLength());
    }
}
