I have got permission from OWON to share this ZIP file which contains firmware XDM1041_OS_V2.1.2_20210525.bin and the program to load it into the XDM1041 meter.
This software runs on my meter reasonably well (you can see the process of upgrading in my Youtube video https://youtu.be/ULxfLKsuCSs) . According to OWON there are no other updates at the moment. But I know there are still issues with it, mainly:
on SCPI:
-  you can get it into a mode where it responds to any command with OK/nOK/nOK/n (the /n being the newline control char) 
-  in the AC functions, when DUAL and FREQ is selected, the frequency as reported by MEAS2? is wrongly scaled in accordance with the range of the voltage or current. For example in 500mV AC, a 50 Hz frequency is reported as 0.05 Hz
-  if TEMP:RTD:SHOW MEAS is sent, the temperature value returned by MEAS? or MEAS1? is no longer updated, i.e. the meter always sends the same value, regardless of the actual temperature
Capacitor mode AUTO RANGING is very slow until about 500uF. From then on  it stops working completely. For example for 1000uF, the meter never settles on a range and sometimes some strange (Chinese?) characters can be seen momentarily glitching on the display. Switching to manual range works fine and capacitors can be measured ok.

One more thing: This is OWON's software and if you use it and brick your XDM1041, don't blame me. Upgrading the firmware is entierly your own risk
