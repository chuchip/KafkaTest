package com.profesorp.kafka.jsonListener;


import com.profesorp.kafka.KafkaMessageProducer;
import com.profesorp.kafka.MessageKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaControllerJson {
	@Autowired
    MessageKafka messageKafka;
	@Autowired
	private KafkaTemplate<String, Car> kafkaTemplate;


	@PostMapping("/json/{topic}/{key}")
	public MessageKafka addJsonMessage( @PathVariable String topic,@PathVariable String key,
									   @RequestBody  Car body) throws InterruptedException
	{
		ListenableFuture<SendResult<String, Car>> future = kafkaTemplate.send(topic,key, body);

		future.addCallback(new ListenableFutureCallback<SendResult<String, Car>>() {
			@Override
			public void onSuccess(SendResult<String, Car> result) {
				System.out.println("Sent message=[" + body.getMarca() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
			}
			@Override
			public void onFailure(Throwable ex) {System.err.println("Unable to send message=[" + body.getMarca() + "] due to : " + ex.getMessage());
			}
		});
		waitMessage();
		return messageKafka;
	}
	private void waitMessage() throws InterruptedException
	{
		long initialDate=System.currentTimeMillis();
		long finalDate=initialDate+1000*10; // Add 10 seconds
		while (!messageKafka.isReceived() && finalDate>System.currentTimeMillis())
		{
			Thread.currentThread().sleep(500);
		}
	}
}
