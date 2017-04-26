package it.valeriovaudi.servicediscovery.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@EnableEurekaClient
@SpringBootApplication
public class MicroserviceSampleClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceSampleClientApplication.class, args);
	}
}


@RestController
class RestTest {

	@RequestMapping("/hello/{name}")
	private ResponseEntity getHello(@PathVariable("name") String name){
		return ResponseEntity.ok(String.format("hello: %s", name));
	}
}
