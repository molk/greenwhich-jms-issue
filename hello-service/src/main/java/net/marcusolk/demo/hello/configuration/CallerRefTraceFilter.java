package net.marcusolk.demo.hello.configuration;

import brave.Span;
import brave.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.sleuth.instrument.web.TraceWebServletAutoConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

import static brave.propagation.ExtraFieldPropagation.set;
import static java.util.Optional.of;

@Component
@RequiredArgsConstructor
@Order(TraceWebServletAutoConfiguration.TRACING_FILTER_ORDER + 1)
public class CallerRefTraceFilter extends GenericFilterBean {
	public static final String X_CALLER_REF = "x-caller-ref";

	private final Tracer tracer;

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {

		final Span span = this.tracer.currentSpan();

		if (span != null) {
			extractCallerRefHeader(servletRequest)
				.ifPresent(callerRef -> {
					set(span.context(), X_CALLER_REF, callerRef);

					// start new scope to propagate new extra fields to log context
					tracer.withSpanInScope(span).close();
				});
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}

	private static Optional<String> extractCallerRefHeader(final ServletRequest servletRequest) {
		return of(servletRequest)
			.filter(HttpServletRequest.class::isInstance)
			.map(HttpServletRequest.class::cast)
			.map(httpServletRequest -> httpServletRequest.getHeader(X_CALLER_REF));
	}
}
