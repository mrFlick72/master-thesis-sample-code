package it.valeriovaudi.cqrs.repository.query;

import it.valeriovaudi.cqrs.model.query.QueryPurchaseOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by vvaudi on 22/04/17.
 */
public interface QueryPurchaseOrderRespository extends MongoRepository<QueryPurchaseOrder, String> {
    QueryPurchaseOrder findByOrderNumber(String orderNumber);
}
