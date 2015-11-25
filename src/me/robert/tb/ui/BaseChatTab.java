package me.robert.tb.ui;

import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JTabbedPane;

public class BaseChatTab extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTabbedPane tabbedPane;

    public BaseChatTab() {
	SpringLayout springLayout = new SpringLayout();
	setLayout(springLayout);

	tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	springLayout.putConstraint(SpringLayout.NORTH, tabbedPane, 0, SpringLayout.NORTH, this);
	springLayout.putConstraint(SpringLayout.WEST, tabbedPane, 0, SpringLayout.WEST, this);
	springLayout.putConstraint(SpringLayout.SOUTH, tabbedPane, 0, SpringLayout.SOUTH, this);
	springLayout.putConstraint(SpringLayout.EAST, tabbedPane, 0, SpringLayout.EAST, this);
	add(tabbedPane);
    }

    public void addTab(String tabName, JPanel panel) {
	tabbedPane.add(tabName, panel);
    }

    public void removeTab(JPanel panel) {
	tabbedPane.remove(panel);
    }
}
