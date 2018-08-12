import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SortAudioFile {
    public static boolean isFaulty(Path location) throws Exception {
        File source = location.toFile();
        AudioFile file = AudioFileIO.read(source);
        Tag tag = file.getTag();
        if(tag.getFirst(FieldKey.TITLE).contains(".com")) { //check for junk in the title
            return true;
        }
        if(tag.getArtworkList().size() < 1) { //check for missing album art
            return true;
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
                return true;
            }
        }
        return false;
    }
    public static void correct(Path location) throws Exception {
        File directory = location.toFile();
        AudioFile file = AudioFileIO.read(directory);
        Tag tag = file.getTag();
        if(tag.getFirst(FieldKey.DISC_TOTAL).equals("1")) {
            tag.deleteField(FieldKey.DISC_TOTAL);
            tag.deleteField(FieldKey.DISC_NO);
        }
        if(tag.getFirst(FieldKey.COMMENT).length() > 0) {
            tag.setField(FieldKey.COMMENT, "");
            AudioFileIO.write(file);
        }
        File parent = directory.getParentFile();
        File[] children = parent.listFiles();
        if(children != null) {
            int numTracksInAlbum = 0;
            for(File child: children) {
                AudioFile childFile = AudioFileIO.read(child);
                Tag childTag = childFile.getTag();
                if(childTag.getFirst(FieldKey.ALBUM).equals(tag.getFirst(FieldKey.ALBUM))) {
                    numTracksInAlbum++;
                }
            }
            if(numTracksInAlbum > 0 && !String.valueOf(numTracksInAlbum).equals(tag.getFirst(FieldKey.TRACK_TOTAL))) {
                tag.setField((FieldKey.TRACK_TOTAL));
            }
        }
        String title = tag.getFirst(FieldKey.TITLE);
        if(title.contains("ft.")) { //check for junk in the title
            String newTitle = title.replace("ft.", "feat.");
            tag.setField(FieldKey.TITLE, newTitle);
            AudioFileIO.write(file);
        }
        if(title.contains("Ft.")) { //check for junk in the title
            String newTitle = title.replace("Ft.", "feat.");
            tag.setField(FieldKey.TITLE, newTitle);
            AudioFileIO.write(file);
        }
        if(title.contains("Feat.")) { //check for junk in the title
            String newTitle = title.replace("Feat.", "feat.");
            tag.setField(FieldKey.TITLE, newTitle);
            AudioFileIO.write(file);
        }
        String genre = tag.getFirst(FieldKey.GENRE);
        if(genre.equals("R&B/Soul")) {
            tag.setField(FieldKey.GENRE, "R&B");
            AudioFileIO.write(file);
        }
        if(genre.equals("Hip-Hop/Rap") || genre.equals("Hip Hop")) {
            tag.setField(FieldKey.GENRE, "Hip-Hop");
            AudioFileIO.write(file);
        }
    }

    public static boolean changeArtist(Path location, String artist) throws Exception {
        File directory = location.toFile();
        AudioFile file = AudioFileIO.read(directory);
        Tag tag = file.getTag();
        tag.setField(FieldKey.ARTIST, artist);
        AudioFileIO.write(file);
        return true;
    }

    public static boolean changeAlbumArtist(Path location, String albumArtist) throws Exception {
        File directory = location.toFile();
        AudioFile file = AudioFileIO.read(directory);
        Tag tag = file.getTag();
        tag.setField(FieldKey.ALBUM_ARTIST, albumArtist);
        AudioFileIO.write(file);
        return true;
    }

    public static boolean changeGenre(Path location, String genre) throws Exception {
        File directory = location.toFile();
        AudioFile file = AudioFileIO.read(directory);
        Tag tag = file.getTag();
        tag.setField(FieldKey.GENRE, genre);
        AudioFileIO.write(file);
        return true;
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
