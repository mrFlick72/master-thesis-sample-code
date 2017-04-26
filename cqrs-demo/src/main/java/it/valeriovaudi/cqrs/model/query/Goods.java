package it.valeriovaudi.cqrs.model.query;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by vvaudi on 22/04/17.
 */

@Data
@Builder
public class Goods {

    private String goodsName;
    private String goodsBarCode;
    private BigDecimal goodsPrice;
    private int goodsQuantity;
}
