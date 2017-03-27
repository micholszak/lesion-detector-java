package pl.olszak.michal.detector.system.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.stereotype.Component;
import pl.olszak.michal.detector.core.operations.controller.ProbabilityMapController;

/**
 * @author molszak
 *         created on 27.03.2017.
 */
@Component
public class ProbabilityMapCommand implements CommandMarker {

    @Autowired
    private ProbabilityMapController controller;

    @CliCommand(value = "train", help = "test for the sake of test")
    public String train() {
        controller.process();
        return "Done";
    }
}
