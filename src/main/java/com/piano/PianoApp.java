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
            PianoKeyboard keyboard = new PianoKeyboard();
            frame.add(keyboard);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            // Request focus so the keyboard panel receives key events immediately
            keyboard.requestFocusInWindow();
        });
    }
}
