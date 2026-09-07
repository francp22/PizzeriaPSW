package org.example.backend.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private double price;

    private int quantity;

    private boolean available;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems;
}
