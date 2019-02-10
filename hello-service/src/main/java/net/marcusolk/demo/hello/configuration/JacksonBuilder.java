package net.marcusolk.demo.hello.configuration;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import static com.fasterxml.jackson.databind.DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

/**
 * Configures Jackson to serialize/deserialize Json bodies.
 *
 * @see org.springframework.http.converter.json.Jackson2ObjectMapperBuilder used by Spring to configure Jackson. Not Null.
 */
@Component
public class JacksonBuilder extends org.springframework.http.converter.json.Jackson2ObjectMapperBuilder {

	public JacksonBuilder() {
		super();

		modules(new Jdk8Module(), new JavaTimeModule());
		featuresToDisable(WRITE_DATES_AS_TIMESTAMPS, ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
		indentOutput(true);
	}

}
