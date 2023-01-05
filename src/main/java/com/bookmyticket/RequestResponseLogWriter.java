package com.bookmyticket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zalando.logbook.Correlation;
import org.zalando.logbook.HttpLogWriter;
import org.zalando.logbook.Precorrelation;

@Component
public class RequestResponseLogWriter implements HttpLogWriter {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean isActive() {
		return logger.isInfoEnabled();
	}

	@Override
	public void write(final Precorrelation precorrelation, final String request) {
		logger.info(request);
	}

	@Override
	public void write(final Correlation correlation, final String response) {
		logger.info(response);
	}

}