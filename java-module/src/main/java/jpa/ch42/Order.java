package jpa.ch42;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Order {

    @Id
    private Long id;

    private Date orderDate;

    private OrderStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}


