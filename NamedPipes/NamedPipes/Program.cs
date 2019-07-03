using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NamedPipes
{
	class Program
	{
		static void Main(string[] args)
		{
			string recieverPipeName = "";
			string senderPipeName = "";
			if (args.Length != 3)
			{
				throw new ArgumentException("expected argument count of 3, found the length to be " + args.Length);
			}
			if (args[0] == "-pipeNames")
			{
				recieverPipeName = args[1];
				senderPipeName = args[2];
			}
			else
			{
				throw new ArgumentException("expected argument: -pipeNames, was not found");
			}
			string message = "";
			Boolean loop = true;
			while (loop) {

				//set up the reciever pipe - this will listen for compute messages from the java plugin.
				using (System.IO.Pipes.NamedPipeServerStream _pipeServer = new System.IO.Pipes.NamedPipeServerStream(recieverPipeName, System.IO.Pipes.PipeDirection.InOut, 5))
				{
					Console.WriteLine("Named Pipe "+ recieverPipeName +" Started");
					Console.WriteLine("Waiting for Client To Connect to " + recieverPipeName);
					//set up the broadcast pipe - this will send a message to the java plugin (preferably when the requested compute is complete)
					using (System.IO.Pipes.NamedPipeServerStream _senderPipeServer = new System.IO.Pipes.NamedPipeServerStream(senderPipeName, System.IO.Pipes.PipeDirection.InOut, 5))
					{
						//establish valid links for the recieving and sending pipes.
						try
						{
							Console.WriteLine("Named Pipe " + senderPipeName + " Started");
							Console.WriteLine("Waiting for Client To Connect to " + senderPipeName);
							_pipeServer.WaitForConnection();
							Console.WriteLine("Client Connected to " + recieverPipeName);
							_senderPipeServer.WaitForConnection();
							Console.WriteLine("Client Connected to " + senderPipeName);
							//now that there are valid connections, establish stream readers and writers accordingly
							StreamReader sr = new StreamReader(_pipeServer);
							StreamWriter sw = new StreamWriter(_senderPipeServer);
							sw.AutoFlush = true;
							//this sr.ReadLine() will wait until a message has been sent from the java plugin.
							message = sr.ReadLine();
							//process the message (in this example I am creating a temporary file to test the message was recieved and actionable.)
							using(StreamWriter srOut = new StreamWriter("C:\\Temp\\AEPTesting\\Test.txt", true)){
								Console.WriteLine("Message Recieved: " + message);
								srOut.WriteLine("Message Recieved: " + message);
								System.Threading.Thread.Sleep(5);
							}
							//ensure that the response 
							sw.WriteLine("Message Recieved: " + message);
						}
						catch (Exception ex)
						{
							try
							{
								_senderPipeServer.Close();
							}
							catch (Exception e1)
							{
							}
							try
							{
								_pipeServer.Close();
							}
							catch (Exception e2)
							{
							}
							Console.WriteLine(ex.InnerException);
						}

					}
				}
				if (message == "Exit") loop = false;
			}
		}

	}
}
