package com.wing.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * spring mvc config
 * <p/>
 * Created by wingzhao on 14-2-15.
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.wing.*.controller")
public class MvcConfig extends WebMvcConfigurerAdapter {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(stringHttpMessageConverter());
        converters.add(jackson2HttpMessageConverter());
        super.configureMessageConverters(converters);
    }


    /**
     * auto convert object 2 json by jackson
     * 
     * @return
     */
    private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper m = new com.fasterxml.jackson.databind.ObjectMapper();
        mappingJackson2HttpMessageConverter.setObjectMapper(m);
        return mappingJackson2HttpMessageConverter;
    }

    /**
     * setup string charset to utf8
     *
     * @return
     */
    private StringHttpMessageConverter stringHttpMessageConverter () {
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(UTF8);
        stringHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(new MediaType("text", "plain", UTF8)));
        return stringHttpMessageConverter;
    }
}
