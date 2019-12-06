package com.stylefeng.guns.rest.vo.promo;

public enum PromoStockLogStatusEnum {

    STOCK_LOG_INIT(1, "初始化"),
    STOCK_LOG_SUCCESS(2, "成功"),
    STOCK_LOG_FAILED(3, "失败");

    private Integer value;

    private String status;

    private PromoStockLogStatusEnum(Integer value, String status) {
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
