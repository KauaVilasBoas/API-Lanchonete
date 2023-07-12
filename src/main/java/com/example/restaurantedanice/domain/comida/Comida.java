package com.example.restaurantedanice.domain.comida;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "comidas")
@Entity(name = "Comida")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Comida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String imagem;
    private Double preco;

    public Comida(DadosCadastroComida dados) {
        this.titulo = dados.titulo();
        this.imagem = dados.imagem();
        this.preco = dados.preco();
    }

    public void atualizarDados(DadosAtualizacaoComida dados) {

        if (dados.titulo() != null) {
            this.titulo = dados.titulo();
        }
        if (dados.imagem() != null) {
            this.imagem = dados.imagem();
        }
        if (dados.preco() != null) {
            this.preco = dados.preco();
        }


    }
}
