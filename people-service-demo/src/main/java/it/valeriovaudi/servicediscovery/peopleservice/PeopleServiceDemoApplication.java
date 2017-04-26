package it.valeriovaudi.servicediscovery.peopleservice;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.*;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@EnableEurekaClient
@SpringBootApplication
@EnableJpaRepositories
public class PeopleServiceDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeopleServiceDemoApplication.class, args);
	}
}

@Component
@Transactional
class InitDataBase implements CommandLineRunner {

	final PersonRespository popleRespository;

	InitDataBase(PersonRespository popleRespository) {
		this.popleRespository = popleRespository;
	}

	@Override
	public void run(String... args) throws Exception {
		popleRespository.save(PersonBuilder.newPersonBuilder().firstName("Valerio").lastName("Vaudi").build());
		popleRespository.save(PersonBuilder.newPersonBuilder().firstName("Massimiliano").lastName("Vaudi").build());
		popleRespository.save(PersonBuilder.newPersonBuilder().firstName("Massimo").lastName("Vaudi").build());
		popleRespository.save(PersonBuilder.newPersonBuilder().firstName("Massimo").lastName("Mecella").build());

	}
}

class PersonBuilder {
	private Person person;
	public static PersonBuilder newPersonBuilder(){
		PersonBuilder personBuilder =  new PersonBuilder();
		personBuilder.setPerson(new Person());
		return personBuilder;
	}

	public PersonBuilder firstName(String firstName){
		this.person.setFirstName(firstName);
		return this;
	}

	public PersonBuilder lastName(String lastName){
		this.person.setLastName(lastName);
		return this;
	}

	public PersonBuilder id(Long id){
		this.person.setId(id);
		return this;
	}

	public Person build(){
		return this.person;
	}
	private void setPerson(Person person) {
		this.person = person;
	}
}
interface PersonRespository extends JpaRepository<Person, Long>{

}

@Data
@Entity
@Table(name = "Person")
class Person {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;

	private String firstName;
	private String lastName;
}

@Component
class PersonResourcesFactory {

	public Resources getPersonResources(List<Person> pople){
		Resources<Person> resources = new Resources<>(pople);
		resources.add(linkTo(PeopleRestEndPoint.class).withSelfRel());
		return resources;
	}
}

@RestController
@RequestMapping("/person")
class PeopleRestEndPoint {

	final PersonRespository popleRespository;
	final PersonResourcesFactory personResourcesFactory;

	PeopleRestEndPoint(PersonRespository popleRespository,
					   PersonResourcesFactory personResourcesFactory) {
		this.popleRespository = popleRespository;
		this.personResourcesFactory = personResourcesFactory;
	}


	@GetMapping
	public ResponseEntity getAllPeople(){
		return ResponseEntity.ok(personResourcesFactory.getPersonResources(popleRespository.findAll()));
	}
}