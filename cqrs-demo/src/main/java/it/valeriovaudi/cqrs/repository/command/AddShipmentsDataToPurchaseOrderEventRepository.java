package it.valeriovaudi.cqrs.repository.command;

import it.valeriovaudi.cqrs.model.command.AddShipmentsDataToPurchaseOrderEvent;
import org.springframework.data.cassandra.repository.CassandraRepository;

/**
 * Created by vvaudi on 22/04/17.
 */
public interface AddShipmentsDataToPurchaseOrderEventRepository
        extends CassandraRepository<AddShipmentsDataToPurchaseOrderEvent> {
}
