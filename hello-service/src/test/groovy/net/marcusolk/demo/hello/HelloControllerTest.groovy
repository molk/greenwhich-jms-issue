package net.marcusolk.demo.hello


import net.marcusolk.demo.hello.HelloController
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

@JsonTest
@ActiveProfiles('TEST')
class HelloControllerTest extends Specification {

	@Subject
	def addressNormalizationController = new HelloController()
	def mockMvc = standaloneSetup(addressNormalizationController).build()

	def 'normalize request'() {
		given:
		def name = 'Marvin'

		when:
		def response = mockMvc.perform(
			post('/hello')
				.contentType(TEXT_PLAIN_VALUE)
				.accept(TEXT_PLAIN_VALUE)
				.content(name))
			.andReturn().response

		then:
		response.status == OK.value()
		response.contentAsString == "Hello $name"
	}

}
