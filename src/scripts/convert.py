import sys
import win32com.client
import time

iTunes = win32com.client.Dispatch("iTunes.Application")
library = iTunes.LibraryPlaylist

success = False;
try:
    while(not success):
        try:
            operationStatus = iTunes.ConvertFile2(sys.argv[1])
            while(operationStatus.ProgressValue < operationStatus.MaxProgressValue):
                time.sleep(1)
            success = True;
        except Exception as e:
            time.sleep(1)
except Exception as e:
    print(e)