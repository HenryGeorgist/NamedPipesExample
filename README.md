# An Example of how to connect a java program to a .net program with named pipes

This example is intended to help people interested in creating WAT plugins without using
a COM interface or the JNI. 
This alternative method makes it easier for connecting a .net application that a user might not
want (or be able) to install into the registry.

There are two parts to this project; the java plugin and the c# namedpipes program.

## JAVA plugin

The java program is a computable, creatable plugin. This means an alternative can be created
and the program can be input into the program order in the WAT compute sequence.

The plugin passes a message with each event in an FRA compute (it will fail in a non FRA compute due to 
a failure to cast the compute options) that describes the event number and the lifecycle number. These messages
are sent via a named pipe. A response is returned that the message was recived via a separate named pipe. 

The recieved messages are echo'ed to the WAT message pane (as well as other information).

## .net named pipes 

The named pipe program listens on one pipe and replies on a different pipe. All messages that are recieved are
echoed to a temporary file at "C:\Temp\AEPTesting\Test.txt". This helps to show that the message was infact recieved.

# Issues 
I had trouble with the java.io.RandomAccessFile being able to flush to the recieving stream across the named pipe, I found
that closing the stream allowed for the memory to flush. So the weird closing and reopening of pipes is a result of that.
Changing to a java.nio.#### file stream would likely fix that design deficency.

I had trouble launching the NamedPipes.exe via the java plugin. The problem manifests as a message being missed by the .net
application after about 12 messages. If the NamedPipes.exe is launched from the visual studio ide or by launching it via a 
shortcut (which passes the appropriate initialization strings) the program seems to work pretty consistently. I do not know
why this behavor works this way.
