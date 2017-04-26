package it.valeriovaudi.cqrs.factory;

import com.datastax.driver.core.utils.UUIDs;
import it.valeriovaudi.cqrs.model.command.AddShipmentsDataToPurchaseOrderEvent;
import org.springframework.stereotype.Component;

/**
 * Created by vvaudi on 22/04/17.
 */

@Component
public class AddShipmentsDataToPurchaseOrderEventFactory {

    public AddShipmentsDataToPurchaseOrderEvent newAddShipmentsDataToPurchaseOrderEvent(String orderNumber, String street, String streetNumber){
        AddShipmentsDataToPurchaseOrderEvent addShipmentsDataToPurchaseOrderEvent = new AddShipmentsDataToPurchaseOrderEvent();

        addShipmentsDataToPurchaseOrderEvent.setId(UUIDs.timeBased());
        addShipmentsDataToPurchaseOrderEvent.setOrderNumber(orderNumber);
        addShipmentsDataToPurchaseOrderEvent.setStreet(street);
        addShipmentsDataToPurchaseOrderEvent.setStreetNumber(streetNumber);
        return addShipmentsDataToPurchaseOrderEvent;
    }

}
