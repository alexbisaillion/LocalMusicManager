# Using the iTunes COM interface for Windows to delete missing files from an iTunes library.
# Install pywin32 using the following command:
#   pip install pywin32

import win32com.client
        
iTunes = win32com.client.Dispatch("iTunes.Application")
library = iTunes.LibraryPlaylist
tracks = library.Tracks

missingTracks = []

# Iteration starts at 1, not 0.
for x in range(1, tracks.Count+1):
    track = win32com.client.CastTo(tracks.Item(x), 'IITFileOrCDTrack')
    try:
        path = track.Location
        if path == "":
            missingTracks.append(x)
    except Exception as e:
        print(e)
    
# When deleting a track, the next track falls into the place of the deleted track.
# Therefore, it is necessary to keep deleting at the same index.
for x in range(len(missingTracks)):
    try:
        print("Deleting " + tracks.Item(missingTracks[0]).Name + " at index " + str(missingTracks[0]) + " previously at " + str(missingTracks[x]))
        tracks.Item(missingTracks[0]).Delete()
    except Exception as e:
        print(e)