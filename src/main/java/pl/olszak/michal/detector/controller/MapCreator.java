package pl.olszak.michal.detector.controller;

import pl.olszak.michal.detector.fx.scenes.database.DatabaseWindowContext;
import pl.olszak.michal.detector.utils.ColorReduce;

/**
 * @author molszak
 *         created on 28.06.2017.
 */
public interface MapCreator {

    void process(DatabaseWindowContext context);

}
