using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NamedPipes.Implementations.Parsers
{
	class AEP_Parser : Abstractions.abstractParserParent
	{
		protected override bool handlesMessage(string message)
		{
			//determine if the message is appropriate for the AEP parser - this should probably be a prepended aep or something like that.
			if (message.Contains("Compute Event")) return true;
			return false;
		}

		protected override bool processMessage(string message)
		{
			//process the incoming message and return true or false.
			//this should probably be either "init" or "compute"
			
			if (!message.Contains("10")) return true;
			setValidation(false, "We hit the 10th event!");
			return false;
		}
	}
}
