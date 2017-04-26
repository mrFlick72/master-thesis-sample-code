package it.valeriovaudi.cqrs.model.query;

import lombok.Builder;
import lombok.Data;

/**
 * Created by vvaudi on 22/04/17.
 */

@Data
@Builder
public class Customer {

    private String firstName;
    private String lastName;
}
