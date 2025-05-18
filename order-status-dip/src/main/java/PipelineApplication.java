import dom.order.status.v0.Order;
import dom.order.status.v0.Order_Type;
import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PipelineApplication {
    private static final String ADHOC_ORDER_TYPE = "ADHOC";
    private static final String ENTERPRISE_ORDER_TYPE = "ENTERPRISE";

    public static void main(String[] args) {
        Pipeline pipeline = Pipeline.create(
                PipelineOptionsFactory.fromArgs(args).withValidation().create()
        );

        Map<String, Object> consumerConfig = new HashMap<>();
        consumerConfig.put("bootstrap.servers", "localhost:9092");
        consumerConfig.put("specific.avro.reader", true);
        consumerConfig.put("schema.registry.url", "localhost:8081");

        // Read orders from Kafka topic
        PCollection<Order> orders = pipeline.apply(
                KafkaIO.<String, Order>read()
                        .withBootstrapServers("localhost:9092")
                        .withTopic("dom.order.status.0")
                        .withKeyDeserializer(StringDeserializer.class)
                        .withValueDeserializer(OrderDeserializer.class)
                        .withConsumerConfigUpdates(consumerConfig)
                        .withoutMetadata()
        ).apply("ExtractValues", MapElements.via(new SimpleFunction<KV<String, Order>, Order>() {
            @Override
            public Order apply(KV<String, Order> kv) {
                return kv.getValue();
            }
        }));

        // Process orders based on their type
        orders.apply("ProcessOrdersByType", ParDo.of(new ProcessOrdersByTypeFn()));

        pipeline.run().waitUntilFinish();
    }

    /**
     * DoFn that processes orders based on their type and sends them to the appropriate service
     */
    private static class ProcessOrdersByTypeFn extends DoFn<Order, Void> {
        private transient HttpClient httpClient;

        @Setup
        public void setup() {
            httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
        }

        @ProcessElement
        public void processElement(@Element Order order) {
            Order_Type orderType = order.getOrder_Type();
            String responseBody;
            int statusCode;
            String orderId = String.valueOf(order);

            try {
                if (ADHOC_ORDER_TYPE.equals(orderType)) {
                    log.info("Processing ADHOC order: {}", order);
                    // Call ADHOC order processing service
                    HttpResponse<String> response = callProcessingService(
                            "http://localhost:8090/customer/process-order",
                            order
                    );
                    statusCode = response.statusCode();
                    responseBody = response.body();

                    if (statusCode == 200) {
                        // Write successful response to the ADHOC topic
                        writeToKafkaTopic("dom.order.customer.0", orderId, responseBody);
                    } else {
                        // Write error to error topic
                        writeToKafkaTopic("dom.order.error.0", orderId,
                                "Error processing ADHOC order: " + statusCode + " - " + responseBody);
                    }
                } else if (ENTERPRISE_ORDER_TYPE.equals(orderType)) {
                    log.info("Processing ENTERPRISE order: {}", order);
                    // Call ENTERPRISE order processing service
                    HttpResponse<String> response = callProcessingService(
                            "http://localhost:8090/enterprise/process-order",
                            order
                    );
                    statusCode = response.statusCode();
                    responseBody = response.body();

                    if (statusCode == 200) {
                        // Write successful response to the ENTERPRISE topic
                        writeToKafkaTopic("dom.order.enterprise.0", orderId, responseBody);
                    } else {
                        // Write error to error topic
                        writeToKafkaTopic("dom.order.error.0", orderId,
                                "Error processing ENTERPRISE order: " + statusCode + " - " + responseBody);
                    }
                } else {
                    log.warn("Unknown order type: {}", orderType);
                    writeToKafkaTopic("dom.order.error.0", orderId,
                            "Unknown order type: " + orderType);
                }
            } catch (Exception e) {
                log.error("Error processing order", e);
                writeToKafkaTopic("dom.order.error.0", (String) order.getOrderId(),
                        "Exception processing order: " + e.getMessage());
            }
        }

        private HttpResponse<String> callProcessingService(String url, Order order) throws IOException, InterruptedException {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(convertOrderToJson(order)))
                    .timeout(Duration.ofSeconds(30))
                    .build();

            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        }

        private String convertOrderToJson(Order order) {
            // In a real application, use a proper JSON serializer library like Jackson or Gson
            // This is a simplified example
            return String.format(
                    "{\"orderId\":\"%s\",\"orderType\":\"%s\"}",
                    order.getOrderId(),
                    order.getOrderType()
            );
        }

        private void writeToKafkaTopic(String topic, String key, String value) {
            Map<String, Object> producerConfig = new HashMap<>();
            producerConfig.put("bootstrap.servers", "localhost:9092");

            try {
                KafkaIO.<String, String>write()
                        .withBootstrapServers("localhost:9092")
                        .withTopic(topic)
                        .withKeySerializer(StringSerializer.class)
                        .withValueSerializer(StringSerializer.class)
                        .withProducerConfigUpdates(producerConfig)
                        .withInputTimestamp()
                        .values();

                log.info("Successfully wrote to Kafka topic: {}", topic);
            } catch (Exception e) {
                log.error("Error writing to Kafka topic: {}", topic, e);
            }
        }
    }
}