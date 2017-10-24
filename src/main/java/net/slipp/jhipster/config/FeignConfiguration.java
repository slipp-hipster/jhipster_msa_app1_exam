package net.slipp.jhipster.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "net.slipp.jhipster")
public class FeignConfiguration {

}
