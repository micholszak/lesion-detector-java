package pl.olszak.michal.detector.fx.scenes.roc;

import pl.olszak.michal.detector.utils.ColorReduce;

/**
 * @author molszak
 *         created on 11.07.2017.
 */
public class RocWindowContext {

    private ColorReduce colorReduce;
    private String segmentationFilesFolder;
    private String maskResourcesFolder;
    private String rocFilesDestinationText;
    private String databaseCollectionName;

    public RocWindowContext() {
        colorReduce = ColorReduce.BINS_PER_CHANNEL_4;
        segmentationFilesFolder = "";
        maskResourcesFolder = "";
        rocFilesDestinationText = "";
        databaseCollectionName = "";
    }

    public ColorReduce getColorReduce() {
        return colorReduce;
    }

    public void setColorReduce(ColorReduce colorReduce) {
        this.colorReduce = colorReduce;
    }

    public String getSegmentationFilesFolder() {
        return segmentationFilesFolder;
    }

    public void setSegmentationFilesFolder(String segmentationFilesFolder) {
        this.segmentationFilesFolder = segmentationFilesFolder;
    }

    public String getMaskResourcesFolder() {
        return maskResourcesFolder;
    }

    public void setMaskResourcesFolder(String maskResourcesFolder) {
        this.maskResourcesFolder = maskResourcesFolder;
    }

    public String getRocFilesDestinationText() {
        return rocFilesDestinationText;
    }

    public void setRocFilesDestinationText(String rocFilesDestinationText) {
        this.rocFilesDestinationText = rocFilesDestinationText;
    }

    public String getDatabaseCollectionName() {
        return databaseCollectionName;
    }

    public void setDatabaseCollectionName(String databaseCollectionName) {
        this.databaseCollectionName = databaseCollectionName;
    }
}
