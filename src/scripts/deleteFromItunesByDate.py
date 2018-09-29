import sys
import win32com.client
import datetime
from pytz import timezone

iTunes = win32com.client.Dispatch("iTunes.Application")
library = iTunes.LibraryPlaylist
tracks = library.Tracks

iTunesMediaFolder = sys.argv[1]

conversionStartDate = datetime.datetime.strptime(sys.argv[2], "%Y/%m/%d %H:%M:%S GMT%z")

indicesToDelete = []
locationsToDelete = []

for x in range(1, tracks.Count+1):
    try:
        currTrack = tracks.Item(x)
        currTrackDate = currTrack.DateAdded        
        if(currTrackDate > conversionStartDate):
            indicesToDelete.append(x)
            locationsToDelete.append(currTrack.Location)
        elif(currTrackDate == conversionStartDate):
            if(iTunesMediaFolder in currTrack.Location):
                indicesToDelete.append(x)
                locationsToDelete.append(currTrack.Location)
    except Exception as e:
        print(e)
        
# When deleting a track, the next track falls into the place of the deleted track.
# Therefore, it is necessary to keep deleting at the same index, assuming all deleted tracks follow sequentially.
while(len(indicesToDelete) > 0):
    library = iTunes.LibraryPlaylist
    tracks = library.Tracks
    if(tracks.Item(indicesToDelete[0]).Location in locationsToDelete):
        tracks.Item(indicesToDelete[0]).Delete()
        deletedIndex = indicesToDelete.pop(0)
        del locationsToDelete[0]
        for x in range(len(indicesToDelete)):
            if(indicesToDelete[x] > deletedIndex):
                indicesToDelete[x] = indicesToDelete[x] - 1
    else:
        raise ValueError("A track fell into place that was not meant to be deleted")