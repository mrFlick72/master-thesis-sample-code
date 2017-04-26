package it.valeriovaudi.be4f4;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.schedulers.Schedulers;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@EnableHystrix
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class Be4f4DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(Be4f4DemoApplication.class, args);
	}

	/**
	 * bulkhead pattern:
	 * http side protection against failure
	 * */
	@Bean
	@LoadBalanced
	public RestTemplate peopleServiceLoadBalancer(){
		return new RestTemplate();
	}

	@Bean
	@LoadBalanced
	public RestTemplate helloServiceLoadBalancer(){
		return new RestTemplate();
	}
}

@Slf4j
@RestController
class HelloAtAllRestEndPoint {

	@Autowired
	private HelloAtAllService helloAtAllService;

	@GetMapping(name = "/hello")
	public ResponseEntity getAllAtEveryOne() throws IOException {
		return ResponseEntity.ok(helloAtAllService.getHelloMessage());
	}
}

@Slf4j
@Component
class HelloAtAllService {

	@Autowired
	private ServiceProxyService serviceProxyService;

	/* We use RxJava for better performance and thread usage. Hystrix behind the scenes already use RxJava
	  for this propose. Every serviceProxyService.getHello(...) service call, is performed on a separated thread,
	  it is possible due to the Observable.create calls subscriber::onNext on a separated thread thanks to
	  Java 8 parallelStream abstraction. The subscriber will be called when the service push a notification
	  from the service whit the response  */
	@HystrixCommand(defaultFallback = "getHelloMessageFallback")
	public String getHelloMessage() throws IOException {
		List<Map> allPeople = serviceProxyService.getAllPeople();
		BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(allPeople.size());

		log.info("start");
		Observable.create(subscriber -> {
			allPeople.parallelStream().forEach(subscriber::onNext);
			subscriber.onCompleted();
		})
		.doOnCompleted(()->log.info("finish"))
		 .subscribe(o -> {
		 	 log.info("current thread is : " + Thread.currentThread().getId());
			 blockingQueue.add(serviceProxyService
				 .getHello(String.valueOf(((Map) o).get("firstName"))));
		 });

		log.info("end");
		return blockingQueue.stream()
				.map(item -> Json.createArrayBuilder().add(item))
				.reduce(Json.createArrayBuilder(),
						(array1, array2) -> {array1.add(array2.build()
								.stream().findFirst().get()); return array1;})
				.build().toString();
	}

	public String getHelloMessageFallback(){
		log.info("getHelloMessageFallback is fired");
		return "";
	}
}

@Slf4j
@Component
class ServiceProxyService {

	@Autowired
	private  RestTemplate peopleServiceLoadBalancer, helloServiceLoadBalancer;

	@Autowired
	private  ObjectMapper objectMapper;

	/**
	 * bulkhead pattern:
	 * thread side protection against failure
	 * Circuit braker pattern with hystrix
	 * */
	@HystrixCommand(defaultFallback = "getHelloFallback")
	public String getHello(String name){
		String url = String.format("http://MICROSERVICE-CLIENT-TEST/hello/%s", name);
		return helloServiceLoadBalancer.getForObject(url,String.class);
	}

	@HystrixCommand(defaultFallback = "getAllPeopleFallback")
	public List<Map> getAllPeople() throws IOException {
		String resource = peopleServiceLoadBalancer.getForObject("http://PEOPLE-SERVICE/person.json", String.class);
		String json = Json.createReader(new StringReader(resource))
			.readObject().getJsonObject("_embedded").getJsonArray("personList").toString();
		return objectMapper.readValue(json, List.class);
	}

	public String getHelloFallback(){
		log.info("getHelloFallback is fired");
		return "";
	}

	public List<Map> getAllPeopleFallback(){
		log.info("getAllPeopleFallback is fired");
		return new ArrayList<>();
	}
}