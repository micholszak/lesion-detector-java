package pl.olszak.michal.detector.fx.scenes.database;

import java.util.Observable;
import java.util.Optional;

/**
 * @author molszak
 *         created on 08.05.2017.
 */
public class DatabaseWindowContext extends Observable {

    private String maskFolder;
    private String resourcesFolder;
    private String jsonDatabaseFolder;

    public DatabaseWindowContext() {
        maskFolder = "";
        resourcesFolder = "";
        jsonDatabaseFolder = "";
    }

    public String getMaskFolder() {
        return maskFolder;
    }

    public String getImageResourcesFolder() {
        return resourcesFolder;
    }

    public String getJsonDatabaseFolder() {
        return jsonDatabaseFolder;
    }

    public void setMaskFolder(String maskFolder) {
        if (maskFolder == null || maskFolder.equals(this.maskFolder)) {
            return;
        }
        this.maskFolder = maskFolder;
        setChanged();
        notifyObservers();
    }

    public void setImageResourcesFolder(String imageResourcesFolder) {
        if (imageResourcesFolder == null || imageResourcesFolder.equals(this.resourcesFolder)) {
            return;
        }
        this.resourcesFolder = imageResourcesFolder;
        setChanged();
        notifyObservers();
    }

    public void setJsonDatabaseFolder(String jsonDatabaseFolder) {
        if (jsonDatabaseFolder == null || jsonDatabaseFolder.equals(this.jsonDatabaseFolder)) {
            return;
        }
        this.jsonDatabaseFolder = jsonDatabaseFolder;
        setChanged();
        notifyObservers();
    }
}
