import sys
import win32com.client

iTunes = win32com.client.Dispatch("iTunes.Application")

iTunes.CurrentEncoder = iTunes.Encoders.ItemByName(sys.argv[1])
