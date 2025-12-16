package com.projeto.steps.repository;

import com.projeto.steps.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * INTERFACE USUARIO REPOSITORY
 * 
 * EXPLICAÇÃO PARA INICIANTES:
 * Esta interface é responsável por todas as operações no banco de dados MongoDB.
 * Ela funciona como uma "ponte" entre nossa aplicação Java e o banco de dados.
 * 
 * O QUE É UM REPOSITORY?
 * Repository é um padrão de projeto que separa a lógica de acesso aos dados
 * da lógica de negócio da aplicação. Pense nele como um "gerenciador de dados"
 * que sabe como salvar, buscar, atualizar e deletar informações no banco.
 * 
 * SPRING DATA MONGODB - MÁGICA DO FRAMEWORK:
 * Ao estender MongoRepository, o Spring automaticamente cria toda a implementação
 * necessária para acessar o MongoDB. Você só precisa declarar os métodos que quer,
 * e o Spring implementa tudo automaticamente!
 * 
 * MÉTODOS AUTOMÁTICOS QUE JÁ VÊM PRONTOS:
 * - save(usuario)           : Salva um novo usuário ou atualiza existente
 * - findById(id)            : Busca um usuário pelo ID
 * - findAll()               : Busca todos os usuários
 * - deleteById(id)          : Deleta um usuário pelo ID
 * - count()                 : Conta quantos usuários existem
 * - existsById(id)          : Verifica se um usuário existe
 * 
 * MÉTODOS CUSTOMIZADOS:
 * Podemos declarar métodos com nomes específicos e o Spring cria a query automaticamente!
 * Exemplo: findByNome(String nome) - Spring entende que deve buscar por nome.
 */
@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    
    /**
     * BUSCA USUÁRIOS POR NOME
     * 
     * COMO FUNCIONA:
     * O Spring lê o nome do método "findByNome" e automaticamente cria uma query
     * que busca usuários onde o campo "nome" é igual ao parâmetro fornecido.
     * 
     * QUERY EQUIVALENTE NO MONGODB:
     * db.dados_usuario.find({ "nome": "João Silva" })
     * 
     * @param nome Nome do usuário a ser buscado
     * @return Lista de usuários com o nome especificado
     */
    List<Usuario> findByNome(String nome);
    
    /**
     * BUSCA USUÁRIOS POR NOME CONTENDO TEXTO
     * 
     * COMO FUNCIONA:
     * O "Containing" faz uma busca parcial. Por exemplo, se buscar "Silva",
     * encontrará "João Silva", "Maria Silva Santos", etc.
     * 
     * QUERY EQUIVALENTE NO MONGODB:
     * db.dados_usuario.find({ "nome": /Silva/i })
     * 
     * @param nome Parte do nome a ser buscado
     * @return Lista de usuários cujo nome contém o texto especificado
     */
    List<Usuario> findByNomeContaining(String nome);
    
    /**
     * BUSCA USUÁRIOS POR NOME IGNORANDO MAIÚSCULAS/MINÚSCULAS
     * 
     * COMO FUNCIONA:
     * O "IgnoreCase" faz a busca sem diferenciar maiúsculas de minúsculas.
     * "joão silva", "JOÃO SILVA", "João Silva" - todos retornariam o mesmo resultado.
     * 
     * @param nome Nome do usuário (case-insensitive)
     * @return Lista de usuários com o nome especificado (ignorando case)
     */
    List<Usuario> findByNomeIgnoreCase(String nome);
    
    /**
     * BUSCA USUÁRIOS QUE CONTENHAM O NOME ESPECIFICADO (BUSCA PARCIAL)
     * 
     * COMO FUNCIONA:
     * Busca usuários cujos nomes contenham a string especificada, ignorando
     * maiúsculas/minúsculas. Por exemplo, "joão" encontraria "João Silva", 
     * "Maria João", "João Pedro", etc.
     * 
     * @param nome Parte do nome a ser buscada (case-insensitive)
     * @return Lista de usuários cujos nomes contêm a string
     */
    List<Usuario> findByNomeContainingIgnoreCase(String nome);
    
    /**
     * VERIFICA SE EXISTE USUÁRIO COM DETERMINADO NOME
     * 
     * COMO FUNCIONA:
     * Retorna true se existir pelo menos um usuário com o nome, false caso contrário.
     * Mais eficiente que buscar e verificar se a lista está vazia.
     * 
     * @param nome Nome a ser verificado
     * @return true se existe, false caso contrário
     */
    boolean existsByNome(String nome);
    
    /**
     * CONTA QUANTOS USUÁRIOS TÊM UM DETERMINADO NOME
     * 
     * COMO FUNCIONA:
     * Retorna o número de usuários que têm exatamente o nome especificado.
     * 
     * @param nome Nome a ser contado
     * @return Quantidade de usuários com esse nome
     */
    long countByNome(String nome);
    
    // ========================================
    // EXEMPLOS DE OUTROS MÉTODOS POSSÍVEIS
    // ========================================
    
    /*
     * O Spring Data MongoDB suporta muitos outros padrões de nomenclatura:
     * 
     * BUSCA POR DATA:
     * - findByDataNascimentoBefore(LocalDate data)    : Nascidos antes da data
     * - findByDataNascimentoAfter(LocalDate data)     : Nascidos depois da data
     * - findByDataNascimentoBetween(LocalDate inicio, LocalDate fim) : Entre datas
     * 
     * ORDENAÇÃO:
     * - findAllByOrderByNomeAsc()                     : Todos ordenados por nome A-Z
     * - findByNomeOrderByDataNascimentoDesc(String nome) : Por nome, ordenado por data Z-A
     * 
     * COMBINAÇÕES:
     * - findByNomeAndDataNascimento(String nome, LocalDate data) : Nome E data
     * - findByNomeOrDataNascimento(String nome, LocalDate data)  : Nome OU data
     * 
     * LIMITAÇÃO:
     * - findTop10ByOrderByNomeAsc()                   : Primeiros 10 por nome
     * - findFirst5ByNomeContaining(String nome)       : Primeiros 5 que contêm nome
     */
}
