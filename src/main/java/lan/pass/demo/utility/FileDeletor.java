package lan.pass.demo.utility;

import java.io.File;

public class FileDeletor {
    /**
     * Deletes a directory and all its contents.
     *
     * @param directory the directory to delete
     */
    public static void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] contents = directory.listFiles();
            if (contents != null) {
                for (File file : contents) {
                    deleteDirectory(file);
                }
            }
        }
        // Delete the directory after its contents have been deleted
        if (!directory.delete()) {
            System.err.println("Failed to delete directory: " + directory.getPath());
        }
    }
}
