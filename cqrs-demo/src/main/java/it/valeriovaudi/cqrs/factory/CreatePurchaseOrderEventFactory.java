package it.valeriovaudi.cqrs.factory;

import com.datastax.driver.core.utils.UUIDs;
import it.valeriovaudi.cqrs.model.command.CreatePurchaseOrderEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by vvaudi on 22/04/17.
 */
@Component
public class CreatePurchaseOrderEventFactory {

    public CreatePurchaseOrderEvent newCreatePurchaseOrderEvent(String customerFirstName, String customerLastName){
        CreatePurchaseOrderEvent createPurchaseOrderEvent = new CreatePurchaseOrderEvent();
        createPurchaseOrderEvent.setId(UUIDs.timeBased());
        createPurchaseOrderEvent.setOrderNumber(UUID.randomUUID().toString());
        createPurchaseOrderEvent.setCustomerFirstName(customerFirstName);
        createPurchaseOrderEvent.setCustomerLastName(customerLastName);

        return createPurchaseOrderEvent;
    }
}
