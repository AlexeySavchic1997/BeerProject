package by.alexeysavchic.configuration;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfig
{
    @Bean
    public XmlMapper xmlMapper()
    {
        return new XmlMapper();
    }
}
