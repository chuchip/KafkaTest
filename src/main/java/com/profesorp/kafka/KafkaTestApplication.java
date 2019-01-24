package com.profesorp.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaTestApplication implements  CommandLineRunner {

	public static void main(String[] args)  throws Exception 
	{
		 SpringApplication.run(KafkaTestApplication.class, args);	

	}	
	 
	@Autowired
	KafkaMessageProducer producer ;
	
    @Override
    public void run(String...args) throws Exception {
//    	  producer.sendMessage("","Hello, World!");
    }

}
