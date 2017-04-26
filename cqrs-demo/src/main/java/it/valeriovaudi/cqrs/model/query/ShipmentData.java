package it.valeriovaudi.cqrs.model.query;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Created by vvaudi on 22/04/17.
 */

@Data
@ToString
@Builder
public class ShipmentData {
    private String street;
    private String streetNumber;
}
