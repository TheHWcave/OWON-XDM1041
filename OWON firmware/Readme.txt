I have got permission from OWON to share the firmware XDM1041_OS_V2.1.2_20210525.bin for the XDM1041 meter.
This software runs on my meter reasonably well (you can see the process of upgrading in my Youtube video https://youtu.be/ULxfLKsuCSs) . According to OWON there are no other updates at the moment. But I know there are still issues with it, mainly:
on SCPI:
-  you can get it into a mode where it responds to any command with OK/nOK/nOK/n (the /n being the newline control char) 
-  in the AC functions, when DUAL and FREQ is selected, the frequency as reported by MEAS2? is wrongly scaled in accordance with the range of the voltage or current. For example in 500mV AC, a 50 Hz frequency is reported as 0.05 Hz
-  if TEMP:RTD:SHOW MEAS is sent, the temperature value returned by MEAS? or MEAS1? is no longer updated, i.e. the meter always sends the same value, regardless of the actual temperature
Capacitor mode AUTO RANGING is very slow until about 500uF. From then on  it stops working completely. For example for 1000uF, the meter never settles on a range and sometimes some strange (Chinese?) characters can be seen momentarily glitching on the display. Switching to manual range works fine and capacitors can be measured ok.

The firmware loader program from OWON is too big to be send per email
The x00 to x14 files are binary chunks which can be reassembled to form a ZIP file which contains the Windows loader program for the XDM12041 firmware which is also included (V2.1.2). To reassemble on a Windows machine you need to use the command line (DOS prompt). Go to the directory where you stored the 15 files and use:

copy/b x* update.zip

One more thing: This is OWON's software and if you use it and brick your XDM1041, don't blame me. Upgrading the firmware is entierly your own risk
