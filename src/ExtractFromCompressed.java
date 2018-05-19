import java.io.File;
import java.nio.file.Path;

public class ExtractFromCompressed {
    public static boolean extractFromZip(File source, Path destination) {
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
