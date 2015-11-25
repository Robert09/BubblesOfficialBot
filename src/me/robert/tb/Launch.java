package me.robert.tb;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.skin.SubstanceMagmaLookAndFeel;

import me.robert.tb.ui.Window;

public class Launch {

    private static Window window;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	try {
	    UIManager.setLookAndFeel(new SubstanceMagmaLookAndFeel());
	} catch (UnsupportedLookAndFeelException e1) {
	    e1.printStackTrace();
	}

	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    window = new Window();
		    window.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    public static Window getWindow() {
	return window;
    }

}
