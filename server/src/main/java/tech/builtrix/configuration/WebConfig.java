package tech.builtrix.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.builtrix.context.ContextHandlerInterceptor;

/**
 * Created By sahar-hoseini at 12. Jul 2019 5:53 PM
 **/

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private final ContextHandlerInterceptor contextHandlerInterceptor;

    private final CorsInterceptor corsInterceptor;

    public WebConfig(ContextHandlerInterceptor contextHandlerInterceptor, CorsInterceptor corsInterceptor) {
        this.contextHandlerInterceptor = contextHandlerInterceptor;
        this.corsInterceptor = corsInterceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.corsInterceptor);
        registry.addInterceptor(this.contextHandlerInterceptor);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheMillis(10000);

        messageSource.setBasenames(
                "classpath:localizations/validation",
                "classpath:localizations/messages",
                "classpath:localizations/errors");

        return messageSource;
    }
}