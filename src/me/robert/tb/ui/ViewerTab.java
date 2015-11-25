package me.robert.tb.ui;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;

import me.robert.tb.Launch;

public class ViewerTab extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable table;
    private JScrollPane scrollPane;

    private String[][] rows;

    private HashMap<String, Boolean> viewers;
    private ArrayList<String> usernames;

    private String channelName;

    public static final String[] colNames = { "Username", "Mod", "Is following" };

    /**
     * Create the panel.
     */
    public ViewerTab(String channelName) {
	this.channelName = channelName;
	SpringLayout springLayout = new SpringLayout();
	setLayout(springLayout);

	scrollPane = new JScrollPane();
	springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.NORTH, this);
	springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, this);
	springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 0, SpringLayout.SOUTH, this);
	springLayout.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, this);
	add(scrollPane);

	rows = new String[1][colNames.length];
	rows[0][0] = "No viewers in chat";
	table = new JTable(rows, colNames);
	table.setEnabled(false);
	table.setColumnSelectionAllowed(true);
	table.setCellSelectionEnabled(true);
	scrollPane.setViewportView(table);

	viewers = new HashMap<String, Boolean>();
	usernames = new ArrayList<String>();
	addViewer(Launch.getWindow().getBotName(), true);
    }

    public void addViewer(String username, boolean isMod) {
	if (!viewers.containsKey(username)) {
	    viewers.put(username, isMod);
	    usernames.add(username);
	} else
	    return;
    }

    public void refresh() {
	this.remove(table);
	this.remove(scrollPane);

	SpringLayout springLayout = new SpringLayout();
	setLayout(springLayout);

	scrollPane = new JScrollPane();
	springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.NORTH, this);
	springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, this);
	springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 0, SpringLayout.SOUTH, this);
	springLayout.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, this);
	add(scrollPane);

	rows = new String[viewers.size()][colNames.length];

	if (viewers == null || viewers.size() == 0) {
	    rows = new String[1][colNames.length];
	    rows[0][0] = "No viewers in chat";
	} else {
	    for (int i = 0; i < usernames.size(); i++)
		for (int j = 0; j < colNames.length; j++) {
		    String viewer = usernames.get(i);
		    if (j == 0)
			rows[i][j] = viewer;
		    else if (j == 1) {
			if (viewers.get(viewer))
			    rows[i][j] = "true";
			else
			    rows[i][j] = "false";
		    } else {
			if (Launch.getWindow().getBot(channelName).getFollowers().getFollowerFile().contains(viewer))
			    rows[i][j] = "true";
			else
			    rows[i][j] = "false";
		    }
		}
	}

	table = new JTable(rows, colNames);
	table.setEnabled(false);
	table.setColumnSelectionAllowed(true);
	table.setCellSelectionEnabled(true);
	scrollPane.setViewportView(table);
	scrollPane.repaint();
	table.repaint();

	System.out.println("Done loading viewers.");
    }
}
