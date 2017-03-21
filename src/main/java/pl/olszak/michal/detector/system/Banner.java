package pl.olszak.michal.detector.system;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultBannerProvider;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

/**
 * @author molszak
 *         created on 21.03.2017.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class Banner extends DefaultBannerProvider {

    @Override
    public String getBanner() {
        String banner = "Lesion Detector Application" + OsUtils.LINE_SEPARATOR +
                "Masters Degree graduate project" + OsUtils.LINE_SEPARATOR +
                "Author Michal Olszak" + OsUtils.LINE_SEPARATOR +
                "Version: " + getVersion();
        return banner;
    }

    @Override
    public String getWelcomeMessage() {
        return "Hello, this application will probably change to sprint fxml integration but not in this stage";
    }

    @Override
    public String getProviderName() {
        return "Lesion Detector Application";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }
}
