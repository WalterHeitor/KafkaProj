package br.com.alura.loja;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var producer = new KafkaProducer<String, String>(properties());
        var value = "111, 2222, 33444";
        var record = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", value, value);

        Callback callback = (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            System.out.println("Sucesso enviando " + data.topic() + " ::: particion " + data.partition() +
                    " ::: offset " + data.offset() + " / timestamp " + data.timestamp());
        };

        var email = "Thank you for your order! We are processing you order";
        var emailRecord = new ProducerRecord<>("ECOMMERCE_SEND_EMAIL", email, email);
        producer.send(record, callback).get();
        producer.send(emailRecord, callback);
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                                StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                                StringSerializer.class.getName());
        return properties;
    }
}
