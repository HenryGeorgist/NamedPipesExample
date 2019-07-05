using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NamedPipes.Implementations
{
	class AEP_Alternative: Interfaces.iValidate
	{
		private string _pointShapefilePath;
		private string _terrainFilePath;
		private string _alternativeName;
		private bool _valid = true;
		private string _message = "";
		public bool isValid
		{
			get
			{
				return _valid;
			}
		}
		public string message
		{
			get
			{
				return _message;
			}
		}
		public string alternativeName {
			get
			{
				return _alternativeName;
			}
		}
		public AEP_Alternative(string initializationMessage)
		{
			//message should follow a consistent form -i <altname> -p <pointshapepath> -t <terrainfilepath>
			string[] args = initializationMessage.Split(new string[] { "-" },StringSplitOptions.None);
			//first arg should be i <altname>
			if (args[0].Substring(0, 1) == "i")
			{
				_alternativeName = args[0].Substring(2, args[0].Length);
			}
			else
			{
				setValidation(false,"expected i, got " + args[0].Substring(0, 1));
			}
			//second arg should be p <pointsshapepath>
			if (args[1].Substring(0, 1) == "p")
			{
				_pointShapefilePath = args[1].Substring(2, args[1].Length);
			}
			else
			{
				setValidation(false,"expected p, got " + args[1].Substring(0, 1));
			}
			//second arg should be t <terrainfilepath>
				if (args[2].Substring(0, 1) == "t")
			{
				_terrainFilePath = args[2].Substring(2, args[2].Length);
			}
			else
			{
				setValidation(false,"expected t, got " + args[2].Substring(0, 1));
			}

		}
		private void setValidation(bool state, string msg)
		{
			_valid = state;
			_message = message;
		}

	}
}
