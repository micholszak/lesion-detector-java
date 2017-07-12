package pl.olszak.michal.detector.system.configuration;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoOperations;
import pl.olszak.michal.detector.controller.MapCreator;
import pl.olszak.michal.detector.controller.ProbabilityMapCreator;
import pl.olszak.michal.detector.controller.SegmentationController;
import pl.olszak.michal.detector.core.converter.ConvertedContainerCreator;
import pl.olszak.michal.detector.core.converter.ImageConverter;
import pl.olszak.michal.detector.core.converter.RegularImageConverter;
import pl.olszak.michal.detector.core.database.DatabaseFacade;
import pl.olszak.michal.detector.utils.ContainerOperations;

/**
 * @author molszak
 *         created on 23.03.2017.
 */
@Configuration
public class OperationsConfiguration {

    @Bean
    @Scope("singleton")
    public ContainerOperations fileOperations() {
        return new ContainerOperations();
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

    @Bean
    @Scope("prototype")
    public ImageConverter converter() {
        return new RegularImageConverter();
    }

    @Bean
    @Scope("prototype")
    public ConvertedContainerCreator convertedContainerCreator(ImageConverter converter) {
        return new ConvertedContainerCreator(converter);
    }

    @Bean
    @Scope("prototype")
    public MapCreator probabilityMapController(ContainerOperations containerOperations, ConvertedContainerCreator creator, DatabaseFacade databaseFacade) {
        return new ProbabilityMapCreator(containerOperations, creator, databaseFacade);
    }

    @Bean
    @Scope("prototype")
    public SegmentationController segmentationController(ContainerOperations containerOperations, ConvertedContainerCreator creator, DatabaseFacade databaseFacade) {
        return new SegmentationController(containerOperations, creator, databaseFacade);
    }

    @Bean
    @Scope("prototype")
    public DatabaseFacade databaseFacade(MongoOperations operations) {
        return new DatabaseFacade(operations);
    }
}
