# Openhab
OpenHab Project for Autonomus Systems.
It is an example of implementation of the MAPE-K loop for autonomous system. It is just a simulation but it is very easy to put it on real devices (if these support Java).
Users can control house via OpenHAB. Data are generated by a program in Java; another component, Analysis, checks this information and, by communicating with the component Decision, which is implemented with OpenHAB rules (Xtend language), it is used to take decisions. Decisions are passed to a simulated actuator (Execution). Data are then virtually sensed again.

#Notes
Please notice that version 1.0.0 was tested with Windows 10 64 bit, OpenHAB 1.8.3 (but it should be compatible with OpenHAB 2.0), Mosquitto (configure it with "localhost" and port 1883), MySQL Ver 15.1 Distrib 10.1.21-MariaDB, for Win32 (AMD64).<br/>
For installation, you will need to download OpenHAB 1.8.3 (available on the official website www.openhab.org) and the JARs+database available here https://www.dropbox.com/s/kkzno8r2prw7wqz/JARs%2Bdatabase_v1.0.0.7z?dl=0 . Of course, you can generate the executable JARs by using the source codes of Analysis, Monitoring, Sensor, Execution components available on this repository, but the database (file smarthome.sql) is still required and it is available in the link above. Moreover, once you download OpenHAB 1.8.3, you should replace the "configuration" folder with the one provided in this repository (actually, only Items, Sitemap and Rules folders are to be replaced; other files are 0K).

#Documentation and installation
Available here https://www.dropbox.com/s/k5z0zxe8egro427/Report_AgostinoMascitti_KelwinPayares.pdf?dl=0

#Contributors
HerrAugust - agostino_aless@yahoo.it<br/>
Kelwinpa - https://github.com/kelwinpa 
