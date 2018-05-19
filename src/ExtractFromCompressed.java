import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExtractFromCompressed {
    public static boolean extractFromZip(File source, Path destination) {
        Path tempDestination = Paths.get(destination.toString() + "\\" + source.getName().substring(0, source.getName().lastIndexOf(".")));
        System.out.println(tempDestination);
        System.out.println("ZIP" + source.getName());
        
        return true;
    }
    public static boolean extractFromRar(File source, Path destination) {
        System.out.println("RAR" + source.getName());
        return true;
    }
    public static boolean extractFrom7z(File source, Path destination) {
        System.out.println("7Z" + source.getName());
        return true;
    }
}
