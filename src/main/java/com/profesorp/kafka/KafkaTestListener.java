package com.profesorp.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaTestListener {
	 	
	   @KafkaListener(topics = "${message.topic.name:profesorp}", groupId = "${message.group.name:profegroup}")
       public void listenTopic1(String message) {
           System.out.println("Recieved Message of topic1 in  listener: " + message);
       }
	   @KafkaListener(topics = "${message.topic.name2:profesorp2}", groupId = "${message.group.name:profegroup}")
	   public void listenTopic2(String message) {
		   System.out.println("Recieved Message of topic2 in  listenerr:  "+message);
	   }
}
