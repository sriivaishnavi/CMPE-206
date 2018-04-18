# CMPE-206
Distributed Denial of Service Attack
Built a master and slave Bot using java socket programming capable of generating distributed denial of service attacks commands from Master Bot which supports commands 
>list
>connect
> disconnect
> keepalive
> url where you can specify a url to which the client must issue a ramdom string. 
Commands
MasterBot.java
Compilation - javac MasterBot .java
Execution - java MasterBot -p portnumber
Connect command
connect (IPAddressOrHostNameOfYourSlave|all) (TargetHostName|IPAddress) TargetPortNumber [NumberOfConnections: 1 if not specified]
Disconnect Command
disconnect (IPAddressOrHostNameOfYourSlave|all) (TargetHostName|IPAddress) [TargetPort:all if no port specified]
Connect with Keepalive option
connect (IPAddressOrHostNameOfYourSlave|all) (TargetHostName|IPAddress) TargetPortNumber [NumberOfConnections: 1 if not specified] [keepalive]
Connect with url option
connect (IPAddressOrHostNameOfYourSlave|all) (TargetHostName|IPAddress) TargetPortNumber [NumberOfConnections: 1 if not specified] [url=path-to-be-provided-to-web-server]
SlaveBot.java
Compilation - javac SlaveBot.java
Execution - java SlaveBot -h hostnumber -p portnumber

