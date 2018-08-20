import sys
import win32com.client
import time

iTunes = win32com.client.Dispatch("iTunes.Application")
library = iTunes.LibraryPlaylist

success = False;
try:
    operationStatus = iTunes.ConvertFile2(sys.argv[1])
    while(iTunes.ConvertOperationStatus is not None):
        time.sleep(1)
except Exception as e:
    print(e)