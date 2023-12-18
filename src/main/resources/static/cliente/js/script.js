document.getElementById('formulario').addEventListener('submit', function (event) {
    event.preventDefault(); // Impede o envio padrão do formulário

    const nome = document.getElementById('nome').value;
    const email = document.getElementById('email').value;
    const telefone = document.getElementById('telefone').value;
    let cpf = document.getElementById('cpf').value;

    // Remove caracteres não numéricos do CPF
    cpf = cpf.replace(/\D/g, '');

    // Aplica a máscara ao CPF
    $('#cpf').inputmask('999.999.999-99');

    const cliente = {
        nome: nome,
        email: email,
        telefone: telefone,
        cpf: cpf
    };

    fetch('http://localhost:8080/clientes', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(cliente)
    })
        .then(response => response.json())
        .then(data => {
            document.getElementById('resultado').innerText = 'Cliente cadastrado com sucesso!';
            document.getElementById('formulario').reset(); // Limpa o formulário após o cadastro
            listarClientes();
        })
        .catch(error => {
            document.getElementById('resultado').innerText = 'Erro ao cadastrar cliente.';
        });
});

function listarClientes() {
    fetch('http://localhost:8080/clientes')
        .then(response => response.json())
        .then(data => {
            var listaClientes = document.getElementById('listaClientes');
            listaClientes.innerHTML = ''; // Limpa a lista antes de adicionar os clientes

            let table = document.createElement('table');
            table.className = 'table';

            let thead = document.createElement('thead');
            thead.innerHTML = `
                <tr>
                    <th scope="col">Nome</th>
                    <th scope="col">Email</th>
                    <th scope="col">Telefone</th>
                    <th scope="col">Ações</th>
                </tr>
            `;
            table.appendChild(thead);

            let tbody = document.createElement('tbody');

            data.content.forEach(cliente => {
                let tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${cliente.nome}</td>
                    <td>${cliente.email}</td>
                    <td>${cliente.telefone}</td>
                    <td>
                     <button class="btn btn-primary mr-2" onclick="abrirModalEditarCliente(${cliente.id})">Editar</button>
                        <button class="btn btn-danger" onclick="excluirCliente(${cliente.id})">Excluir</button>
                        <button class="btn btn-primary mr-2" onclick="abrirModalDetalharCliente(${cliente.id})">Detalhar</button>
                    </td>
                `;
                tbody.appendChild(tr);
            });

            table.appendChild(tbody);

            listaClientes.appendChild(table);
            listaClientes.scrollIntoView({behavior: "smooth"}); // Faz a tela rolar até a lista de clientes
        })
        .catch(error => {
            document.getElementById('listaClientes').innerText = 'Erro ao listar clientes.';
        });

}

function abrirModalEditarCliente(clienteId) {
    fetch(`http://localhost:8080/clientes/${clienteId}`)
        .then(response => response.json())
        .then(cliente => {
            const modal = new bootstrap.Modal(document.getElementById('modalEditarCliente'));
            const form = document.getElementById('formEditarCliente');

            form.innerHTML = `
                <input type="hidden" id="clienteIdEditar" value="${cliente.id}">
                
                <div class="mb-3">
                    <label for="nomeEditar" class="form-label">Nome:</label>
                    <input type="text" class="form-control" id="nomeEditar" value="${cliente.nome}">
                </div>
                
                <div class="mb-3">
                    <label for="telefoneEditar" class="form-label">Telefone:</label>
                    <input type="tel" class="form-control" id="telefoneEditar" value="${cliente.telefone}" pattern="[0-9]{10,11}">
                </div>
            `;

            document.getElementById('btnSalvarEdicao').addEventListener('click', function () {
                const idEditar = document.getElementById('clienteIdEditar').value;
                const nomeEditar = document.getElementById('nomeEditar').value;
                const telefoneEditar = document.getElementById('telefoneEditar').value;

                const clienteEditar = {
                    id: idEditar,
                    nome: nomeEditar,
                    telefone: telefoneEditar
                };

                fetch(`http://localhost:8080/clientes`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(clienteEditar)
                })
                    .then(response => {
                        if (response.ok) {
                            modal.hide();
                            listarClientes();
                        } else {
                            console.error('Erro ao editar cliente.');
                        }
                    })
                    .catch(error => {
                        console.error('Erro ao editar cliente.', error);
                    });
            });

            modal.show();
        })
        .catch(error => {
            console.error('Erro ao obter dados do cliente para edição.', error);
        });
}


function excluirCliente(clienteId) {
    fetch(`http://localhost:8080/clientes/${clienteId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                listarClientes(); // Atualiza a lista após a exclusão
            } else {
                console.error('Erro ao excluir cliente.');
            }
        })
        .catch(error => {
            console.error('Erro ao excluir cliente.', error);
        });
}

function abrirModalDetalharCliente(clienteId) {
    fetch(`http://localhost:8080/clientes/${clienteId}`)
        .then(response => response.json())
        .then(cliente => {
            const modal = new bootstrap.Modal(document.getElementById('modalDetalharCliente'));
            const modalBody = document.getElementById('modalDetalharClienteBody');

            modalBody.innerHTML = `
                <p><strong>Nome:</strong> ${cliente.nome}</p>
                <p><strong>Email:</strong> ${cliente.email}</p>
                <p><strong>Telefone:</strong> ${cliente.telefone}</p>
                <p><strong>CPF:</strong> ${cliente.cpf}</p>
            `;

            modal.show();
        })
        .catch(error => {
            console.error('Erro ao obter dados do cliente para detalhar.', error);
        });
}

