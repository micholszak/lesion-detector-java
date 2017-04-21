package pl.olszak.michal.detector.system.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;
import pl.olszak.michal.detector.core.operations.controller.ProbabilityMapCreatorController;

/**
 * @author molszak
 *         created on 27.03.2017.
 */
@Component
public class ProbabilityMapCommand implements CommandMarker {

    @Autowired
    private ProbabilityMapCreatorController controller;

    @CliCommand(value = "create", help = "Create probability maps for images saved in project location")
    public String create(@CliOption(key = "images", help = " image directory to load files from") String imagesDir,
                         @CliOption(key = "masks", help = "mask directories to load files from") String maskDir,
                         @CliOption(key = "results", help = "directory to save the results") String resultsDir) {
        controller.process();
        return "Processing of images done";
    }


}
