package me.robert.tb.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import me.robert.tb.Launch;

public class ChannelTab extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTextField channelName;
    private SpringLayout springLayout;
    private JButton btnJoin;
    private JScrollPane scrollPane;

    private final String[] COL_NAMES = { "Channels" };
    private String[][] rows;
    private JTable table;

    private ArrayList<String> channelNames;
    private HashMap<String, ChatTab> channels;

    /**
     * Create the panel.
     */
    public ChannelTab() {
	springLayout = new SpringLayout();
	setLayout(springLayout);

	channelName = new JTextField();
	springLayout.putConstraint(SpringLayout.NORTH, channelName, 59, SpringLayout.NORTH, this);
	springLayout.putConstraint(SpringLayout.WEST, channelName, 156, SpringLayout.WEST, this);
	springLayout.putConstraint(SpringLayout.EAST, channelName, 390, SpringLayout.WEST, this);
	add(channelName);
	channelName.setColumns(10);

	btnJoin = new JButton("Join");
	btnJoin.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		join(channelName.getText());
		channelName.setText("");
	    }
	});

	springLayout.putConstraint(SpringLayout.NORTH, btnJoin, 10, SpringLayout.SOUTH, channelName);
	springLayout.putConstraint(SpringLayout.WEST, btnJoin, 0, SpringLayout.WEST, channelName);
	springLayout.putConstraint(SpringLayout.EAST, btnJoin, 0, SpringLayout.EAST, channelName);
	btnJoin.setToolTipText("Join a channel");
	add(btnJoin);

	scrollPane = new JScrollPane();
	springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.SOUTH, btnJoin);
	springLayout.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, this);
	springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH, this);
	springLayout.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, this);
	add(scrollPane);

	rows = new String[1][COL_NAMES.length];
	rows[0][0] = "You aren't in any channels";
	table = new JTable(rows, COL_NAMES);
	table.setToolTipText("Channels you are in");
	table.setEnabled(false);
	scrollPane.setViewportView(table);

	channels = new HashMap<String, ChatTab>();
	channelNames = new ArrayList<String>();
	channelName.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		join(channelName.getText());
		channelName.setText("");
	    }
	});
    }

    public void join(String channel) {
	if (channel.equalsIgnoreCase("")) {
	    join(Launch.getWindow().config_account.getString("lastChannel"));
	    return;
	}

	Launch.getWindow().config_account.set("lastChannel", channel);
	addChannel(channel);
	Launch.getWindow().addBot(channel);
    }

    public void addChannel(String channel) {
	channels.put(channel, new ChatTab(channel));
	channelNames.add(channel);
	refreshChannels();
    }

    private void refreshChannels() {
	rows = new String[channelNames.size()][COL_NAMES.length];

	for (int i = 0; i < channelNames.size(); i++) {
	    rows[i][0] = channelNames.get(i);
	}

	table = new JTable(rows, COL_NAMES);
	scrollPane.setViewportView(table);
    }

    public void removeChannel(String channelName) {
	Launch.getWindow().baseChatTab.removeTab(channels.get(channelName));
	channels.remove(channelName);
	for (int i = 0; i < channelNames.size(); i++) {
	    if (channelNames.get(i).equalsIgnoreCase(channelName))
		channelNames.remove(i);
	}
	refreshChannels();
    }

    public ChatTab getChatTab(String channelName) {
	return channels.get(channelName);
    }
}
