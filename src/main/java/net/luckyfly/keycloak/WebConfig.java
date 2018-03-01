package net.luckyfly.keycloak;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@EnableWebMvc
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	@Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer
			//.defaultContentTypeStrategy(defaultStrategy)
			.favorPathExtension(true).
            favorParameter(true).
            //ignoreAcceptHeader(true).
            defaultContentType(MediaType.TEXT_HTML).
            mediaType("xml", MediaType.APPLICATION_XML)
            .mediaType("json", MediaType.APPLICATION_JSON);
    }

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
		converters.add(converter);
		super.configureMessageConverters(converters);
	}
	
	@Bean
    public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
        resolver.setContentNegotiationManager(manager);
 
        List < ViewResolver > resolvers = new ArrayList < ViewResolver > ();
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        resolvers.add(jsonViewResolver());
 
        resolver.setViewResolvers(resolvers);
        return resolver;
    }
	
	 @Bean
	    public ViewResolver jsonViewResolver() {
	        return new JsonViewResolver();
	    }

	public class JsonViewResolver implements ViewResolver {
		 
	    public View resolveViewName(String viewName, Locale locale) throws Exception {
	 
	        MappingJackson2JsonView view = new MappingJackson2JsonView();
	        view.setPrettyPrint(true);
	        return view;
	 
	    }
	 
	}
//	@Override
//	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(converter());
//        addDefaultHttpMessageConverters(converters);
//    }
}
