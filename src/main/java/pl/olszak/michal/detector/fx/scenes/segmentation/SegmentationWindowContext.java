package pl.olszak.michal.detector.fx.scenes.segmentation;

/**
 * @author molszak
 *         created on 28.06.2017.
 */
public class SegmentationWindowContext{

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
    }

    public void setSegmentationDestinationFolder(String segmentationDestinationFolder) {
        if(segmentationDestinationFolder == null || segmentationDestinationFolder.equals(this.segmentationDestinationFolder)){
            return;
        }
        this.segmentationDestinationFolder = segmentationDestinationFolder;
    }
}
