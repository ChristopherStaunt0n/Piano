package com.piano;

import javax.swing.*;

/**
 * Main entry point for the Piano Application.
 * Creates and displays the piano GUI.
 */
public class PianoApp {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Piano Keyboard");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new PianoKeyboard());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
