import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class OrderDeserializer implements Deserializer<dom.order.status.v0.Order> {

    private final KafkaAvroDeserializer kafkaAvroDeserializer;

    public OrderDeserializer() {
        this.kafkaAvroDeserializer = new KafkaAvroDeserializer();
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        kafkaAvroDeserializer.configure(configs, isKey);
    }

    @Override
    public dom.order.status.v0.Order deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        Object deserialized = kafkaAvroDeserializer.deserialize(topic, data);

        if (deserialized instanceof dom.order.status.v0.Order) {
            return (dom.order.status.v0.Order) deserialized;
        } else if (deserialized instanceof GenericRecord) {
            // Convert GenericRecord to Order if needed
            GenericRecord record = (GenericRecord) deserialized;
            // You could implement conversion logic here if needed
            throw new RuntimeException("Received GenericRecord instead of specific Order record. " +
                    "Make sure 'specific.avro.reader' is set to true in consumer config.");
        }

        throw new RuntimeException("Deserialized object is not an Order or GenericRecord: " +
                (deserialized != null ? deserialized.getClass().getName() : "null"));
    }

    @Override
    public void close() {
        kafkaAvroDeserializer.close();
    }
}
