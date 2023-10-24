package com.example.restaurantedanice;

import com.example.restaurantedanice.controller.ClienteController;
import com.example.restaurantedanice.domain.cliente.Cliente;
import com.example.restaurantedanice.domain.cliente.dtos.CadastroClienteDTO;
import com.example.restaurantedanice.domain.cliente.dtos.DetalhamentoClienteDTO;
import com.example.restaurantedanice.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Inserindo dados do cliente corretamente")
    public void aoCadastrarComDadosCorretosDeveRetornarDetalhamentoClienteDTO() throws Exception {

        CadastroClienteDTO cadastroClienteDTO = new CadastroClienteDTO("Kauã", "kaua@test.com", "71999884455", "");

        Cliente cliente = new Cliente(cadastroClienteDTO);

        when(clienteService.cadastrarCliente(any(CadastroClienteDTO.class)))
                .thenReturn(new DetalhamentoClienteDTO(cliente));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/clientes")
                        .content(asJsonString(cadastroClienteDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        Mockito.verify(clienteService, Mockito.times(1)).cadastrarCliente(any(CadastroClienteDTO.class));

        String locationHeader = result.getResponse().getHeader("Location");

        assertNotNull(locationHeader, "O Header não pode ser vazio");
        assert(locationHeader).contains("/clientes/");
    }

    private String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @DisplayName("Inserindo dados do cliente incorretamente")
    void aoCadastrarComDadosFaltantesDeveRetornarStatusBadRequest() throws Exception {
        // Dados com campos em branco
        CadastroClienteDTO dados = new CadastroClienteDTO(
                "",    // Nome em branco
                "",    // Email em branco
                "1234567890",
                "123.456.789-10"
        );

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dados))
                )
                .andExpect(status().isBadRequest()); // Verifica se o status é 400 Bad Request
    }

}
