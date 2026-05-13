# Piano App
A simple Java piano app with a keyboard GUI.
Built with Claude Haiku 4.5 AI.

# White Keys
A = C4
S = D4
D = E4
F = F4
G = G4
H = A4
J = B4
K = C5
L = D5
; = E5
' = F5

# Black Keys
W = C#4
E = D#4
T = F#4
U = G#4
O = A#4
P = B#4

# Requirements
- **Java Runtime Environment (JRE)** 11 or higher
- **Java Development Kit (JDK)** 11 or higher (for building from source)
- **Maven** 3.6 or higher (for building from source)

Double-click `Piano.bat` to start

# Sound Generation
- Uses Java's built-in `javax.sound.sampled` API
- Generates sine wave synthesis for piano notes
- Implements ADSR envelope (Attack, Decay, Sustain, Release) for realistic sound
- 44.1 kHz sample rate for good audio quality

# Possible Improvements:
- Octave adjustment controls
- Multiple instrument sounds
- Recording and playback functionality
- MIDI file import/export
- Adjustable note duration and velocity
- Visual sheet music display
