import com.github.junrar.Archive;
import com.github.junrar.impl.FileVolumeManager;
import com.github.junrar.rarfile.FileHeader;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SortArchiveFile {
    private static final int bufferSize = 2048;

    public static boolean extract(File source, Path destination, String archiveType) throws Exception {
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

        return true;
    }

    public static boolean extractRar(File source, Path destination) throws Exception {
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
            return false;
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
        return true;
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
}
