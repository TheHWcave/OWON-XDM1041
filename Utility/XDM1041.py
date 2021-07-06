#!/usr/bin/env python3
#MIT License
#
#Copyright (c) 2021 TheHWcave
#
#Permission is hereby granted, free of charge, to any person obtaining a copy
#of this software and associated documentation files (the "Software"), to deal
#in the Software without restriction, including without limitation the rights
#to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
#copies of the Software, and to permit persons to whom the Software is
#furnished to do so, subject to the following conditions:
#
#The above copyright notice and this permission notice shall be included in all
#copies or substantial portions of the Software.
#
#THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
#IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
#FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
#AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
#LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
#OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
#SOFTWARE.
#
import serial
import tkinter as tk
import tkinter.scrolledtext as tkst
import tkinter.filedialog as tkfd
import tkinter.messagebox as tkmb
import tkinter.font as tkFont
from collections import namedtuple

from time import sleep,localtime,strftime,perf_counter_ns
import math,argparse

class SCPI:
	"""
		Serial SCPI interface
	"""
	_SIF : None
	def __init__(self,port_dev=None,speed=9600,timeout=2):
		self._SIF = serial.Serial(
			port=port_dev,
			baudrate=speed,
			bytesize=8,
			parity='N',
			stopbits=1,
			timeout=timeout)
	def __del__(self):
		try: 
			self._SIF.close()
		except:
			pass	
	def readdata(self):
		"""
			read a SCPI response from the serial port terminated by CR LF
			any no-UTF8 characters are replaced by backslash-hex code
		"""
		buf = bytearray(0)
		n = 0
		while True:
			data = self._SIF.read(64)
			if len(data) > 0:
				buf.extend(data)
				if len(buf)>= 2:
					if buf[-2:] == b'\r\n':
						break
			else:
				n = n + 1
				if n > 2:
					buf = bytearray(0)
					break;
		
		#for b in buf: print(hex(b)+' ',end='')
		#print()
		res = buf.decode(errors="backslashreplace")
		x = res.find('\r\n') 
		if x == len(res)-2:
			res = res.strip()
		else:
			res = ''
		return res 				
	def sendcmd(self,msg,getdata=True):
		"""
			send a command over SCPI. If getdata is True, it waits for
			the response and returns it
		"""
		msg = msg+'\n'
		self._SIF.write(msg.encode('ascii'))
		if getdata:
			res = self.readdata()
		else:
			res = None
		return res

class XDM1041_GUI():
	
	
	def __init__(self,port):

		# create root window and frames
		self.window = tk.Tk()
		self.window.option_add('*Font','fixed')
		
		
		# give main loop a chance to run once before running (file) dialog
		# otherwise entry fields will loose focus and appear to hang
		self.window.update_idletasks()
		
		self.window.title("TheHWcave's XDM1041 utility")
		
		# the overall structure is aranged in 6 rows 
	    #  row	use
		#    0  port        
		#    1  id
		#    2  recording status monitor
		#    3  labels for values  
		#    4  values   
		#    5  recording control
		#    6  options      
		
		
		# row 0: port split into 3 columns
		#        (8)           (32)            (8)    = 48
		#         0              1              2
		#   0   label  <device name entry>    CONNECT
		#
		#
		self.portframe = tk.Frame(self.window)
		self.labelPort = tk.Label(self.portframe,width=8, text= 'port:')
		self.entryPort = tk.Entry(self.portframe, width=32)
		self.buttonConn  = tk.Button(self.portframe,width=8,text='Connect',bd=5,command=self.DoConnect)
		self.entryPort.bind('<Return>',self.DoConnect)
		self.entryPort.insert(0,port)
		self.labelPort.grid(row=0,column=0,sticky='E')
		self.entryPort.grid(row=0,column=1,sticky='W')
		self.buttonConn.grid(row=0,column=2,sticky='W') 
		self.portframe.grid(row=0,column=0,columnspan=3)
		
		# row 1: id split into 2 columns
		#  (8)                  (40)               = 48
		#   0                     1              
		#  time             id (as reported)
		#
		#
		self.idframe = tk.Frame(self.window)
		self.labeltime = tk.Label(self.idframe,width=8, text= '',relief='sunken')
		self.labelId   = tk.Label(self.idframe,width=40, text= '',relief='sunken')
		self.labeltime.grid(row=0,column=0,sticky='E')
		self.labelId.grid(row=0,column=1,sticky='E')
		self.idframe.grid(row=1,column=0,columnspan=2)
		
		# row 3: rec status monitor split into 2 columns
		# 
		#        (8)           (40)                         = 48
		#         0              1   
		#   0   #recs         <filename>   
		#
		#
		
		
		self.recmon = tk.Frame(self.window)
		self.RecName   = ''
		self.labelRNums= tk.Label(self.recmon,text= '',width=8,relief='sunken')
		self.labelRecFn= tk.Label(self.recmon,text= '{:24s}'.format(self.RecName),width=40,relief='sunken')
		self.labelRNums.grid(row=0,column=0,sticky='W')
		self.labelRecFn.grid(row=0,column=1,sticky='E')
		self.recmon.grid(row=2,column=0,columnspan=2)
		
		
		# row 4: labels split into 5 columns
		#   (10)     (9)     10)    (9)     (10)    = 48
		#     0       1       3      4       5 
		#   range   fu1    meas1    fu2     meas2
		#
		#
		self.labelframe = tk.Frame(self.window)
		
		self.labelRange = tk.Label(self.labelframe,width=10, text= 'Range')
		self.labelFu1   = tk.Label(self.labelframe,width= 9, text= 'Func1')
		self.labelMeas1 = tk.Label(self.labelframe,width=10, text= 'Meas1')
		self.labelFu2   = tk.Label(self.labelframe,width= 9, text= 'Func2')
		self.labelMeas2 = tk.Label(self.labelframe,width=10, text= 'Meas2')
		self.labelRange.grid(row=0,column=0,sticky='W') 
		self.labelFu1.grid  (row=0,column=1,sticky='W') 
		self.labelMeas1.grid(row=0,column=2,sticky='W') 
		self.labelFu2.grid  (row=0,column=3,sticky='W') 
		self.labelMeas2.grid(row=0,column=4,sticky='W') 
		self.labelframe.grid(row=3,column=0,columnspan=5)
		
		# row 5: values split into 5 columns
		#   (10)     (9)     10)    (9)     (10)    = 48
		#     0       1       3      4       5 
		#   range   fu1    meas1    fu2     meas2
		#
		#
		self.valueframe = tk.Frame(self.window)
		
		self.valueRange = tk.Label(self.valueframe,width=10, text= '',relief='sunken')
		self.valueFu1   = tk.Label(self.valueframe,width= 9, text= '',relief='sunken')
		self.valueMeas1 = tk.Label(self.valueframe,width=10, text= '',relief='sunken')
		self.valueFu2   = tk.Label(self.valueframe,width= 9, text= '',relief='sunken')
		self.valueMeas2 = tk.Label(self.valueframe,width=10, text= '',relief='sunken')
		self.valueRange.grid(row=0,column=0,sticky='W') 
		self.valueFu1.grid  (row=0,column=1,sticky='W') 
		self.valueMeas1.grid(row=0,column=2,sticky='W') 
		self.valueFu2.grid  (row=0,column=3,sticky='W') 
		self.valueMeas2.grid(row=0,column=4,sticky='W') 
		self.valueframe.grid(row=4,column=0,columnspan=5)
		
		self.Range	  =''
		self.Fu1	  =''
		self.Fu1Old	  =''
		self.Meas1	  = 0
		self.Fu2	  =''
		self.Meas2	  = 0
		self.Auto     =''
		
		
		# row 5: rec control split into 4 columns
		# 
		#       (10) (12)  (12)  (14)                   = 48
		#         0   1     2     3               
		#   0   Aspd AREC  MREC   Man   
		#
		#
		self.recframe = tk.Frame(self.window)
		self.RecSpdList= ('1s','2s','5s','10s','30s','60s','5m','10m','30m','1h')
		self.RecSpdSec = (  1  , 2 ,  5  , 10  , 30  ,60  ,300  ,600  ,1800 ,3600)
		self.RecSpd    = 1
		self.RecNums   = 0
		self.RecSpdVal = tk.StringVar()
		self.RecSpdVal.set(self.RecSpdList[0])
		self.optRecSpd = tk.OptionMenu(self.recframe,self.RecSpdVal,*self.RecSpdList,command=self.DoRecSpd)

		self.buttonARec = tk.Button(self.recframe,text='Auto Rec',bd=5,command=self.DoARec,width=7)
		self.buttonMRec = tk.Button(self.recframe,text='Man Rec',bd=5,command=self.DoMRec,width=8)
		self.buttonMan  = tk.Button(self.recframe,text='RECORD THIS',bd=5,command=self.DoMan,width=12)
		
		
		self.optRecSpd.grid (row=0,column=0,sticky='W')
		self.buttonARec.grid(row=0,column=1,sticky='W')
		self.buttonMRec.grid(row=0,column=2,sticky='W')
		self.buttonMan.grid (row=0,column=3,sticky='W')
		self.recframe.grid(row=5,column=0,columnspan=4)

		self.MRec_On  = False
		self.Man_On   = False
		self.ARec_On  = False
	
		
		# row 6 options 
		# 
		#        (8)      (10)   (10)   (10)   (10)        = 48
		#         0        1      2      3       4         
		#   0   PT100Unit PT100  
		#
		#
		self.optframe = tk.Frame(self.window)
		
		self.PT100UnitList= ('C','F','K')
		self.PT100UnitVal = tk.StringVar()
		self.PT100UnitVal.set(self.PT100UnitList[0])
		self.optPT100Unit = tk.OptionMenu(self.optframe,self.PT100UnitVal,*self.PT100UnitList,command=self.DoPT100Unit)
		self.buttonPT100 = tk.Button(self.optframe,text='PT100',bd=5,command=self.DoPT100,width=5)
		
		self.optPT100Unit.grid (row=0,column=0,sticky='W')
		self.buttonPT100.grid(row=0,column=1,sticky='W')
		
		self.optframe.grid(row=6,column=0,columnspan=2)
		
		
		self.PT100_On  = False
		self.PT100_Unit= self.PT100UnitList[0]
	
		
		
		# remaining intitalisation and start of main loop
		
		self.MiniBM = None
		self.id = ''
		self.entryPort.focus_set()
		self.PollCount = 0
		self.ProgStart = perf_counter_ns()
		self.PollMiniBM()
		tk.mainloop()
		if self.RecName != '':
			self.f.close()
	
	def GetResponse(self,Cmd,Numeric = False):
		"""
			sends a command (that will trigger a response) and returns
			that response
			
			Because of bugs in the XDM1041, it may sometimes timeout and 
			sometimes return multiple responses. For timeouts, a "?" is 
			returned. Multiple responses are discarded. 
			
			It also translates some of the weird characters send by the
			XDM1041 for non-ASCII chars 
		"""
		Successful = False
		n =0
		while not Successful:
			s = self.MiniBM.sendcmd(Cmd)
			if s != '':
				if Numeric:
					try:
						v = float(s)
						Successful = True
						Res = v
					except ValueError:
						Successful = True
						Res = 0
				else:
					Successful = True
					if s.endswith('\\xa6\\xb8'): 
						s = s[:-8] +'Ohm'
					elif s.endswith('\\xa6\\xccF'): 
						s = s[:-9] +'uF'
					elif s.endswith('"'):
						s = s.strip('"')
					Res = s
			else:
				#print('nothing'+str(n))
				sleep(0.1)
				n = n + 1
				if n > 5:
					if Numeric:
						Res = 0
					else:
						Res = '?'
					break
		#print(Cmd+str(Res))
		return Res
	
	def PrettyFloat(self,v):
		"""                  
			A crude but functional formatter that shows floating
			points in engineering format
			
			Input                result
		                         1234567890
		                         
		   +1234567800        to +1.23456E9
		    +123456780        to +123.456E6
		    +12345678         to +12.3456E6    
		    +1234567.8        to +1.23456E6
		    +123456.78        to +123.456E3  
		    +12345.678        to +12.3456E3
		    +1234.5678        to +1.23456E3
		    +123.45678        to   +123.456
		    +12.345678        to   +12.3456
		    +1.2345678        to   +1.23456
		    +0.12345678       to +123.45E-3
		    +0.012345678      to +12.345E-3
		    +0.0012345678     to +1.2345E-3
		    +0.00012345678    to +123.45E-6
		    +0.000012345678   to +12.345E-6
		    +0.0000012345678  to +1.2345E-6
		    +0.00000012345678 to +123.45E-9
		    +0.00000001234567 to +12.345E-9
		    +0.00000000123456 to +1.2345E-9
			
		"""
		av = abs(v)
		if av < 1:
			v = v *1000
			if   av >= 1E-1 :vs = '{:+7.2f}E-3'.format(v)
			elif av >= 1E-2 :vs = '{:+7.3f}E-3'.format(v)
			elif av >= 1E-3 :vs = '{:+7.4f}E-3'.format(v)
			else:
				v = v *1000
				if   av >= 1E-4 :vs = '{:+7.2f}E-6'.format(v)
				elif av >= 1E-5 :vs = '{:+7.3f}E-6'.format(v)
				elif av >= 1E-6 :vs = '{:+7.4f}E-6'.format(v)
				else:
					v = v *1000
					if   av >= 1E-7 :vs = '{:+7.2f}E-9'.format(v)
					elif av >= 1E-8 :vs = '{:+7.3f}E-9'.format(v)
					else:
						vs = '{:+7.4f}E-9'.format(v)
		else:
			if   av < 1E1   :  vs = ' {:+8.4f}'.format(v)
			elif av < 1E2   :  vs = ' {:+8.3f}'.format(v)
			elif av < 1E3   :  vs = ' {:+8.2f}'.format(v)
			else:
				v = v / 1000
				if   av < 1E4   :  vs = '{:+8.5f}E3'.format(v)
				elif av < 1E5   :  vs = '{:+8.4f}E3'.format(v)
				elif av < 1E6   :  vs = '{:+8.3f}E3'.format(v)
				else:
					v = v / 1000
					if   av < 1E7   :  vs = '{:+8.5f}E6'.format(v)
					elif av < 1E8   :  vs = '{:+8.4f}E6'.format(v)
					elif av < 1E9	:  vs = '{:+8.3f}E6'.format(v)
					else:
						v = v / 1000
						vs = '{:+8.5f}E9'.format(v)
		return vs
			
	
	def DoConnect(self,event=None):
		"""
			given a port name, the function tries to connect.  
						
			Note that once it connects successfully. a subsequent 
			disconnect terminate the program
			
		"""
		port = self.entryPort.get()
		if self.MiniBM == None:
			
			try:
				self.MiniBM = SCPI(port,speed=115200,timeout=0.1)
				
			
				self.id = self.GetResponse('*IDN?')
				if self.id == '':
					self.MiniBM = None
					tkmb.showerror("device error","device at "+port+" does not respond")
				else:
					self.buttonConn.config(relief='sunken')
					self.labelId.config(text= self.id)
			except: 
				tkmb.showerror("port error","can't open "+port)
				self.MiniBM = None
				self.buttonConn.config(relief='raised')
				self.labelId.config(text= '')
				
		else:
			self.window.destroy()
			
	
	def DoRecSpd(self,event=None):
		"""
			changes the recording speed
		"""
		idx = self.RecSpdList.index(self.RecSpdVal.get())
		self.RecSpd = self.RecSpdSec[idx]
		
	def DoMRec(self,event=None):
		"""
			Turns the manual rec mode on or off. 
		"""
		self.MRec_On = not self.MRec_On
		if self.MRec_On:
			self.buttonMRec.config(relief='sunken')
			self.ARec_On = False
			self.buttonARec.config(relief='raised')
			self.buttonMan.focus_set()
		else:
			self.buttonMRec.config(relief='raised')
			self.entryPort.focus_set()
		self.DoRec()
		
	def DoARec(self,event=None):
		"""
			Turns the auto rec mode on or off. 
		"""
		self.ARec_On = not self.ARec_On
		if self.ARec_On:
			self.buttonARec.config(relief='sunken')
			self.MRec_On = False
			self.buttonMRec.config(relief='raised')
			self.entryPort.focus_set()
		else:
			self.buttonARec.config(relief='raised')
		self.DoRec()
		
	
	
	def DoMan(self,event=None):
		"""
			does a single manual recording  
		"""
		if self.MRec_On:
			self.Man_On = not self.Man_On
			if self.Man_On:
				self.buttonMan.config(relief='sunken')
			else:
				self.buttonMan.config(relief='raised')
	
	def DoPT100Unit(self,event=None):
		"""
			changes the unit for PT100
		"""
		self.PT100_Unit= self.PT100UnitVal.get()
		
		

	def DoPT100(self,event=None):
		"""
			enables PT100 mode. i.e. we convert a 100+ ohm value into 
			a temperature
		"""
		if self.Fu1.upper() == 'RES' and self.Range.upper() == '500 OHM':
			self.PT100_On = not self.PT100_On
			if self.PT100_On:
				self.buttonPT100.config(relief='sunken')
			else:
				self.buttonPT100.config(relief='raised')
		else:
			tkmb.showinfo('info','switch to 500 Ohm RES mode with REL to compensate for wire res.')
			
				
	def DoRec(self):
		"""
			starts or stops the recording and shows the 
			recording filename while recording is on. 
		"""
		def StartNewFile():
			if self.ARec_On: 
				self.RecName ='AREC'
			else:
				self.RecName ='MREC'
			
			self.RecName = self.RecName + '_'+strftime('%Y%m%d%H%M%S',localtime())+'.csv'
				
			try:
				self.f = open(self.RecName,'w')
				if self.ARec_On:
					self.f.write('Time[S],')
				else:
					self.f.write('Count,')
				self.f.write('Range,Func1,Meas1,Func2,Meas2\n')
				self.PollCount = 0
				self.RecNums = 0
				self.labelRNums.config(text= '#{:7n}'.format(self.RecNums))
			except:
				
				tkmb.showerror("rec error","can't create "+self.RecName)
				self.RecName = ''
				self.labelRNums.config(text= '')
				self.buttonMRec.config(relief='raised')
				self.buttonARec.config(relief='raised')
				self.ARec_On = False
				self.MRec_On = False
				
			return
				
		if self.MiniBM != None:
			if self.RecName == '':
				StartNewFile()
			else:
				if self.ARec_On or self.MRec_On:
					self.f.close()
					StartNewFile()
				else:
					self.f.close()
					self.RecName = ''
					self.labelRNums.config(text= '')
			self.labelRecFn.config(text= '{:24s}'.format(self.RecName))
		
			
	def PollMiniBM(self,event=None):
		"""
			polls the meter every 1s. The time is adjusted to maintain accuracy
		"""
		
		def WriteRec(n,m2):
			"""
				write a record to the recording file
			"""
			self.f.write('{:5n},{:10s},{:10s},{:10s},{:10s},{:10s}\n'.format(
				n,
				self.Auto + ':'+self.Range,
				self.Fu1,
				str(self.Meas1),
				self.Fu2,
				m2))
			self.RecNums += 1
			
		def PT100(ohm,vnull=0.0):
			"""
				convert a resistance reading of a standard PT100 probe to
				a temperature in celsius. The resistance must be >=100 Ohm
			"""
			TC = 0.00385
			A  = 3.9083E-03
			B  = 5.775E-07
			C  =-4.183E-12
			R0 = 100.0
			return (-A + math.sqrt(A*A - 4*B*(1-(ohm-vnull)/R0)))/(2*B)
	
		if self.MiniBM != None:
			self.PollCount += 1
			self.labeltime.config(text= '{:8n}'.format(self.PollCount)) 
			err = False
			try:
				
				self.Meas1 = self.GetResponse('MEAS1?',Numeric=True)
				self.Auto = 'A' if self.GetResponse('AUTO?') =='1' else 'M'
				self.Fu1 = self.GetResponse('FUNC1?')
				if (self.Fu1.upper() == 'DIOD' or self.Fu1.upper() == 'CONT'):
					self.Range = ''
				else:
					self.Range = self.GetResponse('RANGE?')
					
						
				if self.Fu1.upper().endswith('AC'):
					self.Fu2 = self.GetResponse('FUNC2?')
					if self.Fu2.upper() == 'NONE': 
						self.Fu2   = ''
					else:
						self.Meas2 = self.GetResponse('MEAS2?',Numeric=True)
						#
						# bug fix: the XDM1041 scales the frequency wrongly
						#
						if self.Range.endswith('mV'): 
							self.Meas2  = self.Meas2 * 1000
						elif self.Range.endswith('uA'): 
							self.Meas2  = self.Meas2 * 1000000
						elif self.Range.endswith('mA'): 
							self.Meas2  = self.Meas2 * 1000
				else:
					self.Fu2 =''
					self.Meas2 =0
						
			
				
				
					
				if self.Fu1Old.upper() == 'RES' and self.Fu1.upper() != 'RES':
					self.PT100_On = False
					self.buttonPT100.config(relief='raised')
					
		
					
				self.Fu1Old = self.Fu1
				
			except:
				err = True
				raise
			if err:
				tkmb.showerror("comms error","lost connection ")
				self.window.quit()
			else:
				if self.PT100_On:
					if self.Range.upper() == '500 OHM':
						if (self.Meas1 >=100) and (self.Meas1 < 550):
							self.Fu2 = 'PT100'
							self.Meas2 = PT100(self.Meas1)
							if self.PT100_Unit == 'F': self.Meas2 = 32+ self.Meas2 * (9/5)
							elif self.PT100_Unit == 'K': self.Meas2 = 273.15+ self.Meas2 
								
						else:
							tkmb.showinfo('info','resistance out of range for PT100')
							self.PT100_On = False
							self.buttonPT100.config(relief='raised')

					else:
						tkmb.showinfo('info','must be in 500 Ohm range to use PT100')
						self.PT100_On = False
						self.buttonPT100.config(relief='raised')

				
					
							
				self.valueRange.config(text='{:8s}'.format(self.Auto+':'+self.Range))
				self.valueFu1.config(text='{:8s}'.format(self.Fu1))
				self.valueMeas1.config(text=self.PrettyFloat(self.Meas1))
				self.valueFu2.config(text='{:8s}'.format(self.Fu2))
				m2 = ''
				if self.Fu2 != '':
					m2 = self.PrettyFloat(self.Meas2)
				self.valueFu2.config(text='{:8s}'.format(self.Fu2))
				self.valueMeas2.config(text=m2)
			
					
				
				if self.RecName != '':
					if self.ARec_On:
						if self.PollCount % self.RecSpd == 0:
							WriteRec(self.PollCount,m2)
					elif self.MRec_On:
						if self.Man_On:
							WriteRec(self.RecNums+1,m2)
							self.Man_On = False
							self.buttonMan.config(relief='raised')
					
						
							
					self.labelRNums.config(text= '{:8n}'.format(self.RecNums))
							
				
							
		elapsed = (perf_counter_ns() - self.ProgStart)//1000000  # time in ms since start
		time2sleep= 1000 - (elapsed % 1000)
		self.window.after(time2sleep, self.PollMiniBM)
			

if __name__ == "__main__":
	
	parser = argparse.ArgumentParser()

	parser.add_argument('--port',help='port ',
					action='store',type=str,default='')
	
					
	
	arg = parser.parse_args()
	
	gui = XDM1041_GUI(arg.port)

