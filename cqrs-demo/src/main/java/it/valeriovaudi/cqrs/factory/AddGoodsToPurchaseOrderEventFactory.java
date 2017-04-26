package it.valeriovaudi.cqrs.factory;

import com.datastax.driver.core.utils.UUIDs;
import it.valeriovaudi.cqrs.model.command.AddGoodsToPurchaseOrderEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by vvaudi on 22/04/17.
 */

@Component
public class AddGoodsToPurchaseOrderEventFactory {

    public AddGoodsToPurchaseOrderEvent newAddGoodsToPurchaseOrderEvent(String orderNumber,
                                                                        String goodsName,
                                                                        String goodsBarCode,
                                                                        BigDecimal goodsPrice,
                                                                        int goodsQuantity){
        AddGoodsToPurchaseOrderEvent addGoodsToPurchaseOrderEvent = new AddGoodsToPurchaseOrderEvent();

        addGoodsToPurchaseOrderEvent.setId(UUIDs.timeBased());
        addGoodsToPurchaseOrderEvent.setOrderNumber(orderNumber);
        addGoodsToPurchaseOrderEvent.setGoodsName(goodsName);
        addGoodsToPurchaseOrderEvent.setGoodsBarCode(goodsBarCode);
        addGoodsToPurchaseOrderEvent.setGoodsPrice(goodsPrice);
        addGoodsToPurchaseOrderEvent.setGoodsQuantity(goodsQuantity);
        return addGoodsToPurchaseOrderEvent;
    }

}
