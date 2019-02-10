package net.marcusolk.demo.hello

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles('TEST')
class ApplicationIT extends Specification {

	def 'application can be wired'() {
		expect:
		true
	}

}
