package tech.builtrix.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.builtrix.context.ContextHandlerInterceptor;
import tech.builtrix.context.RequestLimitInterceptor;
import tech.builtrix.context.SecurityInterceptor;

/**
 * Created By sahar-hoseini at 12. Jul 2019 5:53 PM
 **/

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ContextHandlerInterceptor contextHandlerInterceptor;
    @Autowired
    private SecurityInterceptor securityInterceptor;
    @Autowired
    private CorsInterceptor corsInterceptor;
    @Autowired
    private RequestLimitInterceptor requestLimitInterceptor;
   /* @Autowired
    private RequestLogger requestLogger;
*/

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.requestLimitInterceptor);
        registry.addInterceptor(this.corsInterceptor);
        registry.addInterceptor(this.contextHandlerInterceptor);
        registry.addInterceptor(this.securityInterceptor);
    }

    @Bean(name = "validator")
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Override
    public Validator getValidator() {
        return validator();
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