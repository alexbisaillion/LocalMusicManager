import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MoveAudioFile {
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
