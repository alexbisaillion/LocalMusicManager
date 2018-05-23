import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SortAudioFile {
    public static boolean analyze(Path location) throws Exception {
        File source = location.toFile();
        AudioFile file = AudioFileIO.read(source);
        Tag tag = file.getTag();
        boolean faultDetected = false;
        if(tag.getFirst(FieldKey.TITLE).contains(".com")) { //check for junk in the title
            System.out.println("hi");
            faultDetected = true;
        }
        if(tag.getArtworkList().size() < 1) { //check for missing album art
            System.out.println("hi");
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
        return faultDetected;
    }
    public static boolean correct(Path location) throws Exception {
        File directory = location.toFile();
        boolean changed = false;
        AudioFile file = AudioFileIO.read(directory);
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
        return changed;
    }

    public static Path moveSingle(File source, Path destination) throws Exception {
        AudioFile file = AudioFileIO.read(source);
        Tag tag = file.getTag();
        Path artistDirectory = Paths.get(destination.toString() + "\\" + tag.getFirst(FieldKey.ALBUM_ARTIST));
        if (!Files.exists(artistDirectory)) {
            Files.createDirectory(artistDirectory);
        }
        return Files.move(Paths.get(source.getPath()), Paths.get(artistDirectory.toString() + "\\" + source.getName()));
    }
    public static Path moveUnreleased(File source, Path destination) throws Exception {
        AudioFile file = AudioFileIO.read(source);
        Tag tag = file.getTag();
        Path artistDirectory = Paths.get(destination.toString() + "\\" + tag.getFirst(FieldKey.ALBUM_ARTIST));
        if (!Files.exists(artistDirectory)) {
            Files.createDirectory(artistDirectory);
        }
        //will need more logic
        return Files.move(Paths.get(source.getPath()), Paths.get(artistDirectory.toString() + "\\" + source.getName()));
    }
}
