package pl.olszak.michal.detector.fx.scenes.database;

import java.util.Observable;
import java.util.Optional;

/**
 * @author molszak
 *         created on 08.05.2017.
 */
public class DatabaseWindowContext extends Observable {

    private Optional<String> maskFolder;
    private Optional<String> resourcesFolder;

    public DatabaseWindowContext() {
        maskFolder = Optional.empty();
        resourcesFolder = Optional.empty();
    }

    public Optional<String> getMaskFolder() {
        return maskFolder;
    }

    public Optional<String> getImageResourcesFolder() {
        return resourcesFolder;
    }

    public void setMaskFolder(String maskFolder) {
        if (maskFolder == null ||
                (this.maskFolder.isPresent() && maskFolder.equals(this.maskFolder.get()))) {
            return;
        }
        this.maskFolder = Optional.of(maskFolder);
        setChanged();
        notifyObservers();
    }

    public void setImageResourcesFolder(String imageResourcesFolder) {
        if (imageResourcesFolder == null ||
                (this.resourcesFolder.isPresent() && imageResourcesFolder.equals(this.resourcesFolder.get()))) {
            return;
        }
        this.resourcesFolder = Optional.of(imageResourcesFolder);
        setChanged();
        notifyObservers();
    }
}
