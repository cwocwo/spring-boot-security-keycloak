package net.luckyfly.keycloak;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.AbstractUrlHandlerMapping;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@EnableWebMvc
@Configuration
@EnableConfigurationProperties({ WebMvcProperties.class, ResourceProperties.class })
public class WebConfig extends WebMvcConfigurerAdapter {
	
	private static final Log logger = LogFactory.getLog(WebConfig.class);
	
	private final ResourceProperties resourceProperties;

	private final WebMvcProperties mvcProperties;
	
	public WebConfig(ResourceProperties resourceProperties,
			WebMvcProperties mvcProperties) {
		this.resourceProperties = resourceProperties;
		this.mvcProperties = mvcProperties;
	}
	
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
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (!this.resourceProperties.isAddMappings()) {
			logger.debug("Default resource handling disabled");
			return;
		}
		Integer cachePeriod = this.resourceProperties.getCachePeriod();
		if (!registry.hasMappingForPattern("/webjars/**")) {
			
					registry.addResourceHandler("/webjars/**")
							.addResourceLocations(
									"classpath:/META-INF/resources/webjars/")
					.setCachePeriod(cachePeriod);
		}
		String staticPathPattern = this.mvcProperties.getStaticPathPattern();
		if (!registry.hasMappingForPattern(staticPathPattern)) {
			
					registry.addResourceHandler(staticPathPattern)
							.addResourceLocations(
									this.resourceProperties.getStaticLocations())
					.setCachePeriod(cachePeriod);
		}
	}

	@Bean
	public WelcomePageHandlerMapping welcomePageHandlerMapping(
			ResourceProperties resourceProperties) {
		return new WelcomePageHandlerMapping(resourceProperties.getWelcomePage(),
				this.mvcProperties.getStaticPathPattern());
	}

//	@Override
//	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//		converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
//		converters.add(converter);
//		super.configureMessageConverters(converters);
//	}
//	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		MappingJackson2JsonView json2view = new MappingJackson2JsonView();
		registry.enableContentNegotiation(json2view);
		registry.freeMarker();
	}
	
//	@Bean
//    public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
//        ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
//        resolver.setContentNegotiationManager(manager);
// 
//        List < ViewResolver > resolvers = new ArrayList < ViewResolver > ();
//        resolvers.add(jsonViewResolver());
// 
//        resolver.setViewResolvers(resolvers);
//        return resolver;
//    }
	
//	 @Bean
//	    public ViewResolver jsonViewResolver() {
//	        return new JsonViewResolver();
//	    }

//	public class JsonViewResolver implements ViewResolver {
//		 
//	    public View resolveViewName(String viewName, Locale locale) throws Exception {
//	 
//	        MappingJackson2JsonView view = new MappingJackson2JsonView();
//	        view.setPrettyPrint(true);
//	        return view;
//	 
//	    }
//	 
//	}
//	@Override
//	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(converter());
//        addDefaultHttpMessageConverters(converters);
//    }
	
	static final class WelcomePageHandlerMapping extends AbstractUrlHandlerMapping {

		private static final Log logger = LogFactory
				.getLog(WelcomePageHandlerMapping.class);

		private WelcomePageHandlerMapping(Resource welcomePage,
				String staticPathPattern) {
			if (welcomePage != null && "/**".equals(staticPathPattern)) {
				logger.info("Adding welcome page: " + welcomePage);
				ParameterizableViewController controller = new ParameterizableViewController();
				controller.setViewName("forward:index.html");
				setRootHandler(controller);
				setOrder(0);
			}
		}

		@Override
		public Object getHandlerInternal(HttpServletRequest request) throws Exception {
			for (MediaType mediaType : getAcceptedMediaTypes(request)) {
				if (mediaType.includes(MediaType.TEXT_HTML)) {
					return super.getHandlerInternal(request);
				}
			}
			return null;
		}

		private List<MediaType> getAcceptedMediaTypes(HttpServletRequest request) {
			String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
			return MediaType.parseMediaTypes(
					StringUtils.hasText(acceptHeader) ? acceptHeader : "*/*");
		}

	}
}
