package it.valeriovaudi.cqrs.model;

import lombok.Data;
import org.springframework.data.cassandra.mapping.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by vvaudi on 22/04/17.
 */

@Data
public abstract class AbstractEventModel implements Serializable {

    @PrimaryKey
    protected UUID id;
    protected Date creationDate = new Date();
}
