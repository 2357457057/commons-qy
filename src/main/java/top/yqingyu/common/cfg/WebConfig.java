package top.yqingyu.common.cfg;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.cfg.WebConfig
 * @Description 更换JSON解析器
 * @createTime 2022年07月07日 00:58:00
 */
@Configuration
@EnableWebMvc
// 必须要有这个注解，否则报错：JSON parse error: Cannot deserialize value of type `java.time.LocalDateTime` from String \"2020-06-04 15:07:54\": Failed to deserialize java.time.LocalDateTime: (java.time.format.DateTimeParseException) Text '2020-06-04 15:07:54' could not be parsed at index 10; nested exception is com.fasterxml.jackson.databind.exc.InvalidFormatException: Cannot deserialize value of type `java.time.LocalDateTime` from String \"2020-06-04 15:07:54\": Failed to deserialize java.time.LocalDateTime: (java.time.format.DateTimeParseException) Text '2020-06-04 15:07:54' could not be parsed at index 10\n at [Source: (PushbackInputStream); line: 5, column: 17] (through reference chain: com.heartsuit.domain.Book[\"publishDate\"]
public class WebConfig implements WebMvcConfigurer {
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setCharset(StandardCharsets.UTF_8);
        config.setDateFormat("yyyyMMdd HH:mm:ss.SSS");
        config.setWriterFeatures(JSONWriter.Feature.BeanToArray);
        config.setReaderFeatures(JSONReader.Feature.SupportSmartMatch);
        fastJsonConverter.setFastJsonConfig(config);
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON);
        fastJsonConverter.setSupportedMediaTypes(list);
        converters.add(fastJsonConverter);
    }
}
