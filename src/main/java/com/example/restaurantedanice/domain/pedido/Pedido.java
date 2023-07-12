package com.example.restaurantedanice.domain.pedido;

import com.example.restaurantedanice.domain.cliente.Cliente;
import com.example.restaurantedanice.domain.comida.Comida;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "pedido")
@Table(name = "pedidos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Cliente cliente;
    @ManyToMany
    private List<Comida> comidas;
    private LocalDateTime data_hora;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Pedido(Cliente cliente, List<Comida> comidasList) {

        this.cliente = cliente;
        this.comidas = comidasList;
        this.data_hora = LocalDateTime.now();
        this.status = Status.PREPARANDO;

    }

}
