package vip.testops.coffee.order.entities.vto;

public enum OrderStatus {
    UNPAID("未付款"), PAID("已付款"), DELIVERED("已发货"), SIGNED("已签收");

    private String value;

    private OrderStatus(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}

