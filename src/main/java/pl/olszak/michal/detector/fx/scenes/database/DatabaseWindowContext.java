package pl.olszak.michal.detector.fx.scenes.database;

import pl.olszak.michal.detector.utils.ColorReduce;

/**
 * @author molszak
 *         created on 08.05.2017.
 */
public class DatabaseWindowContext{

    private String maskFolder;
    private String resourcesFolder;
    private ColorReduce colorReduce;

    public DatabaseWindowContext() {
        maskFolder = "";
        resourcesFolder = "";
        colorReduce = ColorReduce.BINS_PER_CHANNEL_4;
    }

    public String getMaskFolder() {
        return maskFolder;
    }

    public String getImageResourcesFolder() {
        return resourcesFolder;
    }

    public ColorReduce getColorReduce() {
        return colorReduce;
    }


    public void setMaskFolder(String maskFolder) {
        if (maskFolder == null || maskFolder.equals(this.maskFolder)) {
            return;
        }
        this.maskFolder = maskFolder;
    }

    public void setImageResourcesFolder(String imageResourcesFolder) {
        if (imageResourcesFolder == null || imageResourcesFolder.equals(this.resourcesFolder)) {
            return;
        }
        this.resourcesFolder = imageResourcesFolder;
    }

    public void setColorReduce(ColorReduce colorReduce) {
        if (colorReduce.equals(this.colorReduce)) {
            return;
        }
        this.colorReduce = colorReduce;
    }
}
