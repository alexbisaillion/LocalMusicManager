#using the iTunes COM interface for Windows to delete missing files from an iTunes library
#install pywin32 using pip install pywin32

import win32com.client
        
def fetchMissingTracks():
    iTunes = win32com.client.Dispatch("iTunes.Application")
    library = iTunes.LibraryPlaylist
    tracks = library.Tracks

    #iteration starts at 1, not 0
    for x in range(1, tracks.Count+1):
        try:
            currTrack = tracks.Item(x)
        except Exception as e:
            print("Rebuilding library playlist...")
            library = iTunes.LibraryPlaylist
            tracks = library.Tracks
            currTrack = tracks.Item(x)
        try:
            if currTrack.Kind == 1 and currTrack.location == "":
                print("Deleting " + currTrack.name)
                currTrack.Delete()
        except AttributeError:
            fetchMissingTracks()
            break

fetchMissingTracks()