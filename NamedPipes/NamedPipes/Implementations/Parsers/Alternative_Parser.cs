using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NamedPipes.Implementations.Parsers
{
	class Alternative_Parser : Abstractions.abstractParserParent
	{
		protected override bool handlesMessage(string message)
		{
			//-alt -init,sender:XXXXXX,reciever:XXXXXX,altName:XXXX
			if (message.Substring(0, 4) == "-alt") return true;
			return false;
		}

		protected override bool processMessage(string message)
		{
			//strip -alt
			string subMessage = message.Substring(5, message.Length - 5);
			// if the message is "-init" create a new pipe?
			if (subMessage.Substring(0, 5) == "-init")
			{
				//gather sender and reciever pipe names
				//-init,sender:XXXXXX,reciever:XXXXXX,altName:XXXX
				string[] parts = subMessage.Split(new string[] { "," },StringSplitOptions.None);
				if (parts.Length != 4)
				{
					//message that this is invalid..
					setValidation(false, "init argument was not 4 members long");
					return false;
				}
				string sender = "";
				string reciever = "";
				string altName = "";
				if (parts[1].Substring(0, 7) == "sender:")
				{
					sender = parts[1].Substring(7, parts[1].Length - 7);
					if (parts[2].Substring(0, 9) == "reciever:")
					{
						reciever = parts[2].Substring(9, parts[2].Length - 9);
						if (parts[3].Substring(0, 8) == "altName:")
						{
							altName = parts[3].Substring(8, parts[3].Length - 8);
						}
						else
						{
							setValidation(false, "the third argument was not reciever:");
							return false;
						}
					}
					else
					{
						setValidation(false, "the third argument was not reciever:");
						return false;
					}
				}
				else
				{
					setValidation(false, "the second argument was not sender:");
					return false;
				}
				//need to jump the thread - otherwize this will halt all communication.
				//create a named pipe based on the arguments gathered for the alternative that has been loaded, but do it on a separate thread.
				return true;
			}
			else
			{
				throw new NotImplementedException();
			}
			
		}
	}
}
