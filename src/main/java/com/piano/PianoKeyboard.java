package com.piano;

import com.piano.util.SoundGenerator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * GUI component representing a piano keyboard.
 * Handles keyboard input and displays visual representation of keys.
 */
public class PianoKeyboard extends JPanel implements KeyListener {
    
    private static final int KEY_WIDTH = 40;
    private static final int KEY_HEIGHT = 200;
    private static final int BLACK_KEY_WIDTH = 25;
    private static final int BLACK_KEY_HEIGHT = 130;
    private static final int PANEL_WIDTH = 880;
    private static final int PANEL_HEIGHT = 220;
    
    private SoundGenerator soundGenerator;
    private Map<Integer, Integer> keyToNote;
    private Map<Integer, Boolean> pressedKeys;
    
    // Keyboard layout mapping (QWERTY piano layout)
    private static final int[] KEY_CODES = {
        KeyEvent.VK_A, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_E, KeyEvent.VK_D,
        KeyEvent.VK_F, KeyEvent.VK_T, KeyEvent.VK_G, KeyEvent.VK_H, KeyEvent.VK_U,
        KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_O, KeyEvent.VK_L, KeyEvent.VK_P,
        KeyEvent.VK_SEMICOLON, KeyEvent.VK_QUOTE
    };
    
    private static final int[] MIDI_NOTES = {
        60, 61, 62, 63, 64, 65, 66, 67, 68, 69,
        70, 71, 72, 73, 74, 75, 76
    };
    
    private static final boolean[] IS_BLACK_KEY = {
        false, true, false, true, false, false, true, false, true, false,
        true, false, false, true, false, true, false
    };
    
    public PianoKeyboard() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(new Color(240, 240, 240));
        setFocusable(true);
        addKeyListener(this);
        
        soundGenerator = new SoundGenerator();
        keyToNote = new HashMap<>();
        pressedKeys = new HashMap<>();
        
        // Map keyboard keys to MIDI notes
        for (int i = 0; i < KEY_CODES.length; i++) {
            keyToNote.put(KEY_CODES[i], MIDI_NOTES[i]);
            pressedKeys.put(KEY_CODES[i], false);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw white keys
        int xOffset = 20;
        int whiteKeyIndex = 0;
        
        for (int i = 0; i < KEY_CODES.length; i++) {
            if (!IS_BLACK_KEY[i]) {
                int x = xOffset + (whiteKeyIndex * KEY_WIDTH);
                drawWhiteKey(g2d, x, 10, KEY_CODES[i]);
                whiteKeyIndex++;
            }
        }
        
        // Draw black keys
        whiteKeyIndex = 0;
        for (int i = 0; i < KEY_CODES.length; i++) {
            if (IS_BLACK_KEY[i]) {
                // Calculate position relative to white keys
                int precedingWhites = 0;
                for (int j = 0; j < i; j++) {
                    if (!IS_BLACK_KEY[j]) precedingWhites++;
                }
                
                int x = xOffset + (precedingWhites * KEY_WIDTH) + KEY_WIDTH - BLACK_KEY_WIDTH / 2;
                drawBlackKey(g2d, x, 10, KEY_CODES[i]);
            }
        }
        
        // Draw help text
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        g2d.drawString("White Keys: A W S E D F T G H U J K O L P ; '", 20, PANEL_HEIGHT - 5);
    }
    
    private void drawWhiteKey(Graphics2D g, int x, int y, int keyCode) {
        boolean isPressed = pressedKeys.getOrDefault(keyCode, false);
        
        g.setColor(isPressed ? new Color(200, 200, 255) : Color.WHITE);
        g.fillRect(x, y, KEY_WIDTH - 1, KEY_HEIGHT - 1);
        
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawRect(x, y, KEY_WIDTH - 1, KEY_HEIGHT - 1);
        
        // Draw key label
        g.setFont(new Font("Arial", Font.BOLD, 10));
        String label = KeyEvent.getKeyText(keyCode);
        FontMetrics fm = g.getFontMetrics();
        int textX = x + (KEY_WIDTH - fm.stringWidth(label)) / 2;
        int textY = y + KEY_HEIGHT - 10;
        g.drawString(label, textX, textY);
    }
    
    private void drawBlackKey(Graphics2D g, int x, int y, int keyCode) {
        boolean isPressed = pressedKeys.getOrDefault(keyCode, false);
        
        g.setColor(isPressed ? new Color(100, 100, 200) : Color.BLACK);
        g.fillRect(x, y, BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);
        
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawRect(x, y, BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);
        
        // Draw key label
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 8));
        String label = KeyEvent.getKeyText(keyCode);
        FontMetrics fm = g.getFontMetrics();
        int textX = x + (BLACK_KEY_WIDTH - fm.stringWidth(label)) / 2;
        int textY = y + BLACK_KEY_HEIGHT - 8;
        g.drawString(label, textX, textY);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (keyToNote.containsKey(e.getKeyCode())) {
            int note = keyToNote.get(e.getKeyCode());
            if (!pressedKeys.get(e.getKeyCode())) {
                pressedKeys.put(e.getKeyCode(), true);
                soundGenerator.playNote(note);
                repaint();
            }
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        if (keyToNote.containsKey(e.getKeyCode())) {
            pressedKeys.put(e.getKeyCode(), false);
            soundGenerator.stopNote(keyToNote.get(e.getKeyCode()));
            repaint();
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}
