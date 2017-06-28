package pl.olszak.michal.detector.fx.scenes.segmentation;

import java.util.Observable;

/**
 * @author molszak
 *         created on 28.06.2017.
 */
public class SegmentationWindowContext extends Observable {

    private String segmentationResourcesFolder;
    private String segmentationDestinationFolder;

    public SegmentationWindowContext(){
        segmentationResourcesFolder = "";
        segmentationDestinationFolder = "";
    }

    public String getSegmentationResourcesFolder() {
        return segmentationResourcesFolder;
    }

    public String getSegmentationDestinationFolder() {
        return segmentationDestinationFolder;
    }

    public void setSegmentationResourcesFolder(String segmentationResourcesFolder) {
        if(segmentationResourcesFolder == null || segmentationResourcesFolder.equals(this.segmentationResourcesFolder)){
            return;
        }
        this.segmentationResourcesFolder = segmentationResourcesFolder;
        setChanged();
        notifyObservers();
    }

    public void setSegmentationDestinationFolder(String segmentationDestinationFolder) {
        if(segmentationDestinationFolder == null || segmentationDestinationFolder.equals(this.segmentationDestinationFolder)){
            return;
        }
        this.segmentationDestinationFolder = segmentationDestinationFolder;
        setChanged();
        notifyObservers();
    }
}
