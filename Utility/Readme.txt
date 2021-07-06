The utiltity uses Python3.8 or newer. The software has been tested on Linux and Windows 7.

If Python is installed properly on Windows you should be able to simply double-click on the .py file. But you can also start it from a command prompt in which case you can add the --port [PORT] as a parameter 

The PORT is COMx under Windows or /dev/ttyUSBx under Linux and is the port the XDM1041 is connected to. 
This utility assumes the default comms settings in the XDM1041 are in use: 

Baud 115200
Party: none
Stop bits: 1
Data bits: 8  

Click CONNECT to connect to the XDM1041 and you should see the meters ID string and firmware version displayed. 

The utility displays the range, prefixed by M or A depending on whether manual or auto ranging is on,
the function e.g. VOLT and the measured value in engineering format. 
If a dual display is enabled on the XDM1041 it shows the 2nd function (Freq) and the value

With AUTO REC you can turn on recording in CSV format. The interval is selectable between 1 record per second to 1 per hour
MAN REC turns manual recording on. It records the current values every time RECORD THIS is clicked or the space bar is pressed. 

If you have a PT100 sensor, you can use it to measure and record temperature. Use RESISTANCE mode to compensate (REL) for the wire 
resistance of the PT100 probe, then connect the probe so its true resistance is shown. Then select PT100 in the utility to show the 
equivalent temperature selectable is Celsius, Fahrenheit or Kelvin as the 2nd function. 
Note that temperature of the probe must be >=0 deg.C 
