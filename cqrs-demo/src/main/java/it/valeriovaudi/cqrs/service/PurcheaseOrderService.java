package it.valeriovaudi.cqrs.service;

import it.valeriovaudi.cqrs.factory.AddGoodsToPurchaseOrderEventFactory;
import it.valeriovaudi.cqrs.factory.AddShipmentsDataToPurchaseOrderEventFactory;
import it.valeriovaudi.cqrs.factory.CreatePurchaseOrderEventFactory;
import it.valeriovaudi.cqrs.model.command.AddGoodsToPurchaseOrderEvent;
import it.valeriovaudi.cqrs.model.command.AddShipmentsDataToPurchaseOrderEvent;
import it.valeriovaudi.cqrs.model.command.CreatePurchaseOrderEvent;
import it.valeriovaudi.cqrs.model.query.QueryPurchaseOrder;
import it.valeriovaudi.cqrs.repository.command.AddGoodsToPurchaseOrderEventRepository;
import it.valeriovaudi.cqrs.repository.command.AddShipmentsDataToPurchaseOrderEventRepository;
import it.valeriovaudi.cqrs.repository.command.CreatePurchaseOrderEventRepository;
import it.valeriovaudi.cqrs.repository.query.QueryPurchaseOrderRespository;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by vvaudi on 22/04/17.
 */

@Service
public class PurcheaseOrderService {

    @Autowired
    private Queue createOrderCommandQueue, addGoodsToOrderCommandQueue, addShipmentDataToOrderCommandQueue;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CreatePurchaseOrderEventFactory createPurchaseOrderEventFactory;

    @Autowired
    private AddGoodsToPurchaseOrderEventFactory addGoodsToPurchaseOrderEventFactory;

    @Autowired
    private AddShipmentsDataToPurchaseOrderEventFactory addShipmentsDataToPurchaseOrderEventFactory;

    @Autowired
    private CreatePurchaseOrderEventRepository createPurchaseOrderEventRepository;

    @Autowired
    private AddGoodsToPurchaseOrderEventRepository addGoodsToPurchaseOrderEventRepository;

    @Autowired
    private AddShipmentsDataToPurchaseOrderEventRepository addShipmentsDataToPurchaseOrderEventRepository;

    @Autowired
    private QueryPurchaseOrderRespository queryPurchaseOrderRespository;

    public String createOrder(String customerFirstName, String customerLastName){
        CreatePurchaseOrderEvent event = createPurchaseOrderEventFactory
                        .newCreatePurchaseOrderEvent(customerFirstName, customerLastName);

        createPurchaseOrderEventRepository.save(event);

        rabbitTemplate.convertAndSend(createOrderCommandQueue.getName(), event);
        return event.getOrderNumber();
    }

    public QueryPurchaseOrder getOrder(String orderNumber){
        return queryPurchaseOrderRespository.findByOrderNumber(orderNumber);
    }

    public void addGoodsToOrder(String orderNumber, String goodsName, String goodsBarCode,
                                BigDecimal goodsPrice, int goodsQuantity){
        AddGoodsToPurchaseOrderEvent event = addGoodsToPurchaseOrderEventFactory
                .newAddGoodsToPurchaseOrderEvent(orderNumber, goodsName,
                        goodsBarCode, goodsPrice, goodsQuantity);

        addGoodsToPurchaseOrderEventRepository.save(event);
        rabbitTemplate.convertAndSend(addGoodsToOrderCommandQueue.getName(), event);
    }

    public void addShipmentsDataToOrder(String orderNumber, String street, String streetNumber){
        AddShipmentsDataToPurchaseOrderEvent event =  addShipmentsDataToPurchaseOrderEventFactory
                        .newAddShipmentsDataToPurchaseOrderEvent(orderNumber, street, streetNumber);

        addShipmentsDataToPurchaseOrderEventRepository.save(event);
        rabbitTemplate.convertAndSend(addShipmentDataToOrderCommandQueue.getName(), event);
    }
}