package pl.olszak.michal.detector.fx.scenes.database;

import java.util.Observable;

/**
 * @author molszak
 *         created on 08.05.2017.
 */
public class DatabaseWindowContext extends Observable {

    private String maskFolder;
    private String resourcesFolder;
    private boolean loading = false;

    public DatabaseWindowContext() {
        maskFolder = "";
        resourcesFolder = "";
    }

    public String getMaskFolder() {
        return maskFolder;
    }

    public String getImageResourcesFolder() {
        return resourcesFolder;
    }

    public boolean isLoading() {
        return loading;
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

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}
