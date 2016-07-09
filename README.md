# BMT

Behavioral Monitoring Tool is software used to record and analyze behavioral experiments in neuroscience. it was developed in collaboration with neuropharmacology lab at Faculty of Medicine, Ain Shams University- Egypt. The software is planned to perform three types of experiments (Open field, Water maze and Forced swimming).
Open Field Module and Forced Swimming Module are currently supported.

Open Field Module is a test arena divided into a number of zones. a rat or mouse is put inside the arena. rat's movements are recorded and different parameters (number of zones, time spent in central zones, rearing behavior, distance travelled) are analyzed.

Forced Swimming Module is a test where a rat or mouse is put into a water container to analyze the struggle and despair times.

other types of experiments are planned for implementation in the next releases.

This software is Open Source, under the LGPL license.

Features:

for researchers:

1. Flexible and intelligent program to record lab data
2. Experiment analysis:<br>
---- At real time, from a connected webcam<br>
---- From a recorded video file
3. Exporting experiment's statistics to Excel sheet
4. Saving experiment to a video file

for developers:

1. Extensible architecture, on both the higher level (statisics) and the lower level (image processing)
2. Depends on open-source webcam libraries under (Windows,Mac and Linux)
3. GUI library used is IBM's SWT library

System Requirements:

Linux:
* Java runtime environment 32-bit*, v1.6 or higher
* Package libgstreamer0.10-dev

Windows:
* Java runtime environment 32-bit*, v1.6 or higher
* Microsoft Visual C++ 2008 SP1 Redistributabl

* in case of systems with Java runtime environment 64bit installed, use portable jre of 32bit to run the tool:
example: replace "java" in the run.bat file with the path of the portable java.exe.

Video frame-size currently supported is 640x480.

if you have any feedback, don't hesitate to contact me.
