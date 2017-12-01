P2PFileTransfer
Author: Zhangyang Wei

The P2PFileTransfer is a program that allows clients to transfer files between each other. The clients need to register the file to the broker, then the other clients can request file by getting the host information from the broker.

To run the P2PFileTransfer, a broker has to be up and running. Each client needs to run his peer.java by "java peer [brokerIPAddress]".

The commands for a client are "R" for registering a file; "U" for unregistering; B for browsing the registered files; S for searching for a file; E for exit. 

Currently the file to be registered has to exist in the folder and will be stored in the folder where the client runs the java program. 
