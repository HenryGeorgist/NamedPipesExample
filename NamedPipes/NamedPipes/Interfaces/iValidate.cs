﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NamedPipes.Interfaces
{
	interface iValidate
	{
		bool isValid { get; }
		string message { get; }
	}
}
