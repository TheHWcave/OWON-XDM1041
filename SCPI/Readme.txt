This is a list of all SCPI commands I have found to do something on the XDM1041 (well some should do something but but don't, hopefully OWON is going to fix the firmware to do that). 

And speaking of firmware. There is at least one firmware version 25052021 that has introduced an error into the SCPI. In that firmware any command that is not ending in "?" returns OK/nOK/nOK/n (the /n are new iine control chars). This nonsense return occurs even for illegal commands for example sending "hello world" to the XDM1041 results in OK/nOK/nOK/n 
