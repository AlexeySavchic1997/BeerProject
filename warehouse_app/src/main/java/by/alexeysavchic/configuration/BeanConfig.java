package by.alexeysavchic.configuration;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;


@Configuration
public class BeanConfig {

    @Bean
    public XmlMapper xmlMapper() {
        return new XmlMapper();
    }

    @Bean
    public XMLInputFactory xmlInputFactory() {
        return XMLInputFactory.newInstance();
    }

    @Bean
    public XMLOutputFactory xmlOutputFactory() {
        return XMLOutputFactory.newInstance();
    }
}
