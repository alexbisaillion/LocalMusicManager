import com.github.junrar.Archive;
import com.github.junrar.impl.FileVolumeManager;
import com.github.junrar.rarfile.FileHeader;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
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

    public static Path extract(File source, Path destination, String archiveType) throws Exception {
        Path tempDestination = Paths.get(destination.toString() + "\\" + source.getName().substring(0, source.getName().lastIndexOf(".")));
        if(!Files.exists(tempDestination)) {
            Files.createDirectory(tempDestination);
        }
        ArchiveInputStream archiveInputStream = null;
        try {
            archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveType, new FileInputStream(source));
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
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if(archiveInputStream != null) {
                try {
                    archiveInputStream.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return tempDestination;
    }

    public static Path extractRar(File source, Path destination) throws Exception {
        Path tempDestination = Paths.get(destination.toString() + "\\" + source.getName().substring(0, source.getName().lastIndexOf(".")));
        if(!Files.exists(tempDestination)) {
            Files.createDirectory(tempDestination);
        }
        Archive archive = null;
        try {
            archive = new Archive(new FileVolumeManager(source));
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
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if(archive != null) {
                try {
                    archive.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
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

    public static boolean analyze(Path location) throws Exception {
        File directory = location.toFile();
        File[] files = directory.listFiles();
        boolean faultDetected = false;
        if(files != null) {
            for (File child : files) {
                AudioFile file = AudioFileIO.read(child);
                Tag tag = file.getTag();
                if(tag.getFirst(FieldKey.TITLE).contains(".com")) { //check for junk in the title
                    faultDetected = true;
                }
                if(tag.getArtworkList().size() < 1) { //check for missing album art
                    faultDetected = true;
                }
                if(tag.getFirst(FieldKey.YEAR).length() != 4) { //check for faulty date
                    String year = tag.getFirst(FieldKey.YEAR);
                    if(year.contains("20")) {
                        String newYear = year.substring(year.indexOf("20"), year.indexOf("20") + 4);
                        tag.setField(FieldKey.YEAR, newYear);
                        AudioFileIO.write(file);
                    }
                    else if(year.contains("19")) {
                        String newYear = year.substring(year.indexOf("19"), year.indexOf("19") + 4);
                        tag.setField(FieldKey.YEAR, newYear);
                        AudioFileIO.write(file);
                    }
                    if(tag.getFirst(FieldKey.YEAR).length() != 4) { //if the fault wasn't corrected...
                        faultDetected = true;
                    }
                }
            }
        }
        return faultDetected;
    }
    public static boolean correct(Path location) throws Exception {
        File directory = location.toFile();
        File[] files = directory.listFiles();
        boolean changed = false;
        if(files != null) {
            for (File child : files) {
                AudioFile file = AudioFileIO.read(child);
                Tag tag = file.getTag();
                if(tag.getFirst(FieldKey.COMMENT).length() > 0) {
                    tag.setField(FieldKey.COMMENT, "");
                    AudioFileIO.write(file);
                    changed = true;
                }
                String title = tag.getFirst(FieldKey.TITLE);
                if(title.contains("ft.")) { //check for junk in the title
                    String newTitle = title.replace("ft.", "feat.");
                    tag.setField(FieldKey.TITLE, newTitle);
                    AudioFileIO.write(file);
                    changed = true;
                }
                if(title.contains("Ft.")) { //check for junk in the title
                    String newTitle = title.replace("Ft.", "feat.");
                    tag.setField(FieldKey.TITLE, newTitle);
                    AudioFileIO.write(file);
                    changed = true;
                }
                if(title.contains("Feat.")) { //check for junk in the title
                    String newTitle = title.replace("Feat.", "feat.");
                    tag.setField(FieldKey.TITLE, newTitle);
                    AudioFileIO.write(file);
                    changed = true;
                }
            }
        }
        return changed;
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
                    artist = tag.getFirst(FieldKey.ALBUM_ARTIST);
                    album = tag.getFirst(FieldKey.ALBUM);
                    break;
                }
            }
        }
        Path newLocation = location;
        if(artist.length() > 0 && album.length() > 0) {
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
}
