package com.profesorp.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Profile("listener")
public class KafkaMessageListener {
		@Value(value="${message.topic.name1}")
		String topicName1;
		@Value(value="${message.topic.name2}")
		String topicName2;
		@Value(value="${message.topic.name3}")
		String topicName3;

		@Autowired
		MessageKafka messageKafka;

	   @org.springframework.kafka.annotation.KafkaListener(topics = "${message.topic.name1}", groupId = "${message.group.name}")
       public void listenTopic1(@Payload  String message,
								@Header(name=KafkaHeaders.RECEIVED_MESSAGE_KEY,required=false) String messageKey,
								@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
								@Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
								@Header(name=KafkaHeaders.RECEIVED_TIMESTAMP,required=false) Long timeStamp) {
           System.out.println("Received Message of  topic: "+topicName1+ " in  listener: " + message+
				   " TimeStamp: "+ new Date(timeStamp));
           messageKafka.setMessage(message);
           messageKafka.setKey(messageKey);
           messageKafka.setPartition(partition);
		   messageKafka.setTopic(topic);
		   messageKafka.setTimeStamp(new Date(timeStamp));
           messageKafka.setReceived(true);
       }

	@org.springframework.kafka.annotation.KafkaListener(groupId = "${message.group.name}",topics = "${message.topic.name2}")
	public void listenTopic3(@Payload  String message,
							 @Header(name=KafkaHeaders.RECEIVED_MESSAGE_KEY,required=false) String messageKey,
							 @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
							 @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
							 @Header(name=KafkaHeaders.RECEIVED_TIMESTAMP,required=false) Long timeStamp) {
		System.out.println("Received Message of  topic: "+topicName2+ " in  listener: " + message+
				" TimeStamp: "+ new Date(timeStamp));
		messageKafka.setMessage(message);
		messageKafka.setKey(messageKey);
		messageKafka.setPartition(partition);
		messageKafka.setTopic(topic);
		messageKafka.setTimeStamp(new Date(timeStamp));
		messageKafka.setReceived(true);
	}
	   @org.springframework.kafka.annotation.KafkaListener(/*groupId = "${message.group.name:profegroup}", NO SE PUEDE PONER EN MISMO GRUPO SI ELEGIMOS PARTITION*/
			   topicPartitions =	   { @TopicPartition(topic = "${message.topic.name3}",
							   partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))})
	   public void listenTopic2(@Payload  String message,
								@Header(name=KafkaHeaders.RECEIVED_MESSAGE_KEY,required=false) String messageKey,
								@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
								@Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
								@Header(name=KafkaHeaders.RECEIVED_TIMESTAMP,required=false) Long timeStamp) {
		   System.out.println("Received Message of "+topicName3+" in  listener:  "+message+ " Key: "+messageKey);
		   messageKafka.setMessage(message);
		   messageKafka.setKey(messageKey);
		   messageKafka.setPartition(partition);
		   messageKafka.setTopic(topic);
		   messageKafka.setReceived(true);
		   messageKafka.setTimeStamp(new Date(timeStamp));

	   }

}
