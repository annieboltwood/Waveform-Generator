/*
 * ANNIE BOLTWOOD
 * Student ID: 301455990
 * CMPT 365 Assignment 1 Part 1
*/

import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;  
import java.io.*;
import java.awt.event.*;  
import java.io.File;


public class ReadWav{
    
    //Global Variables
    private int bytesRead;
    private float sampleRate;
    private int sampleSizeBits;
    private int numSamples;
    private int numChannels;
    private int frameSize;
    private float frameRate; 
   
    //Constructor
    ReadWav(int br, float sr, int ss, int nc, int fs, float fr, int ns){
        bytesRead = br;
        sampleRate = sr;
        sampleSizeBits = ss;
        numSamples = ns;
        numChannels = nc;
        frameSize = fs;
        frameRate = fr;
        
    }
    
    //Print Wav File Information
    void printInfo(){
        System.out.println("[WAV FILE INFORMATION]");
        System.out.println("Successfully read " + bytesRead + " bytes of audio data.");
        System.out.println("Sample Rate (Whole File): " + sampleRate + " Hz");
        System.out.println("Sample Size (bits): " + sampleSizeBits);
        System.out.println("Number of Samples: " + numSamples);
        System.out.println("Channels: " + numChannels);
        System.out.println("Frame Size (bytes): " + frameSize);
        System.out.println("Frame Rate: " + frameRate + " frames/second");
    
    }
    
    //Create Frame and Display Waveforms
    public void displayWaveforms(short[] samples, int frameX, int frameY){
        //Create Frame
        JFrame frame = new JFrame("Waveform Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Check number of channels
        if(numChannels == 2){
            
            //Splitting channels into left and right
            short[] Lchannel = new short[(int)Math.ceil(numSamples/2)];
            short[] Rchannel = new short[(int)Math.ceil(numSamples/2)];
                
            for(int i = 0; i<((int)Math.floor(numSamples/2)); i++){
                Lchannel[i] = samples[i*2];
                Rchannel[i] = samples[(i*2)+1];
            }
            
            //New panel for waveform
            JPanel panel = new JPanel(){
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    
                    //Frame split in two to draw both waveforms
                    drawWaveform(g, Lchannel, Color.GREEN, 0, frameY/4, frameX, frameY);
                    drawWaveform(g, Rchannel, Color.GREEN, 0, frameY*3/4, frameX, frameY);
                    
                    //Display values on screen
                    int fontSize = 12;
                    drawLabel(g, "Left Channel", 10 ,fontSize + 2, fontSize);
                    g.setColor(Color.WHITE);
                    g.drawLine(0, frameY/2, frameX, frameY/2);
                    drawLabel(g, "Right Channel", 10 ,frameY/2 + fontSize + 2, fontSize);
                    String title = "[NUMBER SAMPLES PER CHANNEL: " + (int)(numSamples/2) + "]     [SAMPLING FREQUENCY: "+ sampleRate + "]";
                    drawLabel(g, title, frameX/2 - (int)((title.length()/2)*(fontSize/2)), fontSize + 2, fontSize);
                }
            };
            
            //Set the frame and panel
            panel.setBackground(Color.BLACK);
            frame.add(panel);
            frame.setSize(frameX, frameY);
            frame.setVisible(true);
            
        }else if(numChannels == 1){
            
            //New panel for waveform
            JPanel panel = new JPanel(){
                @Override
                protected void paintComponent(Graphics g){
                    super.paintComponent(g);
                    //Draw Waveforms
                    drawWaveform(g, samples, Color.GREEN, 0, frameY/2, frameX, frameY);
                    
                    //Display values on screen
                    int fontSize = 12;
                    String title = "[NUMBER SAMPLES PER CHANNEL: " + (int)(numSamples/2) + "]     [SAMPLING FREQUENCY: "+ sampleRate + "]";
                    drawLabel(g, title, frameX/2 - (int)((title.length()/2)*(fontSize/2)), fontSize + 2, fontSize);
                    
                }
            };
            
            //Set the frame and panel
            panel.setBackground(Color.BLACK);
            frame.add(panel);
            frame.setSize(frameX, frameY);
            frame.setVisible(true);
        }
    
    }
    
    //Add Text to Panel
    private void drawLabel(Graphics g, String text, int x, int y, int fontSize){
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, fontSize)); //Set font and size
        g.drawString(text, x, y);
    }
    
    //Draw Waveform
    private void drawWaveform(Graphics g, short[] samples, Color color, int positionX, int positionY, int frameX, int frameY){
        //Checking samples validity
        if (samples == null || samples.length == 0) {
            return;
        }
    
        int width = frameX;
        int height = frameY/2;
        g.setColor(color);
        
        //Scale down the sample size and draw
        for (int i = 1; i < samples.length; i++) {
            int x1 = (i - 1) * width / samples.length + positionX;
            int y1 = (int) (positionY - samples[i - 1] * height / Short.MAX_VALUE);
            int x2 = i * width / samples.length + positionX;
            int y2 = (int) (positionY - samples[i] * height / Short.MAX_VALUE);
            g.drawLine(x1, y1, x2, y2);
        }
    }
    
    
    //Main Function
    public static void main(String[] args) {
        
        //Setting up frame and panel
        JFrame frame = new JFrame("Choose File");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        LayoutManager layout = new FlowLayout();  
        panel.setLayout(layout);      
        JButton button = new JButton("Select File");
        final JLabel label = new JLabel();

        //Setting up Wav File
        button.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 
                //Select File
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(frame);
                if(option == JFileChooser.APPROVE_OPTION){
                   File wavFile = fileChooser.getSelectedFile();
                   label.setText("File Selected: " + wavFile.getName());
                                   
                    try {
                            //Open the .wav file and get info
                            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile); 
                            AudioFormat audioFormat = audioInputStream.getFormat();
                            long audioDataLength = wavFile.length() - 44; 
                
                            // Create a byte array to store the audio data
                            int sampleNum = (int)(audioDataLength/(audioFormat.getSampleSizeInBits()/8));
                            byte[] audioDataBytes = new byte[(int)audioDataLength];
                            short[] samples = new short[sampleNum];
                            
                            // Read the audio data into the byte array
                            int numBytesRead = audioInputStream.read(audioDataBytes);
                            
                            //Create class instance
                            ReadWav myWav = new ReadWav(numBytesRead,audioFormat.getSampleRate(),audioFormat.getSampleSizeInBits(),audioFormat.getChannels(), audioFormat.getFrameSize(), audioFormat.getFrameRate(), sampleNum);
                            
                            //myWav.printInfo();
                        
                            //Bit shift to combine bytes into samples
                            if(myWav.sampleSizeBits == 16){
                                for(int i=0; i< audioDataLength/2; i++){
                                    byte byte1 = audioDataBytes[2*i]; 
                                    byte byte2 = audioDataBytes[(2*i)+1];
                    
                                    short combinedShort = (short)(((byte2 & 0xff) << 8) | (byte1 & 0xFF));
                                    samples[i] = combinedShort;
                                }   
                            }
                            
                            myWav.displayWaveforms(samples, 1350, 500);
                            //myWav.displayWaveforms(samples, 800, 400);
                            
                            audioInputStream.close();
                
                        } catch (UnsupportedAudioFileException | IOException ex) {
                            ex.printStackTrace();
                        }
                   
                }else{
                   label.setText("Open command canceled");
                }
            }
        });
        
        //Set the frame and panel
        panel.add(button);
        panel.add(label);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        
        frame.setSize(250, 400);      
        frame.setLocationRelativeTo(null);  
        frame.setVisible(true);

    }
}

//End of Code
