package org.example.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders") //evitare incompatibilità con comando order
@Getter
@Setter
@NoArgsConstructor
public class Order {

    public enum OrderType {//Tipi di ordine: ritiro o consegna
        TAKEAWAY,
        DELIVERY
    }

    public enum OrderStatus {//Stati possibili dell'ordine
        PREPARING,
        READY,
        OUT_FOR_DELIVERY,
        DELIVERED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime creationDate;
    private double total;
    private String street;
    private String city;
    private String phoneNumber;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items;

    @Enumerated(EnumType.STRING)
    private OrderType type;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
