package it.valeriovaudi.cqrs.model.query;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by vvaudi on 22/04/17.
 */

@Data
@Document
@ToString
public class QueryPurchaseOrder{

    @Id
    private String id;

    @Version
    private Long version;

    protected Date creationDate = new Date();
    private String orderNumber;
    private Customer customer;
    private ShipmentData shipmentData;
    private List<Goods> goodsList;
    private BigDecimal total;
}
