package br.com.ifsp.projetolinguagens.controller;

import br.com.ifsp.projetolinguagens.exceptions.LivroExceptions;
import br.com.ifsp.projetolinguagens.exceptions.UsuarioExceptions;
import br.com.ifsp.projetolinguagens.interfaces.GerenciamentoDeFuncionarios;
import br.com.ifsp.projetolinguagens.interfaces.GerenciamentoDeUsuarios;
import br.com.ifsp.projetolinguagens.model.Cliente;
import br.com.ifsp.projetolinguagens.model.Funcionario;
import br.com.ifsp.projetolinguagens.model.Livro;
import br.com.ifsp.projetolinguagens.interfaces.GerenciamentoDeLivros;
import br.com.ifsp.projetolinguagens.model.Usuario;
import br.com.ifsp.projetolinguagens.services.LivroService;
import br.com.ifsp.projetolinguagens.services.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Define a classe como sendo uma classe controller
@RestController

// Cria uma rota http personalizada para o administrador
@RequestMapping("/admin")
public class AdministradorController implements GerenciamentoDeLivros, GerenciamentoDeUsuarios, GerenciamentoDeFuncionarios {

    /*
     Essa classe pode realizar o CRUD nos livros e nos Clientes/Usuarios, e também consegue manipular os funcionarios
    * */

    // Faz a injeção das dependências do serviço 'Livro Service' para que seja possível usar os métodos CRUD
    @Autowired
    private LivroService livroService;

    // Faz a injeção das dependências do serviço 'Usuario Service' para que seja possível usar os métodos CRUD
    @Autowired
    private UsuariosService usuariosService;

    /*################################# Método POST #####################################*/
    @PostMapping("/livros")
    /*
         Para acessar a rota basta adicionar /livros na rota padrão /admin
          e definir o método como POST
    */


    public ResponseEntity<?> adicionarLivro(@RequestBody Livro livro) {
        try {
            Livro livroAdicionado = livroService.adicionarLivro(livro);
            return new ResponseEntity<>(livroAdicionado, HttpStatus.CREATED);
        }catch(LivroExceptions l){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(l.getMessage());
        }
    }
    /*
        O método recebe pelo body um novo livro, e utilizando a instancia
        do servico de livros, grava esse objeto na lista de livros e retorna, através do
        httpStatus, o status de criação de objetos, que é o 201

     */

    /*############################### Método GET - Lista todos os livros ##################################*/

    @GetMapping("/livros")
    /*
         Para acessar a rota basta adicionar /livros na rota padrão /admin
          e definir o método como GET
    */
    public ResponseEntity<?> listarLivros() {
        try {
            List<Livro> livros = livroService.listarLivros();
            return new ResponseEntity<>(livros, HttpStatus.OK);
        }catch (LivroExceptions l){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(l.getMessage());
        }
    }

    /*
        chama o método ListarLivros e retorna para o usuario todos os livros da lista,
        juntamente com o httpStatus de OK, que é o 200

     */

    /*################################# Método GET - Lista só um livro #####################################*/
    @GetMapping("/livros/{id}")
     /*
         Para acessar a rota basta adicionar /livros/id do livro que deseja listar na rota padrão /admin
          e definir o método como GET
    */
    public ResponseEntity<?> buscarLivro(@PathVariable Integer id) {
        try {
            Livro livro = livroService.buscarLivro(id);
            return new ResponseEntity<>(livro, HttpStatus.OK);
        }catch (LivroExceptions l){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(l.getMessage());
        }
    }
    /*
           Recebe um id pela url, chama o método buscarLivro e passa esse id por parametro e armazena
           o resultado em livro. Se o livro for nulo, ou seja, nao exitir um livro com esse id,
           entao é retornado o httpStatus NOT_FOUND, caso o livro seja encontrado, retorna um httpStatus 200
           de OK, e o livro que foi solicitado.

        */

    /*################################# Método DELETE - Apaga um livro #####################################*/

    @DeleteMapping("/livros/{id}")
    /*
         Para acessar a rota basta adicionar /livros/id do livro que seja excluir
          na rota padrão /admin e definir o método como DELETE
    */
    public ResponseEntity<?> excluirLivro(@PathVariable Integer id) {
        try {
            Livro livro = livroService.buscarLivro(id);
            livroService.excluirLivro(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (LivroExceptions l){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(l.getMessage());
        }
    }
     /*
           Recebe um id pela url, chama o método buscarLivro e passa esse id por parametro e armazena
           o resultado em livro. Se o livro for nulo, ou seja, se nao existir um livro com esse id,
           entao é retornado o httpStatus NOT_FOUND, caso o livro seja encontrado, chama o metodo ExcluirLivro,
           passando o id por parâmetro e retorna um json vazio e o httpStatus e NO_CONTENT

        */

    /*################################# Método PUT - Altera um livro #####################################*/

    @PutMapping("/livros/{id}")
    /*
         Para acessar a rota basta adicionar /livros/id do livro que seja alterar
          na rota padrão /admin e definir o método como DELETE
    */
    public ResponseEntity<?> alterarLivro(@PathVariable Integer id, @RequestBody Livro livro) {
        try {
            Livro livroAtualizado = livroService.alterarLivro(id, livro);
            return new ResponseEntity<>(livroAtualizado, HttpStatus.OK);
        }catch (LivroExceptions l){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(l.getMessage());
        }
    }
    /*
           Recebe um id pela url e um objeto livro pelo body (em formato JSON), chama o método
           AlterarLivro e passa esse id e as alterações por parametro, armazenando o resultado em livroAtualizado.
           Se o livroAtualizado for nulo, ou seja, se nao existir um livro com esse id, entao é retornado o httpStatus
           NOT_FOUND, caso o livro seja encontrado, retorna um json com o livro atualizado e o httpStatus de OK

        */

    /*########################   ROTAS PARA MANIPULAR USUARIOS/CLIENTES  ########################333*/

    //METODO POST


    @PostMapping("/clientes")
    /*
         Para acessar a rota basta adicionar /clientes
          na rota padrão /admin e definir o método como POST para adicionar um cliente
    */
    public ResponseEntity<?> adicionarCliente(@RequestBody Cliente cliente) {
        try {
            Cliente novoCliente = usuariosService.adicionarCliente(cliente);
            return new ResponseEntity<>(novoCliente, HttpStatus.CREATED);
        }catch (UsuarioExceptions u){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(u.getMessage());
        }
    }

    /*
    *   O método recebe pelo corpo da requisição um JSON com um objeto cliente,
    *   cria-se uma nova instancia de cliente que irá receber a resposta do método
    *   adicionarCliente, que recebe por parametro o cliente enviado por JSON. A resposta
    *   do método é o próprio objeto que foi criado, e através do ResponseEntity é enviado
    *   como resposta à requisição o novo cliente e o status http 201.
    * */


    // METODO GET
    @GetMapping("/usuarios")
    /*
         Para acessar a rota basta adicionar /usuarios
          na rota padrão /admin e definir o método como GET para listar os clientes
    */
    public ResponseEntity<?> listarUsuario() {
        try {
            List<Usuario> usuarios = usuariosService.listarUsuarios();
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }catch (UsuarioExceptions u){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(u.getMessage());
        }
    }
    /*
    *   Cria uma lista de usuarios a qual será atribuída a lista de usuarios armazenados, que será
    *   retornada pelo método listarUsuarios(). Por fim, é retornada a lista de usuarios e o status http 200.
    * */

    @GetMapping("/clientes")
    /*
         Para acessar a rota basta adicionar /clientes
          na rota padrão /admin e definir o método como GET para listar os clientes
    */
    public ResponseEntity<?> listarCliente() {
        try {
            List<Cliente> clientes = usuariosService.listarClientes();
            return new ResponseEntity<>(clientes, HttpStatus.OK);
        }catch (UsuarioExceptions u){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(u.getMessage());
        }
    }
    /*
    *   /*
        chama o método ListarClientes e retorna para o usuario todos os clientes da lista,
        juntamente com o httpStatus de OK, que é o 200

     */

    // METODO GET PELO CPF
    @GetMapping("/usuarios/{cpf}")
     /*
         Para acessar a rota basta adicionar /usuarios/cpf do usuario que quer listar
          na rota padrão /admin e definir o método como GET para listar o usuario com aquele
          cpf
    */
    public ResponseEntity<?> buscarUsuario(@PathVariable String cpf) {
        try {
            Usuario usuario = usuariosService.buscarUsuario(cpf);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        }catch (UsuarioExceptions u){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(u.getMessage());
        }
    }
    /*
           Recebe um cpf pela url da requisição, chama o método buscarUsuario e passa esse id por parametro e
           armazena o resultado em usuario. Se o usuario for nulo, ou seja, nao exitir um usuario com esse id,
           entao é retornado o httpStatus NOT_FOUND, caso o usuario seja encontrado, retorna um httpStatus 200
           de OK, e o usuario que foi solicitado.
     */

    @GetMapping("/clientes/{cpf}")
     /*
         Para acessar a rota basta adicionar /clientes/cpf do usuario que quer listar
          na rota padrão /admin e definir o método como GET para listar o cliente com aquele
          cpf
    */
    public ResponseEntity<?> buscarCliente(String cpf) {
        try {
            Cliente cliente = usuariosService.buscarCliente(cpf);
            return new ResponseEntity<>(cliente, HttpStatus.OK);
        }catch (UsuarioExceptions u){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(u.getMessage());
        }
    }


    // METODO DELETE - ESSE METODO É VALIDO PARA TODOS QUE EXTENDEM USUARIO
    @DeleteMapping("/usuarios/{cpf}")
    public ResponseEntity<?> excluirUsuario(@PathVariable String cpf) {
        try {
            Usuario usuario = usuariosService.buscarUsuario(cpf);
            usuariosService.excluirUsuario(cpf);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (UsuarioExceptions u){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(u.getMessage());
        }
    }

     /*
           Recebe um CPF pela url da requisição, chama o método buscarUsuario e passa esse id por parametro e
           armazena o resultado em usuario. Se o usuario for nulo, ou seja, nao exitir um usuario com esse id,
           entao é retornado o httpStatus NOT_FOUND, caso o usuario seja encontrado, exclui o usuario das listas
           onde ele está armazenado e retorna um httpStatus 204 de NO_CONTENT.
     */

    // METODO PUT - Essa rota utiliza o método alterarUsuario da classe de serviços de usuario, que permite que
    // possa ser utilizada também para alterar funcionarios e clientes
    @PutMapping("/usuarios/{cpf}")
    public ResponseEntity<?> alterarUsuario(@PathVariable String cpf, @RequestBody Usuario usuario) {
        try {
            Usuario usuarioAtualizado = usuariosService.atualizarUsuario(cpf, usuario);
            return new ResponseEntity<>(usuarioAtualizado, HttpStatus.OK);
        }catch (UsuarioExceptions u){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(u.getMessage());
        }
    }

    /*
           Recebe um cpf pela url e um objeto usuario pelo body (em formato JSON), chama o método
           AtualizarUsuario e passa esse cpf e as alterações por parametro, armazenando o resultado em
           usuarioAtualizado. Se o usuarioAtualizado for nulo, ou seja, se nao existir um usuario com esse cpf,
           entao é retornado o httpStatus NOT_FOUND, caso o usuario seja encontrado, retorna um json com o usuario
           atualizado e o httpStatus de OK
    */


    /*########################### ROTAS PARA MANIPULAR FUNCIONARIOS ########################3*/

    // MÉTODO POST
    @PostMapping("/funcionarios")
    /*
         Para acessar a rota basta adicionar /funcionarios na rota padrão /admin
          e definir o método como POST
    */
    public ResponseEntity<?> adicionarFuncionario(Funcionario funcionario) {
        try {
            Funcionario novoFuncionario = usuariosService.adicionarFuncionario(funcionario);
            return new ResponseEntity<>(novoFuncionario, HttpStatus.CREATED);
        }catch (UsuarioExceptions u){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(u.getMessage());
        }
    }
    /*
        O método recebe pelo body um novo funcionario, e utilizando a instancia
        do servico de funcionarios, grava esse objeto na lista de funcionarios e retorna, através do
        httpStatus, o status de criação de objetos, que é o 201.
     */

    // MÉTODO GET
    @GetMapping("/funcionarios")
    /*
         Para acessar a rota basta adicionar /funcionarios na rota padrão /admin
          e definir o método como GET
    */
    public ResponseEntity<?> listarFuncionario() {
        try {
            List<Funcionario> funcionarios = usuariosService.listarFuncionarios();
            return new ResponseEntity<>(funcionarios, HttpStatus.OK);
        }catch (UsuarioExceptions u){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(u.getMessage());
        }
    }
    /*
        chama o método listarFuncionarios e retorna para o usuario todos os funcionarios da lista,
        juntamente com o httpStatus de OK, que é o 200

     */

    // MÉTODO GET
    @GetMapping("/funcionarios/{cpf}")
    /*
         Para acessar a rota basta adicionar /funcionarios/cpf do funcionario que seja buscar
          na rota padrão /admin e definir o método como GET
    */
    public ResponseEntity<?> buscarFuncionario(String cpf) {
        try {
            Funcionario funcionario = usuariosService.buscarFuncionario(cpf);
            return new ResponseEntity<>(funcionario, HttpStatus.OK);
        }catch (UsuarioExceptions u){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(u.getMessage());
        }
    }

    /*
        chama o método buscarFuncionario, passando o cpf recebido pelo parâmetro e retorna o usuário com
        o cpf correspondente, juntamente com o httpStatus de OK, que é o 200.
     */

}