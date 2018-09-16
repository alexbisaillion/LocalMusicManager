import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;

public class SetupModel {
    private HashMap<ReleaseFormat, Path> formatPaths;
    private HashMap<String, Path> conversionPaths;
    private HashMap<String, Path> devicePaths;
    private Path iTunesMediaFolder;

    public SetupModel(HashMap<ReleaseFormat, Path> fp, HashMap<String, Path> cp, HashMap<String, Path> dp, Path imf) {
        formatPaths = fp;
        conversionPaths = cp;
        devicePaths = dp;
        iTunesMediaFolder = imf;
    }

    public HashMap<ReleaseFormat, Path> getFormatPaths() { return formatPaths; }
    public HashMap<String, Path> getConversionPaths() { return conversionPaths; }
    public HashMap<String, Path> getDevicePaths() { return devicePaths; }
    public Path getiTunesMediaFolder() { return iTunesMediaFolder; }

    public void setFormatPaths(HashMap<ReleaseFormat, Path> fp) { formatPaths = fp; }
    public void setConversionPaths(HashMap<String, Path> cp) { conversionPaths = cp; }
    public void setDevicePaths(HashMap<String, Path> dp) { devicePaths = dp; }
    public void setiTunesMediaFolder(Path imf) { iTunesMediaFolder = imf; }

    public boolean checkWritePermissions() {
        for(Path p: formatPaths.values()) {
            if(!p.toFile().canWrite()) {
                return false;
            }
        }
        for(Path p: conversionPaths.values()) {
            if(!p.toFile().canWrite()) {
                return false;
            }
        }
        for(Path p: devicePaths.values()) {
            if(!p.toFile().canWrite()) {
                return false;
            }
        }
        if(!iTunesMediaFolder.toFile().canWrite()) {
            return false;
        }
        return true;
    }

    public static boolean isComplete(HashMap<ReleaseFormat, Path> fp, HashMap<String, Path> cp, HashMap<String, Path> dp, Path imf) {
        for(ReleaseFormat rf: ReleaseFormat.values()) {
            if(!fp.keySet().contains(rf)) {
                return false;
            }
        }
        return(cp.keySet().contains("AAC") && cp.keySet().contains("MP3") && dp.keySet().contains("Phone"));
    }

}
