package it.valeriovaudi.cqrs.service;

import it.valeriovaudi.cqrs.model.query.QueryPurchaseOrder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * Created by vvaudi on 22/04/17.
 */

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class PurcheaseOrderServiceTests {

    @Autowired
    PurcheaseOrderService purcheaseOrderService;

    @Test
    public void testPurcheaseOrderService() throws InterruptedException {
        String orderNumber = purcheaseOrderService.createOrder("Valerio", "Vaudi");
        log.info("orderNumber: " + orderNumber);
        purcheaseOrderService.addGoodsToOrder(orderNumber,"Penne Barilla","XXXXXX",new BigDecimal(0.50), 10);
        purcheaseOrderService.addGoodsToOrder(orderNumber,"Rigatoni Barilla","XXXXXX",new BigDecimal(0.89), 10);
        purcheaseOrderService.addGoodsToOrder(orderNumber,"Passata Mutti","XXXXXX",new BigDecimal(4.50), 20);
        purcheaseOrderService.addShipmentsDataToOrder(orderNumber,"via sessa aurunca","8b");

        Thread.sleep(40000L);
        log.info("****************************************************************");
        log.info("****************************************************************");
        log.info("****************************************************************");
        log.info("****************************************************************");
        log.info("****************************************************************");
        log.info("****************************************************************");
        QueryPurchaseOrder order = purcheaseOrderService.getOrder(orderNumber);

        log.info(order.toString());
        Assert.assertNotNull(order);
    }
}
