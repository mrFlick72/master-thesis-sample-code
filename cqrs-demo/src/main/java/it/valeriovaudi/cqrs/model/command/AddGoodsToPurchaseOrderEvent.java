package it.valeriovaudi.cqrs.model.command;

import it.valeriovaudi.cqrs.model.AbstractEventModel;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.cassandra.mapping.Table;

import java.math.BigDecimal;

/**
 * Created by vvaudi on 22/04/17.
 */

@Data
@Table
@ToString
public class AddGoodsToPurchaseOrderEvent extends AbstractEventModel {

    private String orderNumber;

    private String goodsName;
    private String goodsBarCode;
    private BigDecimal goodsPrice;
    private int goodsQuantity;
}
