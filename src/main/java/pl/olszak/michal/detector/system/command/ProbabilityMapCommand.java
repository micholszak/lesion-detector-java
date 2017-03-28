package pl.olszak.michal.detector.system.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
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
    public String create() {
        controller.process();
        return "Processing of images done";
    }


}
