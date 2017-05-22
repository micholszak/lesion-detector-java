package pl.olszak.michal.detector.model.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.awt.Color;

/**
 * Created by Michal on 2017-05-20.
 */
@Document(collection = "probabilityMap")
public class ColorProbability {

    @Id
    private final Color color;

    private final int colorMode;

    private final double lesionProbability;

    private final double nonLesionProbability;

    private final int sampleSize;

    public ColorProbability(Color color, int colorMode, double lesionProbability, double nonLesionProbability, int sampleSize) {
        this.color = color;
        this.colorMode = colorMode;
        this.lesionProbability = lesionProbability;
        this.nonLesionProbability = nonLesionProbability;
        this.sampleSize = sampleSize;
    }


    public Color getColor() {
        return color;
    }

    public int getColorMode() {
        return colorMode;
    }

    public double getLesionProbability() {
        return lesionProbability;
    }

    public double getNonLesionProbability() {
        return nonLesionProbability;
    }

    public int getSampleSize() {
        return sampleSize;
    }

    @Override
    public String toString() {
        return "ColorProbability{" +
                "color=" + color +
                ", colorMode=" + colorMode +
                ", lesionProbability=" + lesionProbability +
                ", nonLesionProbability=" + nonLesionProbability +
                ", sampleSize=" + sampleSize +
                '}';
    }
}
