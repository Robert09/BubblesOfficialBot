package me.robert.tb.ui;

import java.io.File;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import com.google.gson.JsonParser;

import me.robert.configuration.Configuration;
import me.robert.tb.Bot;

public class Window extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    // Tabs
    private JTabbedPane tabbedPane;
    private LoginTab loginTab;

    public ChannelTab channelTab;
    public BaseChatTab baseChatTab;
    public BaseViewerTab baseViewerTab;

    private String botName;
    private String oAuth;

    // Config stuff
    public Configuration config_account;

    private JsonParser json;

    // BOTS
    private HashMap<String, Bot> bots;

    /**
     * Create the frame.
     */
    public Window() {
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(100, 100, 450, 300);
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(contentPane);
	SpringLayout sl_contentPane = new SpringLayout();
	contentPane.setLayout(sl_contentPane);

	tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	sl_contentPane.putConstraint(SpringLayout.NORTH, tabbedPane, 0, SpringLayout.NORTH, contentPane);
	sl_contentPane.putConstraint(SpringLayout.WEST, tabbedPane, 0, SpringLayout.WEST, contentPane);
	sl_contentPane.putConstraint(SpringLayout.SOUTH, tabbedPane, 0, SpringLayout.SOUTH, contentPane);
	sl_contentPane.putConstraint(SpringLayout.EAST, tabbedPane, 0, SpringLayout.EAST, contentPane);
	contentPane.add(tabbedPane);

	setLocationRelativeTo(null);

	init();
    }

    private void init() {
	bots = new HashMap<String, Bot>();
	json = new JsonParser();

	config_account = new Configuration("C:\\twitchbot", new File("C:\\twitchbot\\account.yml"));

	loginTab = new LoginTab();
	channelTab = new ChannelTab();
	baseChatTab = new BaseChatTab();
	baseViewerTab = new BaseViewerTab();

	addTab("Login Tab", loginTab);
	addTab("Channels Tab", channelTab);
	addTab("Chat Tab", baseChatTab);
	addTab("Viewer Tab", baseViewerTab);
    }

    public void addTab(String tabName, JPanel panel) {
	tabbedPane.add(tabName, panel);
    }

    public String getBotName() {
	return this.botName;
    }

    public String getOAuth() {
	return this.oAuth;
    }

    public Bot getBot(String channelName) {
	return bots.get(channelName);
    }

    public JsonParser getJson() {
	return json;
    }

    public void setBotName(String botName) {
	this.botName = botName;
    }

    public void setOAuth(String oAuth) {
	this.oAuth = oAuth;
    }

    public void addBot(String channelName) {
	Bot temp = new Bot(channelName);
	bots.put(channelName, temp);
    }

    public void removeBot(String channelName) {
	bots.get(channelName).disconnect();
	bots.get(channelName).dispose();
	bots.remove(channelName);
    }
}
