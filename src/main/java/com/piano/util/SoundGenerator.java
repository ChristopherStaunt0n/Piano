package com.piano.util;

import javax.sound.sampled.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates piano sounds using synthesized audio.
 * Supports playing and stopping notes at different frequencies.
 */
public class SoundGenerator {
    
    private static final int SAMPLE_RATE = 44100;
    private static final float VOLUME = 0.7f;
    private static final long NOTE_DURATION = 2000; // 2 seconds
    
    private Map<Integer, SoundThread> activeSounds;
    
    public SoundGenerator() {
        activeSounds = new HashMap<>();
    }
    
    /**
     * Convert MIDI note number to frequency in Hz
     */
    private double midiToFrequency(int midiNote) {
        return 440.0 * Math.pow(2.0, (midiNote - 69) / 12.0);
    }
    
    /**
     * Play a note given its MIDI note number
     */
    public synchronized void playNote(int midiNote) {
        // Stop any existing sound for this note
        if (activeSounds.containsKey(midiNote)) {
            activeSounds.get(midiNote).stopSound();
        }
        
        double frequency = midiToFrequency(midiNote);
        SoundThread thread = new SoundThread(frequency);
        activeSounds.put(midiNote, thread);
        thread.start();
    }
    
    /**
     * Stop playing a note
     */
    public synchronized void stopNote(int midiNote) {
        if (activeSounds.containsKey(midiNote)) {
            activeSounds.get(midiNote).stopSound();
            activeSounds.remove(midiNote);
        }
    }
    
    /**
     * Inner class to handle sound generation in a separate thread
     */
    private static class SoundThread extends Thread {
        private double frequency;
        private volatile boolean running = true;
        
        public SoundThread(double frequency) {
            this.frequency = frequency;
            setDaemon(true);
        }
        
        @Override
        public void run() {
            try {
                AudioFormat format = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    SAMPLE_RATE,
                    16,
                    1,
                    2,
                    SAMPLE_RATE,
                    false
                );
                
                SourceDataLine line = AudioSystem.getSourceDataLine(format);
                line.open(format);
                line.start();
                
                byte[] buffer = new byte[2048];
                long startTime = System.currentTimeMillis();
                int sampleCount = 0;
                
                while (running && (System.currentTimeMillis() - startTime) < NOTE_DURATION) {
                    for (int i = 0; i < buffer.length; i += 2) {
                        // Apply envelope (fade in and fade out)
                        double time = (double) sampleCount / SAMPLE_RATE;
                        double envelope = getEnvelope(time);
                        
                        // Generate sine wave
                        double value = Math.sin(2.0 * Math.PI * frequency * time) * envelope * VOLUME;
                        
                        // Convert to 16-bit PCM
                        short sample = (short) (value * Short.MAX_VALUE);
                        buffer[i] = (byte) (sample & 0xFF);
                        buffer[i + 1] = (byte) ((sample >> 8) & 0xFF);
                        
                        sampleCount++;
                    }
                    
                    line.write(buffer, 0, buffer.length);
                }
                
                line.drain();
                line.close();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
        
        /**
         * Apply ADSR envelope to smooth the note
         */
        private double getEnvelope(double time) {
            double attackTime = 0.05;
            double releaseTime = 0.3;
            
            if (time < attackTime) {
                return time / attackTime;
            } else if (!running) {
                double releaseProgress = (time - attackTime) / releaseTime;
                return Math.max(0, 1.0 - releaseProgress);
            }
            return 1.0;
        }
        
        public void stopSound() {
            running = false;
        }
    }
}
