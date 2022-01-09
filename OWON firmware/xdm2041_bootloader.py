#!/usr/bin/python3

'''Owon XDM2041 bootloader script.

This script can be used to upgrade XDM2041 firmware.
It *SHOULD* work without damaging calibration, but no guarantees are given.
'''

import sys
import time
import serial
import math
import struct

if len(sys.argv) < 3:
    sys.stderr.write("Usage: bootloader.py <serialport> <filename.bin>\n")
    sys.exit(1)

port = serial.Serial(sys.argv[1], 115200, timeout = 0.1)
bindata = open(sys.argv[2], 'rb').read()

print("Trying to enter bootloader. Restart XDM2041 now.")
while True:
    sys.stdout.write('.')
    sys.stdout.flush()
    
    port.write(b'start')
    time.sleep(0.1)
    if b'7' in port.read(1024):
        break

print("Bootloader active!")
port.timeout = 10.0

packsize = 10240
packcount = math.ceil(len(bindata) / float(packsize))
print("Will send %d packs of 10kB to area 5" % packcount)
print("Erasing flash..")

port.write(b'\x05')
if b'7' not in port.read(16):
    sys.stderr.write("ERROR: No acknowledge to write area\n")
    sys.exit(2)

port.write(struct.pack('b', packcount))
if b'7' not in port.read(16):
    sys.stderr.write("ERROR: No acknowledge to pack count\n")
    sys.exit(3)

for pack in range(packcount):
    print("Writing pack %d/%d" % (pack + 1, packcount))
    
    part = bindata[pack * packsize : (pack + 1) * packsize]
    port.write(part)
    
    if b'7' not in port.read(16):
        sys.stderr.write("ERROR: No acknowledge to pack write %d\n" % (pack + 1))
        sys.exit(4)

print("Exiting bootloader..")
port.write(b'\x14')

port.timeout=1.0
port.read()
time.sleep(10)
port.write(b'*IDN?\r\n')
response = port.read(1024)

print("All done, SCPI IDN response: " + repr(response))

