package com.profesorp.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class KafkaMessageProducer {
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Value("${message.topic.name:profesorp}")
	private String topicName;	
	
	public void sendMessage(String topic,String key,String message) {
		if (topic==null || topic.trim().equals(""))
			topic=topicName;
		ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic,key, message);

		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
			@Override
			public void onSuccess(SendResult<String, String> result) {
				System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
			}
			@Override
			public void onFailure(Throwable ex) {System.err.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
			}
		});
	}	
}

