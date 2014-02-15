Stats Plugin for QSAdminGUI v2.0
--------------------------------

Stats Plugin is a simple plugin written for QSAdmin of QuickServer.
This will work with QuickServer v1.4.6 or above. 

The directory structure needed for getting plugin loaded is
given below 

<pre>
 +quickserver (installation dir)
   |
   +bin
   |
   +dist
   |
   +plugin
     |
     +Stats (folder to place all jars and plugin.xml files of Stats Plugin)
</pre>

When QsAdmin GUI is stated it will look into any directory in 
"plugin" folder for any plugin and load it.


The plugin currently shows the following information when connected to a server
 * Client Meter: No of clients currently connected.
 * Memory Status: Shows the amount of memory used/total/max in KB.
 * Thread Object Pool: Shows the number of client thread in pool.
 * Data Object Pool: Shows the number of ClientData objects in pool.
   (This may not come up if the ClientData used in the server is not poolable)


Other Libraries Used
---------------------
JFreeChart v 0.9.14 or later :
 A free Java class library for producing charts. License : GNU LGPL 
JCommon v 0.8.9 or later



License : GNU Lesser General Public License


Date : 15 Feb 2014
---------------------

Copyright (C) 2003-2014 QuickServer.org
http://www.quickserver.org
