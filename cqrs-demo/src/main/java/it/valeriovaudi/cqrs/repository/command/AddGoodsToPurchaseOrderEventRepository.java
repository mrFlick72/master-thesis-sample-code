package it.valeriovaudi.cqrs.repository.command;

import it.valeriovaudi.cqrs.model.command.AddGoodsToPurchaseOrderEvent;
import org.springframework.data.cassandra.repository.CassandraRepository;

/**
 * Created by vvaudi on 22/04/17.
 */
public interface AddGoodsToPurchaseOrderEventRepository
        extends CassandraRepository<AddGoodsToPurchaseOrderEvent> {
}
