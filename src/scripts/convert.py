import sys
import win32com.client

iTunes = win32com.client.Dispatch("iTunes.Application")
library = iTunes.LibraryPlaylist

success = False

while not success:
    try:
        iTunes.ConvertFile2(sys.argv[1])
        success = True
    except Exception as e:
        success = False