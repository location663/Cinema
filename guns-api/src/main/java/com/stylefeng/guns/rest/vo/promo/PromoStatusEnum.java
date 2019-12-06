package com.stylefeng.guns.rest.vo.promo;

public enum PromoStatusEnum {

    PROMO_NOT_BEGIN(0,"未开始"),
    PROMO_RUNNING(1, "进行中"),
    PROMO_OVER(2, "已结束");

    private Integer value;
    private String status;

    PromoStatusEnum(Integer value, String status) {
        this.value = value;
        this.status = status;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
