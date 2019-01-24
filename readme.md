**Kafka** es un programa de mensajería para comunicaciones asíncronas. Básicamente la idea es que los cliente o consumidores se subscriben a un tipo de noticia  o **topic** y cuando un emisor o **broker** manda un mensaje sobre ese **topic** Kafka distribuye esa noticia a los clientes suscritos.

Hay una extensa documentación sobre **Kafka** en internet, por lo cual no voy a profundizar demasiado en su funcionamiento, ni instalación.

Para probar este programa deberemos tener un servidor funcionando con los *topics* ya definidos .  En la página [https://kafka.apache.org/quickstart](https://kafka.apache.org/quickstart) hay un manual rápido y muy claro de como levantar uno en apenas 10 minutos.

Solo aclarar dos conceptos dos conceptos básicos  de Kafka. 

1. Siempre se trabaja sobre **topics**. Poniendo un símil con la prensa escrita, un **topic** seria el periódico al que nos hemos subscrito. Solo recibiremos las ediciones (mensajes en Kafka) de ese periódico. Por supuesto una misma persona (suscriptor) mismo puede estar subscrito a muchos periódicos.

2. Los subscritores siempre forman **grupos** , aunque en el grupo no haya mas que una sola subscritor.

   Kafka se encargara que un mensaje solo sea enviado a un subscritor de un grupo.

   Hay que pensar que Kafka es una tecnología enfocada a la nube y lo normal es que un mismo programa (normalmente un microservicio) este ejecutándose en varios servidores, para tener tolerancia a fallos. Sin embargo cuando un emisor envía un mensaje de un *topic* solo queremos que ese mensaje sea procesado por una de las instancias de ese programa y no por todas las instancias. 

   De esta manera suponiendo que el mensaje implicaría realizar un apunte en una base de datos central, solo se realizaría un único apunte y no uno por cada una de las instancias.

En esta entrada voy a explicar como mandar y recibir mensajes usando  **Spring Boot** a  un servidor  Kafka. Además usaremos  **Docker** para realizar ciertas pruebas.

El fuente de  esta entrada están en [https://github.com/chuchip/KafkaTest](https://github.com/chuchip/KafkaTest)

Empezaremos creando un proyecto **Spring Boot**,  con los siguientes starters.

- Web
- kafka

El starter **Web** lo vamos  a necesitar para las pruebas que haremos, pero no lo necesitaremos en un caso *real*.

### 1. Configuración

La configuración es muy simple. Solo tendremos que poner en el fichero *application.properties*,  la dirección del servidor **Kafka** con el parámetro **spring.kafka.bootstrap-server** 

```
message.topic.name=${topicname}
message.topic.name2=${topicname2}
message.group.name=${groupid}
spring.kafka.bootstrap-servers=kafkaserver:9092
spring.kafka.consumer.group-id=myGroup
```

Si tuviéramos varios servidores **Kakfa** como suele ser el caso en producción, los indicaríamos separándolos por comas. (server1:9092,server2:9092, server3:9093 ...)

Con el parametro **spring.kafka.consumer.group-id**  podemos definir el grupo al que por defecto pertenecerán los *listeners* pero esto es configurable en cada uno de ellos y no es necesario.

Los demás parámetros los usaremos más adelante y son solo para poder realizar pruebas.

### 2. Enviando mensajes

Los mensajes los enviaremos en la clase `KafkaMessageProducer`la cual pongo a continuación.

````
@Component
public class KafkaMessageProducer {
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Value(value = "${message.topic.name:profesorp}")
	private String topicName;	
	
	public void sendMessage(String topic,String message) {
		if (topic==null || topic.trim().equals(""))
			topic=topicName;
		ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
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
````

Lo primero será pedir a Spring que nos inyecte  un objeto tipo **KafkaTemplate** . 

El *topic* por defecto, sobre el que enviaremos  los mensajes lo definimos en la variable **topicname** que, por defecto, cogerá de  **message.topic.name** establecida en el fichero *properties* de **Spring Boot**

En la función **sendMessage** sobre el *topic* mandado mandaremos los mensajes deseados.

Para ello crearemos un **ListenableFuture** a partir de **kafkaTemplate**. De esta manera la llamada al servidor de Kafka será asincrona. Para hacerla simplemente usaremos la función **addCallback** de la clase **ListenableFuture**, pasándole el interface  **ListenableFutureCallback**.

La función **onSuccess** sera ejecutada si todo va bien y la función **onFailure** en caso de error.

### 3. Recibiendo mensajes

Los mensajes los recibiremos en la clase `KafkaTestListener`

```
@Component
public class KafkaTestListener {
	 	
	  @KafkaListener(topics = "${message.topic.name:profesorp}", groupId = "${message.group.name:profegroup}")
       public void listenTopic1(String message) {
           System.out.println("Recieved Message of topic1 in  listener: " + message);
       }
	
	   @KafkaListener(topics = "${message.topic.name2:profesorp-group}", groupId = "${message.group.name:profegroup}")
	   public void listenTopic2(@Payload String message) {
		   System.out.println("Recieved Message of topic2 in  listener "+message);
	   }
}
```

En la función **listenTopic1** con la etiqueta **@KafkaListener** definimos que queremos escuchar los mensajes del **topic** definidos en la variable **message.topic.name** del fichero *properties*  de **Spring Boot**. Si esa variable  no estuviera definida, tendrá el valor `profesorp`. Además especificamos el **grupo** al que pertenece el *listener*. Recordar si no lo definimos cogera el que hayamos configurado con el parametro **spring.kafka.consumer.group-id**

En la función **listenTopic2** recibiremos los mensajes del *topic*  **message.topic.name2**.

La etiqueta **@Payload** es 

