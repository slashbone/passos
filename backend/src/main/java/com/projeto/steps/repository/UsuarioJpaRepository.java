package com.projeto.steps.repository;

import com.projeto.steps.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 🗄️ Repositório JPA para Usuario
 * 
 * Interface que estende JpaRepository para operações CRUD automáticas
 * no SQL Server rodando no Podman
 * 
 * 🎯 Funcionalidades Automáticas:
 * - save() - Salvar/Atualizar usuário
 * - findById() - Buscar por ID
 * - findAll() - Listar todos
 * - deleteById() - Excluir por ID
 * - count() - Contar registros
 * 
 * 🔍 Consultas Personalizadas:
 * - Buscar por nome (parcial, case-insensitive)
 * - Buscar por data de nascimento
 * - Buscar usuários criados hoje
 * 
 * @author Projeto Steps V1.4
 * @version 1.4.0
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // ========================================
    // CONSULTAS DERIVADAS (Spring Data JPA)
    // ========================================
    
    /**
     * Busca usuários por nome contendo o texto (case-insensitive)
     * 
     * @param nome Parte do nome a buscar
     * @return Lista de usuários encontrados
     * 
     * Exemplo: findByNomeContainingIgnoreCase("silva")
     * Encontra: "João Silva", "Maria da Silva Santos"
     */
    List<Usuario> findByNomeContainingIgnoreCase(String nome);
    
    /**
     * Busca usuário por nome exato (case-insensitive)
     * 
     * @param nome Nome completo a buscar
     * @return Optional com usuário se encontrado
     */
    Optional<Usuario> findByNomeIgnoreCase(String nome);
    
    /**
     * Busca usuários por data de nascimento
     * 
     * @param dataNascimento Data a buscar
     * @return Lista de usuários nascidos na data
     */
    List<Usuario> findByDataNascimento(LocalDate dataNascimento);
    
    /**
     * Conta usuários por nome contendo texto
     * 
     * @param nome Parte do nome
     * @return Quantidade de usuários
     */
    long countByNomeContainingIgnoreCase(String nome);

    // ========================================
    // CONSULTAS PERSONALIZADAS (JPQL)
    // ========================================
    
    /**
     * Busca usuários criados hoje
     * 
     * @return Lista de usuários cadastrados hoje
     */
    @Query("SELECT u FROM Usuario u WHERE DATE(u.createdAt) = CURRENT_DATE ORDER BY u.createdAt DESC")
    List<Usuario> findUsuariosCriadosHoje();
    
    /**
     * Busca usuários por faixa de idade (baseado na data de nascimento)
     * 
     * @param idadeMin Idade mínima
     * @param idadeMax Idade máxima
     * @return Lista de usuários na faixa etária
     */
    @Query("SELECT u FROM Usuario u WHERE " +
           "DATEDIFF(year, u.dataNascimento, CURRENT_DATE) BETWEEN :idadeMin AND :idadeMax " +
           "ORDER BY u.dataNascimento DESC")
    List<Usuario> findByFaixaIdade(@Param("idadeMin") int idadeMin, @Param("idadeMax") int idadeMax);
    
    /**
     * Estatísticas gerais do sistema
     * 
     * @return Array com [totalUsuarios, usuariosHoje, idadeMedia]
     */
    @Query("SELECT " +
           "COUNT(u), " +
           "COUNT(CASE WHEN DATE(u.createdAt) = CURRENT_DATE THEN 1 END), " +
           "AVG(DATEDIFF(year, u.dataNascimento, CURRENT_DATE)) " +
           "FROM Usuario u")
    Object[] getEstatisticasGerais();

    // ========================================
    // CONSULTAS NATIVAS SQL (Para casos específicos)
    // ========================================
    
    /**
     * Busca usuários com aniversário hoje
     * 
     * @return Lista de usuários que fazem aniversário hoje
     */
    @Query(value = "SELECT * FROM usuarios WHERE " +
                   "MONTH(data_nascimento) = MONTH(GETDATE()) AND " +
                   "DAY(data_nascimento) = DAY(GETDATE())", 
           nativeQuery = true)
    List<Usuario> findAniversariantesHoje();
    
    /**
     * Lista os 10 usuários mais recentes
     * 
     * @return Lista dos 10 últimos usuários cadastrados
     */
    @Query(value = "SELECT TOP 10 * FROM usuarios ORDER BY created_at DESC", 
           nativeQuery = true)
    List<Usuario> findTop10MaisRecentes();
}