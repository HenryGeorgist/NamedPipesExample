using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NamedPipes.Implementations.Parsers
{
	class defaultParser : Abstractions.abstractParserParent
	{
		protected override bool handlesMessage(string message)
		{
			//always handle, this should be the last parser in the chain of responsibility
			return true;
		}
		protected override bool processMessage(string message)
		{
			//process the incoming message and return false, this is basically the default case in a switch... we did not understand the message.
			string filepath = System.IO.Path.GetTempPath() + "NamedPipes\\NamedPipes.txt";
			setValidation(true, "The default parser caught this message: " + message + " and wrote out message log to: " + filepath);
			using (System.IO.StreamWriter srOut = new System.IO.StreamWriter(filepath, true))
			{
				srOut.WriteLine(this.message);
				
			}
			return false;
		}
	}
}
