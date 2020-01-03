package tech.builtrix.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import tech.builtrix.context.ContextHandlerInterceptor;
import tech.builtrix.context.RequestLimitInterceptor;
import tech.builtrix.context.SecurityInterceptor;

import java.util.Locale;

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
    /*@Autowired
    private CorsInterceptor corsInterceptor;*/
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
        registry.addResourceHandler("/resources/**").addResourceLocations("/", "/resources/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.requestLimitInterceptor);
        //registry.addInterceptor(this.corsInterceptor);
        registry.addInterceptor(this.contextHandlerInterceptor);
        registry.addInterceptor(this.securityInterceptor);
    }

   /* @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }*/

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

   /* @Bean(name = "validator")
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }*/

    @Bean
    public LocaleResolver localeResolver() {
        final CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        return cookieLocaleResolver;
    }

    @Override
    public Validator getValidator() {
        return null;
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