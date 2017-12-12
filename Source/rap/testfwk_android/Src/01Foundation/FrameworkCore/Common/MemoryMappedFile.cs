

using System;
using System.Runtime.InteropServices;

namespace Philips.MRAutomation.Foundation.FrameworkCore.Common
{
	public class MemoryMappedFile : IDisposable
	{
		private IntPtr fileMapping;
		private readonly int size;
		private readonly FileAccess access;

		public enum FileAccess : int
		{
			ReadOnly = 2,
			ReadWrite = 4
		}

		private MemoryMappedFile(IntPtr fileMapping, int size, FileAccess access)
		{
			this.fileMapping = fileMapping;
			this.size = size;
			this.access = access;
		}
		
			public static MemoryMappedFile CreateFile(string name, FileAccess access, int size)
		{
			if(size < 0)
				throw new ArgumentException("Size must not be negative","size");

			IntPtr fileMapping = NTKernel.CreateFileMapping(0xFFFFFFFFu,null,(uint)access,0,(uint)size,name);
			if(fileMapping == IntPtr.Zero)
				throw new MemoryMappingFailedException();

			return new MemoryMappedFile(fileMapping,size,access);
		}


		public MemoryMappedFileView CreateView(int offset, int size, MemoryMappedFileView.ViewAccess access)
		{
			if(this.access == FileAccess.ReadOnly && access == MemoryMappedFileView.ViewAccess.ReadWrite)
				throw new ArgumentException("Only read access to views allowed on files without write access","access");
			if(offset < 0)
				throw new ArgumentException("Offset must not be negative","size");
			if(size < 0)
				throw new ArgumentException("Size must not be negative","size");
			IntPtr mappedView = NTKernel.MapViewOfFile(fileMapping,(uint)access,0,(uint)offset,(uint)size);
			return new MemoryMappedFileView(mappedView,size,access);
		}

		#region IDisposable Member

		public void Dispose()
		{
			if(fileMapping != IntPtr.Zero)
			{
				if(NTKernel.CloseHandle((uint)fileMapping))
					fileMapping = IntPtr.Zero;
			}
		}

		#endregion
	}
}

