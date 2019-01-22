package com.profesorp.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class KafkaTestApplication {

	public static void main(String[] args)  throws Exception 
	{
		 SpringApplication.run(KafkaTestApplication.class, args);	

	}	
	 
}
/*
@Component
class initApp implements CommandLineRunner  {
	
	@Autowired
	KafkaMessageProducer producer ;
	
    @Override
    public void run(String...args) throws Exception {
    	  producer.sendMessage("","Hello, World!");
    }
}

*/