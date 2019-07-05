using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NamedPipes.Implementations
{
	class AEP_Compute: Interfaces.iValidate
	{
		private string _rasPlanName;
		private string _aepAlternativeName;
		private string _rootDirectory;
		private string _rasResultsPath;
		private int _lifecycleNumber;
		private int _realizationNumber;
		private int _eventNumber;
		private string _message;
		private bool _valid = true;
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

		private void setValidation(bool validState, string msg)
		{
			throw new NotImplementedException();
		}
	}
}
