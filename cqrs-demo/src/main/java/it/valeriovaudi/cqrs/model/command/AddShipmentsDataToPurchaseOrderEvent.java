package it.valeriovaudi.cqrs.model.command;

import it.valeriovaudi.cqrs.model.AbstractEventModel;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Created by vvaudi on 22/04/17.
 */

@Data
@Table
@ToString
public class AddShipmentsDataToPurchaseOrderEvent extends AbstractEventModel {

    private String orderNumber;

    private String street;
    private String streetNumber;
}
