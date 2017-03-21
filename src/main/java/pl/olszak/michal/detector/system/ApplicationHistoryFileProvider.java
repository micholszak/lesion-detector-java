package pl.olszak.michal.detector.system;

import org.apache.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.HistoryFileNameProvider;
import org.springframework.stereotype.Component;

/**
 * @author molszak
 *         created on 21.03.2017.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationHistoryFileProvider implements HistoryFileNameProvider {

    @Override
    public String getHistoryFileName() {
        return "lesion-detector-shell-file.log";
    }

    @Override
    public String getProviderName() {
        return ApplicationHistoryFileProvider.class.getSimpleName();
    }
}
