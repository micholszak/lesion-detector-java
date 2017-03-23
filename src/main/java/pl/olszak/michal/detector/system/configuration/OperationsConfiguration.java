package pl.olszak.michal.detector.system.configuration;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import pl.olszak.michal.detector.utils.FileOperations;

/**
 * @author molszak
 *         created on 23.03.2017.
 */
@Configuration
public class OperationsConfiguration {

    @Bean
    @Scope("singleton")
    public FileOperations fileOperations() {
        return new FileOperations();
    }

    @Bean
    @Scope("prototype")
    public Gson gson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .create();
    }
}
