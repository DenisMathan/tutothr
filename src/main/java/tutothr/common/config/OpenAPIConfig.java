package tutothr.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("tutothr@gmail.com");
        contact.setName("TutOTHr Support");
        contact.setUrl("https://www.tutothr.de");

        License license = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("TutOTHr API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage tutorials and bookings.")
                .license(license);

        return new OpenAPI().info(info);
    }
}
