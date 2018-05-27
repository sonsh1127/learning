package jpa.ch42;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Item {

    @Id
    private Long id;

    private String name;

    private int stockQuantity;

}
