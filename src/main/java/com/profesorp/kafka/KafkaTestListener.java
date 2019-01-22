package com.profesorp.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component

public class KafkaTestListener {
	 
	  
	   @KafkaListener(topics = "${filtered.topic.name}")
       public void listenTopico(String message) {
           System.out.println("Recieved Message in filtered listener: " + message);
//           this.filterLatch.countDown();
       }
	   @KafkaListener(topics = "baeldung")
	    public void receiveTopic1(ConsumerRecord<?, ?> consumerRecord) {
	        System.out.println("Receiver on topic1: "+consumerRecord.toString());
	}
	   @KafkaListener(topics = "${filtered.topic.name}", groupId = "${kafka.groupId}")
	    public void listen(@Payload String message) {
		   System.out.println("Receiver on topic1: "+message);
	   }
}
