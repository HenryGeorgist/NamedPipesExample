using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NamedPipes
{
	class NamedPipeConnection
	{
		private string _recieverPipeName = "";
		private string _senderPipeName = "";
		public NamedPipeConnection(string reciever, string sender)
		{
			_recieverPipeName = reciever;
			_senderPipeName = sender;
		}
		public void openConnection()
		{
			string message = "";
			Boolean loop = true;
			while (loop)
			{

				//set up the reciever pipe - this will listen for compute messages from the java plugin.
				using (System.IO.Pipes.NamedPipeServerStream _pipeServer = new System.IO.Pipes.NamedPipeServerStream(_recieverPipeName, System.IO.Pipes.PipeDirection.InOut, 5))
				{
					Console.WriteLine("Named Pipe " + _recieverPipeName + " Started");
					Console.WriteLine("Waiting for Client To Connect to " + _recieverPipeName);
					//set up the broadcast pipe - this will send a message to the java plugin (preferably when the requested compute is complete)
					using (System.IO.Pipes.NamedPipeServerStream _senderPipeServer = new System.IO.Pipes.NamedPipeServerStream(_senderPipeName, System.IO.Pipes.PipeDirection.InOut, 5))
					{
						//establish valid links for the recieving and sending pipes.
						try
						{
							Console.WriteLine("Named Pipe " + _senderPipeName + " Started");
							Console.WriteLine("Waiting for Client To Connect to " + _senderPipeName);
							_pipeServer.WaitForConnection();
							Console.WriteLine("Client Connected to " + _recieverPipeName);
							_senderPipeServer.WaitForConnection();
							Console.WriteLine("Client Connected to " + _senderPipeName);
							//now that there are valid connections, establish stream readers and writers accordingly
							StreamReader sr = new StreamReader(_pipeServer);
							StreamWriter sw = new StreamWriter(_senderPipeServer);
							sw.AutoFlush = true;
							//this sr.ReadLine() will wait until a message has been sent from the java plugin.
							message = sr.ReadLine();
							//process the message 
							if (!Implementations.ParserFactory.ParserFactory.Instance.parse(message))
							{
								if (Implementations.ParserFactory.ParserFactory.Instance.isValid)
								{
									sw.WriteLine("Compute Successful!");
								}
								else
								{
									sw.WriteLine("Compute Failed! " + Implementations.ParserFactory.ParserFactory.Instance.message);
								}
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
