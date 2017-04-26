package it.valeriovaudi.cqrs.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by vvaudi on 22/04/17.
 */

@Configuration
public class MessagingConfig {

    @Bean
    public Queue createOrderCommandQueue() {
        return new Queue("create-order-command-queue");
    }

    @Bean
    public Queue addGoodsToOrderCommandQueue() {
        return new Queue("add-goods-to-order.command-queue");
    }

    @Bean
    public Queue addShipmentDataToOrderCommandQueue() {
        return new Queue("add-shipment-to-order.command-queue");
    }
}
