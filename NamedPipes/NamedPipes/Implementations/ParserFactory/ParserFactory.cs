using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NamedPipes.Implementations.ParserFactory
{
	sealed class ParserFactory : Interfaces.iParse, Interfaces.iValidate
	{
		private Abstractions.abstractParserParent _chain;// chain of responsibility.
		private bool _isValid;
		private string _message;
		private static readonly ParserFactory _factory = new ParserFactory();
		public static ParserFactory Instance { get { return _factory; } }
		public bool isValid { get { return _isValid; } }
		public string message { get { return _message; } }

		private ParserFactory() {
			//reflect to get all parsers and add them to a list.
			_chain = new Implementations.Parsers.defaultParser();//make sure default parser is at the end of the chain!
			foreach (Type type in System.Reflection.Assembly.GetAssembly(typeof(Abstractions.abstractParserParent)).GetTypes().Where(myType => myType.IsClass && !myType.IsAbstract && myType.IsSubclassOf(typeof(Abstractions.abstractParserParent))))
			{
				if (type != typeof(Implementations.Parsers.defaultParser))//already added, so skip it.
				{
					_chain = _chain.appendParser((Abstractions.abstractParserParent)Activator.CreateInstance(type));
				}
			}
		}
		public bool parse(string message)
		{
			_chain.clearValidation();
			if (_chain.parse(message))
			{
				return true;
			}
			else
			{
				setValidation(_chain.isValid, _chain.message);
				return false;
			}
		}
		private void setValidation(bool validState, string msg)
		{
			_isValid = validState;
			_message = msg;
		}
		private void clearValidation()
		{
			_isValid = true;
			_message = "";
		}
	}
}
