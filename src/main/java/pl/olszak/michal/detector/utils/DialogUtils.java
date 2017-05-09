package pl.olszak.michal.detector.utils;

import javafx.stage.DirectoryChooser;
import javafx.stage.Window;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * @author molszak
 *         created on 09.05.2017.
 */
public final class DialogUtils {

    private DialogUtils() {
    }


    public static Optional<File> openFolder(final String windowTitle,final Window window) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(windowTitle);
        directoryChooser.setInitialDirectory(new File(Paths.get(".").toAbsolutePath().toString()));
        File directory = directoryChooser.showDialog(window);
        if (directory == null) {
            return Optional.empty();
        }
        return Optional.of(directory);
    }

}
