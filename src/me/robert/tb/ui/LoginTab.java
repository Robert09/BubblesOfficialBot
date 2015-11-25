package me.robert.tb.ui;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import me.robert.tb.Launch;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginTab extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTextField botName;
    private JPasswordField oAuth;

    /**
     * Create the panel.
     */
    public LoginTab() {
	SpringLayout springLayout = new SpringLayout();
	setLayout(springLayout);

	botName = new JTextField();
	springLayout.putConstraint(SpringLayout.NORTH, botName, 56, SpringLayout.NORTH, this);
	springLayout.putConstraint(SpringLayout.WEST, botName, 95, SpringLayout.WEST, this);
	springLayout.putConstraint(SpringLayout.EAST, botName, -95, SpringLayout.EAST, this);
	add(botName);
	botName.setColumns(10);

	oAuth = new JPasswordField();
	springLayout.putConstraint(SpringLayout.NORTH, oAuth, 10, SpringLayout.SOUTH, botName);
	springLayout.putConstraint(SpringLayout.WEST, oAuth, 0, SpringLayout.WEST, botName);
	springLayout.putConstraint(SpringLayout.SOUTH, oAuth, 26, SpringLayout.SOUTH, botName);
	springLayout.putConstraint(SpringLayout.EAST, oAuth, 0, SpringLayout.EAST, botName);
	add(oAuth);

	JButton btnLogin = new JButton("Login");
	btnLogin.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent arg0) {
		login();
	    }
	});
	springLayout.putConstraint(SpringLayout.NORTH, btnLogin, 10, SpringLayout.SOUTH, oAuth);
	springLayout.putConstraint(SpringLayout.WEST, btnLogin, 0, SpringLayout.WEST, botName);
	springLayout.putConstraint(SpringLayout.EAST, btnLogin, 0, SpringLayout.EAST, botName);
	add(btnLogin);

	botName.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		login();
	    }
	});

	oAuth.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		login();
	    }
	});
    }

    @SuppressWarnings("deprecation")
    private void login() {
	if (botName.getText().isEmpty()) {
	    Launch.getWindow().setBotName(Launch.getWindow().config_account.getString("botName"));
	    Launch.getWindow().setOAuth(Launch.getWindow().config_account.getString("oAuth"));

	    System.out.println(
		    "Bot name: " + Launch.getWindow().getBotName() + " oAuth: " + Launch.getWindow().getOAuth());
	    return;
	}
	Launch.getWindow().setBotName(botName.getText());
	Launch.getWindow().setOAuth(oAuth.getText());

	Launch.getWindow().config_account.set("botName", botName.getText());
	Launch.getWindow().config_account.set("oAuth", oAuth.getText());
	
	botName.setText("");
	oAuth.setText("");
    }

}
