import com.github.junrar.Archive;
import com.github.junrar.impl.FileVolumeManager;
import com.github.junrar.rarfile.FileHeader;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SortArchiveFile {
    private static final int bufferSize = 2048;

    public static Path extractZIP(File source, Path destination) throws Exception {
        Path tempDestination = Paths.get(destination.toString() + "\\" + source.getName().substring(0, source.getName().lastIndexOf(".")));
        if(!Files.exists(tempDestination)) {
            Files.createDirectory(tempDestination);
        }
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, new FileInputStream(source));
        ArchiveEntry entry = archiveInputStream.getNextEntry();
        while(entry != null) {
            if(isAudioFile(entry)) {
                String fileName = entry.getName().substring(entry.getName().lastIndexOf("/") + 1);
                Path target = Paths.get(tempDestination.toString() + "\\" + fileName);
                int count;
                byte data[] = new byte[bufferSize];
                FileOutputStream fileOutputStream = new FileOutputStream(target.toFile());
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, bufferSize);
                while((count = archiveInputStream.read(data, 0, bufferSize)) != -1) {
                    bufferedOutputStream.write(data, 0, count);
                }
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            }
            entry = archiveInputStream.getNextEntry();
        }
        archiveInputStream.close();

        return tempDestination;
    }

    public static Path extract7z(File source, Path destination) throws Exception {
        Path tempDestination = Paths.get(destination.toString() + "\\" + source.getName().substring(0, source.getName().lastIndexOf(".")));
        if(!Files.exists(tempDestination)) {
            Files.createDirectory(tempDestination);
        }
        SevenZFile sevenZFile = new SevenZFile(source);
        SevenZArchiveEntry entry = sevenZFile.getNextEntry();
        while(entry != null) {
            if(isAudioFile(entry)) {
                String fileName = entry.getName().substring(entry.getName().lastIndexOf("/") + 1);
                Path target = Paths.get(tempDestination.toString() + "\\" + fileName);
                int count;
                byte data[] = new byte[bufferSize];
                FileOutputStream fileOutputStream = new FileOutputStream(target.toFile());
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, bufferSize);
                while((count = sevenZFile.read(data, 0, bufferSize)) != -1) {
                    bufferedOutputStream.write(data, 0, count);
                }
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            }
            entry = sevenZFile.getNextEntry();
        }
        sevenZFile.close();

        return tempDestination;
    }

    public static Path extractRar(File source, Path destination) throws Exception {
        Path tempDestination = Paths.get(destination.toString() + "\\" + source.getName().substring(0, source.getName().lastIndexOf(".")));
        if(!Files.exists(tempDestination)) {
            Files.createDirectory(tempDestination);
        }
        Archive archive = new Archive(new FileVolumeManager(source));
        FileHeader fileHeader = archive.nextFileHeader();
        while(fileHeader != null) {
            if(isAudioFile(fileHeader)) {
                String fileName = fileHeader.getFileNameString().substring(fileHeader.getFileNameString().lastIndexOf("\\") + 1);
                Path target = Paths.get(tempDestination.toString() + "\\" + fileName);
                File output = target.toFile();
                FileOutputStream outputStream = new FileOutputStream(output);
                archive.extractFile(fileHeader, outputStream);
                outputStream.flush();
                outputStream.close();
            }
            fileHeader = archive.nextFileHeader();
        }

        archive.close();
        return tempDestination;
    }

    private static boolean isAudioFile(ArchiveEntry entry) {
        if(entry.isDirectory()) {
            return false;
        }
        String extension = entry.getName().substring(entry.getName().lastIndexOf("."));
        return (extension.equals(".m4a") || extension.equals(".mp3") || extension.equals(".aac") || extension.equals(".wav") || extension.equals(".aiff"));
    }

    private static boolean isAudioFile(FileHeader entry) {
        if(entry.isDirectory()) {
            return false;
        }
        String extension = entry.getFileNameString().substring(entry.getFileNameString().lastIndexOf("."));
        return (extension.equals(".m4a") || extension.equals(".mp3") || extension.equals(".aac") || extension.equals(".wav") || extension.equals(".aiff"));
    }

    public static boolean isFaulty(Path location) throws Exception {
        File directory = location.toFile();
        File[] files = directory.listFiles();
        if(files != null) {
            for (File child : files) {
                if(SortAudioFile.isFaulty(child.toPath())) {
                    return true;
                }
            }
        }
        return false;
    }
    public static void correct(Path location) throws Exception {
        File directory = location.toFile();
        File[] files = directory.listFiles();
        if(files != null) {
            for (File child : files) {
                SortAudioFile.correct(child.toPath());
            }
        }
    }
    public static void changeArtist(Path location, String artist) throws Exception {
        File directory = location.toFile();
        File[] files = directory.listFiles();
        if(files != null) {
            for (File child : files) {
                SortAudioFile.changeArtist(child.toPath(), artist);
            }
        }
    }

    public static void changeAlbumArtist(Path location, String albumArtist) throws Exception {
        File directory = location.toFile();
        File[] files = directory.listFiles();
        if(files != null) {
            for (File child : files) {
                SortAudioFile.changeAlbumArtist(child.toPath(), albumArtist);
            }
        }
    }

    public static void changeGenre(Path location, String genre) throws Exception {
        File directory = location.toFile();
        File[] files = directory.listFiles();
        boolean changed = false;
        if(files != null) {
            for (File child : files) {
                SortAudioFile.changeGenre(child.toPath(), genre);
            }
        }
    }

    public static Path move(Path location) throws Exception {
        File directory = location.toFile();
        File[] files = directory.listFiles();
        String artist = "";
        String album = "";
        if(files != null) {
            for (File child : files) {
                AudioFile file = AudioFileIO.read(child);
                Tag tag = file.getTag();
                if(tag.getFirst(FieldKey.ALBUM_ARTIST).length() > 0 && tag.getFirst(FieldKey.ALBUM).length() > 0) {
                    artist = stripIllegalCharacters(tag.getFirst(FieldKey.ALBUM_ARTIST));
                    album = stripIllegalCharacters(tag.getFirst(FieldKey.ALBUM));
                    break;
                }
            }
        }
        Path newLocation = location;
        if(artist.equals(album)) { //self-titled albums are a problem:
            File renamedFolder = new File(directory.getParent() + "\\" + album + " (Self-Titled)");
            directory.renameTo(renamedFolder);
            Path artistDirectory = Paths.get(location.getParent().toString() + "\\" + artist);
            if(!Files.exists(artistDirectory)) {
                Files.createDirectory(artistDirectory);
            }
            artistDirectory = Paths.get(location.getParent().toString() + "\\" + artist + "\\" + album + " (Self-Titled)");
            newLocation = Files.move(Paths.get(renamedFolder.getPath()), artistDirectory);
        }
        else if(artist.length() > 0 && album.length() > 0) {
            File renamedFolder = new File(directory.getParent() + "\\" + album);
            directory.renameTo(renamedFolder); //rename album folder
            Path artistDirectory = Paths.get(location.getParent().toString() + "\\" + artist);
            if(!Files.exists(artistDirectory)) {
                Files.createDirectory(artistDirectory);
            }
            artistDirectory = Paths.get(location.getParent().toString() + "\\" + artist + "\\" + album);
            newLocation = Files.move(Paths.get(renamedFolder.getPath()), artistDirectory);
        }
        return newLocation;
    }

    public static String stripIllegalCharacters(String s) {
        StringBuilder stringBuilder = new StringBuilder(s);
        char[] illegalCharacters = {'/', '\\', ':', '*', '?', '"', '<', '>', '|'};
        for(int i = 0; i<stringBuilder.length(); i++) {
            for(char c : illegalCharacters) {
                if(stringBuilder.charAt(i) == c) {
                    stringBuilder.deleteCharAt(i);
                    i--;
                    break;
                }
            }
        }
        return stringBuilder.toString();
    }
}
