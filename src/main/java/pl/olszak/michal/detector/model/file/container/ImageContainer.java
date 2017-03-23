package pl.olszak.michal.detector.model.file.container;

import pl.olszak.michal.detector.model.file.AbstractImageFile;
import pl.olszak.michal.detector.model.file.ImageType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author molszak
 *         created on 23.03.2017.
 */
public class ImageContainer {

    private final List<AbstractImageFile> images = new ArrayList<>();
    private final ImageType type;

    public ImageContainer(ImageType type) {
        this.type = type;
    }

    public void addImage(File image) {
        ImageContainerContext context = new ImageContainerContext(type, image);
        images.add(ImageContainerFactory.create(context));
    }

    public List<AbstractImageFile> getImages() {
        return images;
    }

    public ImageType getType() {
        return type;
    }
}
