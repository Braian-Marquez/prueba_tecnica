package com.tsg.gateway.setups;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalPostFiltering {

	final Logger log = LoggerFactory.getLogger(GlobalPostFiltering.class);

	@Bean
	GlobalFilter postGlobalFilter() {
		return (exchange, chain) -> {
			return chain.filter(exchange).doOnSuccess(v -> log.info("Global Post Filter executed successfully."))
					.doOnError(e -> log.error("Error in Global Post Filter execution", e));
		};
	}

}
