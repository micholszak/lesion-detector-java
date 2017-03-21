package pl.olszak.michal.detector.system;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultPromptProvider;
import org.springframework.stereotype.Component;

/**
 * @author molszak
 *         created on 21.03.2017.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PromptProvider extends DefaultPromptProvider {

    @Override
    public String getPrompt() {
        return "console>";
    }

    @Override
    public String getProviderName() {
        return PromptProvider.class.getSimpleName();
    }
}
