import sys
import win32com.client

iTunes = win32com.client.Dispatch("iTunes.Application")
library = iTunes.LibraryPlaylist

print(sys.argv[1])
library.AddFile(sys.argv[1])