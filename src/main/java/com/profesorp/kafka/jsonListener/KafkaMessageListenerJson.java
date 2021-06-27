package com.profesorp.kafka.jsonListener;

import com.profesorp.kafka.MessageKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Profile("listener")
public class KafkaMessageListenerJson {
		@Value(value="${message.topic.json1}")
		String topicName1;


		@Autowired
        MessageKafka messageKafka;

	   @KafkaListener(topics = "${message.topic.json1}", groupId = "${message.group.name}")
       public MessageKafka listenTopic1(@Payload  Car car,
								@Header(name=KafkaHeaders.RECEIVED_MESSAGE_KEY,required=false) String messageKey,
								@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
								@Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
								@Header(name=KafkaHeaders.RECEIVED_TIMESTAMP,required=false) Long timeStamp) {
           System.out.println("Received Message of  topic: "+topicName1+ " in  listener: " + car.toString()+
				   " TimeStamp: "+ new Date(timeStamp));
           messageKafka.setCar(car);
           messageKafka.setKey(messageKey);
           messageKafka.setPartition(partition);
		   messageKafka.setTopic(topic);
		   messageKafka.setTimeStamp(new Date(timeStamp));
           messageKafka.setReceived(true);
           return messageKafka;
       }



}
