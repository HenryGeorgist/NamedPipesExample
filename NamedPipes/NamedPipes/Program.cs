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
			NamedPipeConnection npc = new NamedPipeConnection(recieverPipeName, senderPipeName);
		}

	}
}
