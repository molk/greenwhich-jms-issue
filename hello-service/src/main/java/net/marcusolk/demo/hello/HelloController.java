package net.marcusolk.demo.hello;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public final class HelloController {

	@PostMapping(path = "/hello", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public String hello(final @RequestBody String name) {
		log.info("Received hello");
		log.info("Processed hello");

		return String.format("Hello %s", name);
	}

}
