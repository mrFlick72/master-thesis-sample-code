package it.valeriovaudi.cqrs.repository;

import com.datastax.driver.core.utils.UUIDs;
import com.google.common.collect.Lists;
import it.valeriovaudi.cqrs.model.query.Customer;
import it.valeriovaudi.cqrs.model.query.Goods;
import it.valeriovaudi.cqrs.model.query.QueryPurchaseOrder;
import it.valeriovaudi.cqrs.model.query.ShipmentData;
import it.valeriovaudi.cqrs.repository.query.QueryPurchaseOrderRespository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * Created by vvaudi on 22/04/17.
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class QureyEventsRepositoryTests {

    @Autowired
    QueryPurchaseOrderRespository queryPurchaseOrderRespository;

    @Test
    public void testQueryPurchaseOrderRespository(){
        QueryPurchaseOrder queryPurchaseOrder = new QueryPurchaseOrder();
        queryPurchaseOrder.setId(UUIDs.timeBased().toString());
        queryPurchaseOrder.setOrderNumber("ORDER_N000000001");

        Customer customer = Customer.builder().firstName("Valerio").lastName("Vaudi").build();
        queryPurchaseOrder.setCustomer(customer);

        ShipmentData shipmentData = ShipmentData.builder().street("via sessa aurunca").streetNumber("8a").build();
        queryPurchaseOrder.setShipmentData(shipmentData);

        Goods goods = Goods.builder().goodsBarCode("BAR_CODE").goodsName("Penne Barilla")
        .goodsPrice(new BigDecimal(0.50).setScale(2, RoundingMode.HALF_DOWN)).goodsQuantity(10).build();

        queryPurchaseOrder.setGoodsList(Lists.newArrayList(goods));
        queryPurchaseOrder.setTotal(goods.getGoodsPrice().multiply(new BigDecimal(goods.getGoodsQuantity())).setScale(2, RoundingMode.HALF_DOWN));
        QueryPurchaseOrder save = queryPurchaseOrderRespository.save(queryPurchaseOrder);
        log.info(save.toString());
        Assert.assertNotNull(save);
    }
}
