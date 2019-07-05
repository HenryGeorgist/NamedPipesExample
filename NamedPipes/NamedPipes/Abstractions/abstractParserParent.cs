using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NamedPipes.Interfaces;

namespace NamedPipes.Abstractions
{
	abstract class abstractParserParent : Interfaces.iParse, Interfaces.iValidate
	{
		protected abstractParserParent _next = null;
		private bool _isValid;
		private string _message;
		public bool isValid { get { return _isValid; } }
		public string message { get { return _message; } }
		public abstractParserParent appendParser(abstractParserParent parser)
		{
			parser._next = this;
			return parser;
		}
		public void clearValidation()
		{
			_isValid = true;
			_message = "";
			if (_next != null)
			{
				_next.clearValidation();
			}
		}
		public bool parse(string message)
		{
			if (handlesMessage(message))
			{
				if (processMessage(message))
				{
					return true;
				}
				else
				{
					//setValidation(false, "message was not properly parsed");
					return false;
				}
			}
			else
			{
				if (_next != null)
				{
					if (_next.parse(message))
					{
						return true;
					}
					else
					{
						//bubble up validation messages.
						setValidation(_next.isValid, _next.message);
						return false;
					}
				}
				else
				{
					setValidation(false, "no parser can handle message: " + message);
					return false;
				}
			}
		}
		protected abstract bool handlesMessage(string message);
		protected abstract bool processMessage(string message);
		protected void setValidation(bool validState, string msg)
		{
			_isValid = validState;
			_message = msg;
		}
	}
}
