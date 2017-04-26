package it.valeriovaudi.cqrs.handler;

import com.google.common.collect.Lists;
import it.valeriovaudi.cqrs.model.command.AddGoodsToPurchaseOrderEvent;
import it.valeriovaudi.cqrs.model.command.AddShipmentsDataToPurchaseOrderEvent;
import it.valeriovaudi.cqrs.model.command.CreatePurchaseOrderEvent;
import it.valeriovaudi.cqrs.model.query.Customer;
import it.valeriovaudi.cqrs.model.query.Goods;
import it.valeriovaudi.cqrs.model.query.QueryPurchaseOrder;
import it.valeriovaudi.cqrs.model.query.ShipmentData;
import it.valeriovaudi.cqrs.repository.query.QueryPurchaseOrderRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Created by vvaudi on 22/04/17.
 */

@Slf4j
@Component
public class QueryDataAggregatorHandler {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue addGoodsToOrderCommandQueue;

    @Autowired
    private Queue addShipmentDataToOrderCommandQueue;

    @Autowired
    private QueryPurchaseOrderRespository queryPurchaseOrderRespository;


    @RabbitListener(queues = "create-order-command-queue")
    public void createOrderHandler(CreatePurchaseOrderEvent event){
        QueryPurchaseOrder queryPurchaseOrder = new QueryPurchaseOrder();
        queryPurchaseOrder.setOrderNumber(event.getOrderNumber());

        Customer customer = Customer.builder()
                .firstName(event.getCustomerFirstName())
                .lastName(event.getCustomerLastName())
                .build();
        queryPurchaseOrder.setCustomer(customer);

        queryPurchaseOrderRespository.save(queryPurchaseOrder);
    }

    @RabbitListener(queues = "add-goods-to-order.command-queue")
    public void addGoodsToOrderCommandHandler(AddGoodsToPurchaseOrderEvent event)
            throws ExecutionException, InterruptedException {
           QueryPurchaseOrder queryPurchaseOrder =
                   queryPurchaseOrderRespository.findByOrderNumber(event.getOrderNumber());
        if(queryPurchaseOrder!=null){
             Goods goods = Goods.builder()
                    .goodsName(event.getGoodsName())
                    .goodsBarCode(event.getGoodsBarCode())
                    .goodsPrice(event.getGoodsPrice().setScale(2, RoundingMode.HALF_DOWN))
                    .goodsQuantity(event.getGoodsQuantity())
                    .build();
            List<Goods> goodsListAux = Optional.ofNullable(queryPurchaseOrder.getGoodsList())
                    .map(goodsList -> {
                        goodsList.add(goods);
                        return goodsList;
                    }).orElse(Lists.newArrayList(goods));
            queryPurchaseOrder.setGoodsList(goodsListAux);
            BigDecimal orderTotal = Optional.ofNullable(queryPurchaseOrder.getTotal()).orElse(BigDecimal.ZERO)
                    .add(goods.getGoodsPrice().multiply(BigDecimal.valueOf(goods.getGoodsQuantity())))
                    .setScale(2, RoundingMode.HALF_DOWN);
            queryPurchaseOrder.setTotal(orderTotal);

            queryPurchaseOrderRespository.save(queryPurchaseOrder);
        }else {
            rabbitTemplate.convertAndSend(addGoodsToOrderCommandQueue.getName(), event);
        }
    }

    @RabbitListener(queues = "add-shipment-to-order.command-queue")
    public void addShipmentToOrderCommandHandler(AddShipmentsDataToPurchaseOrderEvent event)
            throws ExecutionException, InterruptedException {
        QueryPurchaseOrder queryPurchaseOrder =
                queryPurchaseOrderRespository.findByOrderNumber(event.getOrderNumber());

        if (queryPurchaseOrder != null) {
            queryPurchaseOrder.setShipmentData(ShipmentData.builder().street(event.getStreet())
                    .streetNumber(event.getStreetNumber()).build());

            queryPurchaseOrderRespository.save(queryPurchaseOrder);
        } else {
            rabbitTemplate.convertAndSend(addShipmentDataToOrderCommandQueue.getName(), event);
        }
    }
}