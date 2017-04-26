package it.valeriovaudi.cqrs.repository;

import com.datastax.driver.core.utils.UUIDs;
import it.valeriovaudi.cqrs.model.command.AddGoodsToPurchaseOrderEvent;
import it.valeriovaudi.cqrs.model.command.AddShipmentsDataToPurchaseOrderEvent;
import it.valeriovaudi.cqrs.model.command.CreatePurchaseOrderEvent;
import it.valeriovaudi.cqrs.repository.command.AddGoodsToPurchaseOrderEventRepository;
import it.valeriovaudi.cqrs.repository.command.AddShipmentsDataToPurchaseOrderEventRepository;
import it.valeriovaudi.cqrs.repository.command.CreatePurchaseOrderEventRepository;
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
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommandEventsRepositoryTests {

    @Autowired
    CreatePurchaseOrderEventRepository createPurchaseOrderEventRepository;

    @Autowired
    AddGoodsToPurchaseOrderEventRepository addGoodsToPurchaseOrderEventRepository;

    @Autowired
    AddShipmentsDataToPurchaseOrderEventRepository addShipmentsDataToPurchaseOrderEventRepository;


    @Test
    public void testCreatePurchaseOrderEventRepository(){
        CreatePurchaseOrderEvent createPurchaseOrderEvent = new CreatePurchaseOrderEvent();
        createPurchaseOrderEvent.setId(UUIDs.timeBased());
        createPurchaseOrderEvent.setOrderNumber("ORDER_N000000001");
        createPurchaseOrderEvent.setCustomerFirstName("Valerio");
        createPurchaseOrderEvent.setCustomerLastName("Vaudi");
        CreatePurchaseOrderEvent save = createPurchaseOrderEventRepository.save(createPurchaseOrderEvent);

        log.info(save.toString());
        createPurchaseOrderEventRepository.findAll().forEach(System.out::println);
        Assert.assertNotNull(save);
    }

    @Test
    public void testAddGoodsToPurchaseOrderEventRepository(){
        AddGoodsToPurchaseOrderEvent addGoodsToPurchaseOrderEvent= new AddGoodsToPurchaseOrderEvent();
        addGoodsToPurchaseOrderEvent.setId(UUIDs.timeBased());
        addGoodsToPurchaseOrderEvent.setOrderNumber("ORDER_N000000001");
        addGoodsToPurchaseOrderEvent.setGoodsBarCode("TEST_BAR_CODE");
        addGoodsToPurchaseOrderEvent.setGoodsName("Penne Barilla");
        addGoodsToPurchaseOrderEvent.setGoodsPrice(new BigDecimal(0.50));
        addGoodsToPurchaseOrderEvent.setGoodsQuantity(10);
        AddGoodsToPurchaseOrderEvent  save = addGoodsToPurchaseOrderEventRepository.save(addGoodsToPurchaseOrderEvent);

        log.info(save.toString());
        createPurchaseOrderEventRepository.findAll().forEach(System.out::println);
        Assert.assertNotNull(save);
    }

    @Test
    public void testAddShipmentsDataToPurchaseOrderEventRepository(){
        AddShipmentsDataToPurchaseOrderEvent addShipmentsDataToPurchaseOrderEvent = new AddShipmentsDataToPurchaseOrderEvent();
        addShipmentsDataToPurchaseOrderEvent.setId(UUIDs.timeBased());
        addShipmentsDataToPurchaseOrderEvent.setOrderNumber("ORDER_N000000001");
        addShipmentsDataToPurchaseOrderEvent.setStreet("Via Sessa Aurunca");
        addShipmentsDataToPurchaseOrderEvent.setStreetNumber("8a");
        AddShipmentsDataToPurchaseOrderEvent save = addShipmentsDataToPurchaseOrderEventRepository.save(addShipmentsDataToPurchaseOrderEvent);

        log.info(save.toString());
        createPurchaseOrderEventRepository.findAll().forEach(System.out::println);
        Assert.assertNotNull(save);
    }
}
