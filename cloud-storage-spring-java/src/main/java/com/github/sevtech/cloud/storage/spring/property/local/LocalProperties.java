package com.github.sevtech.cloud.storage.spring.property.local;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "storage.local")
public class LocalProperties {

	private String base;
}
