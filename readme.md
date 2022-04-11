# Remote Shutdown Tray

## Acknowledges
This project is based on the 'RemoteShutdownPCServer' by Isah Rikovic (rikovicisah @ gmail.com)

Original JAR file found @ https://github.com/rikovicisah

I decompiled his code and used some portions of `NetListener` and `CMDWriter` wrapped in a tray application. 
So this application is fully compatible with the _Remote Shutdown PC_ Android App.

## Features
Runs in the OS' system tray. No ugly cmd/shell window.<br>
Tray icon's right click command `Show computer IP(s)` to view all (non-loopback) interfaces' IPs<br>
Can be installed easily as a start-up application both in Windows and Linux.

