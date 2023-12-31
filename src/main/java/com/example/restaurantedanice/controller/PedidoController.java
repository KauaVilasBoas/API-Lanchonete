package com.example.restaurantedanice.controller;

import com.example.restaurantedanice.domain.comida.Comida;
import com.example.restaurantedanice.domain.pedido.*;
import com.example.restaurantedanice.domain.pedido.dtos.AtualizacaoPedidoDTO;
import com.example.restaurantedanice.domain.pedido.dtos.CadastroPedidoDTO;
import com.example.restaurantedanice.domain.pedido.dtos.DetalhamentoPedidoDTO;
import com.example.restaurantedanice.domain.pedido.dtos.ListagemPedidoDTO;
import com.example.restaurantedanice.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @PostMapping
    public ResponseEntity<DetalhamentoPedidoDTO> novoPedido(@RequestBody @Valid CadastroPedidoDTO dados, UriComponentsBuilder uriBuilder){

        var dadosDetalhamentoPedido = service.novoPedido(dados);
        var uri = uriBuilder.path("pedido/{id}").buildAndExpand(dadosDetalhamentoPedido.id()).toUri();
        return ResponseEntity.created(uri).body(dadosDetalhamentoPedido);

    }

    @GetMapping
    public ResponseEntity<Page<ListagemPedidoDTO>> listarPedidos(@PageableDefault(size = 10, sort = "dataHora", direction = Sort.Direction.DESC) Pageable pageable){
        var page = service.listarPedidos(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @GetMapping("comidas/{id}")
    public ResponseEntity<List<Comida>> listarComidasPeloPedido(@PathVariable Long id){

        var lista = service.listarComidasPeloPedido(id);
        return ResponseEntity.ok(lista);

    }

    @GetMapping("/{status}")
    public ResponseEntity<List<Pedido>> listarPedidoPeloStatus(@PathVariable Status status){

        var pedidos = service.listarPedidosPeloStatus(status);
        return ResponseEntity.ok(pedidos);

    }

    @GetMapping("/detalhar/{id}")
    public ResponseEntity<DetalhamentoPedidoDTO> detalharPedido(@PathVariable Long id){

        var dadosDetalhamentoPedido = service.detalharPedido(id);
        return ResponseEntity.ok(dadosDetalhamentoPedido);

    }

    @PutMapping
    public ResponseEntity<DetalhamentoPedidoDTO> atualizarPedido(@RequestBody @Valid AtualizacaoPedidoDTO dados){
        service.atualizarPedido(dados);
        var detalhesPedido = service.detalharPedido(dados.id());
        return ResponseEntity.ok(detalhesPedido);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> concluirPedido(@PathVariable Long id){

        service.concluirPedido(id);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluirPedido(@PathVariable Long id){
        service.excluirPedido(id);
        return ResponseEntity.noContent().build();
    }

}
