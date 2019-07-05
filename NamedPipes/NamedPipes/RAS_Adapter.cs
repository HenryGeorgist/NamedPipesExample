using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NamedPipes
{
	public sealed class RAS_Adapter
	{
		static RAS_Adapter Instance { get; }
		
		private RAS_Adapter()
		{
			//initalize gdal- requires GDAL dependencies to be next to executable path.
			Environment.SetEnvironmentVariable("GDAL_TIFF_OVR_BLOCKSIZE", "256");
			GDALAssist.GDALSetup.Initialize(System.IO.Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "GDAL"));
		}

		public bool processMessage(string message)
		{
			message = message.Trim();

			string code = message.Substring(0, 2);
			switch (code) {
				case "-i":
					break;
				case "-c":
					break;
				default:
					return false;
			}
			return true;
		}

		//  If args(i) = "-d" AndAlso i< (args.Count - 1) Then baseDir = args(i + 1)
		//	If args(i) = "-f" AndAlso i< (args.Count - 1) Then rasFileName = args(i + 1)
		//	If args(i) = "-r" AndAlso i< (args.Count - 1) Then Integer.TryParse(args(i + 1), nRealizations)
		//	If args(i) = "-l" AndAlso i< (args.Count - 1) Then Integer.TryParse(args(i + 1), nLifeCycles)
		//	If args(i) = "-e" AndAlso i< (args.Count - 1) Then Integer.TryParse(args(i + 1), nEvents)
		//	If args(i) = "-m" AndAlso i< (args.Count - 1) Then Boolean.TryParse(args(i + 1), isPostProcess)
		//	If args(i) = "-t" AndAlso i< (args.Count - 1) Then terrainPath = args(i + 1)
		//	If args(i) = "-p" AndAlso i< (args.Count - 1) Then pointsPath = args(i + 1)


	}
}
