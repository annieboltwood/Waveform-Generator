![Banner](https://github.com/annieboltwood/Waveform-Generator/blob/main/banner.png?raw=true)

# Waveform Generator
Takes input .wav file and displays single and multichannel waveforms. 

## Table of Contents

- [About](#about)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [How to Run](#How-to-Run)
  
## About

This project was created for my Multimedia Systems course at SFU. The goal was to create a program that could read a selected WAV audio file and display the visual representation of the sound waves to the user. The Java program works by reading the WAV files, organizing audio data into samples, and utilizes drawWaveform() to scale and draw waveforms on the screen.

## Features

- File Selector
- Single and Multichannel file reading
- Sample and Frequency Display

### Prerequisites

Before you begin, ensure you have the following installed on your machine:

- Java Development Kit (JDK) 17 or later
  - [Download JDK](https://adoptium.net/)

## How to Run

1. Clone the repository to your local machine:

    ```bash
    git clone https://github.com/annieboltwood/Waveform-Generator.git
    ```

2. Navigate to the Program directory:

    ```bash
    cd Program
    ```

3. Compile the Java file:

    ```bash
    javac ReadWav.java
    ```

4. Run the program:

    ```bash
    java ReadWav
    ```

5. Test Samples:
Provided are .wav test samples to see the Waveform output. This is located in the Test-Samples Directory.
    

