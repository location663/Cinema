package com.stylefeng.guns.rest.vo.promo;

import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
public class PromoOrder implements Serializable {
    @Min(value = 1)
    private Integer promoId;
    @Min(value = 1)
    private Integer amount;
    private String promoToken;
    private String token;
}
