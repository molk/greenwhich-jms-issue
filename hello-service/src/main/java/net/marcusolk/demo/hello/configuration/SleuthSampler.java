package net.marcusolk.demo.hello.configuration;

import brave.sampler.Sampler;
import org.springframework.stereotype.Component;

@Component
public class SleuthSampler extends Sampler {

	@Override
	public boolean isSampled(final long traceId) {
		return true;
	}

}
