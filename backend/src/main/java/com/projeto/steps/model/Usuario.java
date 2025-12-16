package com.projeto.steps.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

/**
 * CLASSE USUARIO - MODELO DE DADOS
 * 
 * EXPLICAÇÃO PARA INICIANTES:
 * Esta classe representa um usuário no nosso sistema. No MongoDB, cada usuário
 * será armazenado como um "documento" (equivalente a uma linha em banco SQL).
 * 
 * ANOTAÇÕES (@):
 * - @Document: Indica que esta classe será armazenada no MongoDB como um documento
 * - collection: Nome da coleção (equivalente a "tabela" em SQL) onde os dados serão salvos
 * - @Id: Marca o campo como identificador único do documento (como chave primária em SQL)
 * 
 * ESTRUTURA NO MONGODB:
 * Cada usuário será salvo assim:
 * {
 *   "_id": "65a1b2c3d4e5f6789012345",
 *   "nome": "João Silva",
 *   "dataNascimento": "1990-05-15"
 * }
 */
@Document(collection = "dados_usuario")
public class Usuario {
    
    /**
     * ID DO USUÁRIO
     * O MongoDB gera automaticamente um ID único para cada documento.
     * Este ID é uma string longa como "65a1b2c3d4e5f6789012345".
     * Não precisamos definir este valor - o MongoDB faz isso automaticamente!
     */
    @Id
    private String id;
    
    /**
     * NOME DO USUÁRIO
     * Armazena o nome completo do usuário.
     * Exemplo: "Maria Silva", "João Santos", etc.
     */
    private String nome;
    
    /**
     * DATA DE NASCIMENTO
     * Armazena a data de nascimento do usuário.
     * Usamos LocalDate que é uma classe Java para representar datas (ano-mês-dia).
     * Exemplo: 1990-05-15 (15 de maio de 1990)
     */
    private LocalDate dataNascimento;
    
    // ========================================
    // CONSTRUTORES
    // ========================================
    
    /**
     * CONSTRUTOR VAZIO
     * Necessário para o Spring Data MongoDB criar objetos vazios
     * e preencher com dados do banco de dados.
     */
    public Usuario() {
    }
    
    /**
     * CONSTRUTOR COM PARÂMETROS
     * Facilita criar um novo usuário com todos os dados de uma vez.
     * 
     * @param nome Nome do usuário
     * @param dataNascimento Data de nascimento do usuário
     */
    public Usuario(String nome, LocalDate dataNascimento) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }
    
    // ========================================
    // GETTERS E SETTERS
    // ========================================
    
    /**
     * EXPLICAÇÃO DOS GETTERS E SETTERS:
     * - Getters (get...): Métodos para OBTER (ler) o valor de um campo
     * - Setters (set...): Métodos para DEFINIR (modificar) o valor de um campo
     * 
     * São necessários porque os campos são privados (private) e não podem
     * ser acessados diretamente de fora da classe.
     */
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
    
    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    
    // ========================================
    // MÉTODOS AUXILIARES
    // ========================================
    
    /**
     * MÉTODO toString()
     * Converte o objeto Usuario em uma string legível.
     * Útil para debug e logs.
     * 
     * Exemplo de saída:
     * Usuario{id='65a1b2c3...', nome='João Silva', dataNascimento=1990-05-15}
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", dataNascimento=" + dataNascimento +
                '}';
    }
}
