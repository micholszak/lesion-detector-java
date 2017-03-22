package pl.olszak.michal.detector.model.files;

import java.io.File;

/**
 * @author molszak
 *         created on 22.03.2017.
 */
public abstract class AbstractImageFile implements Comparable<AbstractImageFile> {

    private final File file;

    public AbstractImageFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public String getFileName() {
        return file.getName();
    }

    public abstract ImageType getImageType();

    @Override
    public int compareTo(AbstractImageFile other) {
        String name = file.getName().substring(0, file.getName().indexOf('.'));
        String otherName = other.getFileName().substring(0, other.getFileName().indexOf('.'));
        return name.compareTo(otherName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractImageFile that = (AbstractImageFile) o;

        return file.equals(that.file);
    }

    @Override
    public int hashCode() {
        return file.hashCode();
    }
}
