

package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * A classe {@code Usuario} representa um usuário do sistema de venda de ingressos.
 * Cada usuário possui um login, senha, nome completo, CPF, e-mail, status de administrador,
 * e uma lista de ingressos e recibos associados.
 */
public class Usuario {
    private String username;
    private String password;
    private String fullName;
    private String cpf;
    private String email;
    private boolean isAdmin;
    private List<Ingresso> Ingressos;
    private List<Recibo> Recibos;

    /**
     * Construtor da classe {@code Usuario}.
     * 
     * @param username o nome de usuário
     * @param password a senha do usuário
     * @param fullName o nome completo do usuário
     * @param cpf o CPF do usuário
     * @param email o e-mail do usuário
     * @param isAdmin se o usuário é administrador
     */
    public Usuario(String username, String password, String fullName, String cpf, String email, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.cpf = cpf;
        this.email = email;
        this.isAdmin = isAdmin;
        this.Ingressos = new ArrayList<>();
        this.Recibos = new ArrayList<>();
    }

    /**
     * Retorna o nome de usuário.
     * 
     * @return o nome de usuário
     */
    public String getLogin() {
        return username;
    }

    /**
     * Retorna o nome completo do usuário.
     * 
     * @return o nome completo do usuário
     */
    public String getNome() {
        return fullName;
    }

    /**
     * Retorna a senha do usuário.
     * 
     * @return a senha do usuário
     */
    public String getSenha() {
        return password;
    }

    /**
     * Retorna o CPF do usuário.
     * 
     * @return o CPF do usuário
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Retorna o e-mail do usuário.
     * 
     * @return o e-mail do usuário
     */
    public String getEmail() {
        return email;
    }

    /**
     * Verifica se o usuário é administrador.
     * 
     * @return {@code true} se o usuário é administrador, {@code false} caso contrário
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Define o nome de usuário.
     * 
     * @param username o nome de usuário
     */
    public void setLogin(String username) {
        this.username = username;
    }

    /**
     * Define a senha do usuário.
     * 
     * @param password a nova senha
     */
    public void setSenha(String password) {
        this.password = password;
    }

    /**
     * Define o nome completo do usuário.
     * 
     * @param fullName o nome completo
     */
    public void setNome(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Define o CPF do usuário.
     * 
     * @param cpf o novo CPF
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * Define o e-mail do usuário.
     * 
     * @param email o novo e-mail
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Define se o usuário é administrador.
     * 
     * @param isAdmin o status de administrador
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * Verifica se o login (username e senha) é válido.
     * 
     * @param username o nome de usuário
     * @param password a senha
     * @return {@code true} se o login for válido, {@code false} caso contrário
     */
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    /**
     * Adiciona um ingresso à lista de ingressos do usuário.
     * 
     * @param ingresso o ingresso a ser adicionado
     */
    public void adicionarIngresso(Ingresso ingresso) {
        this.Ingressos.add(ingresso);
    }

    /**
     * Retorna a lista de ingressos comprados pelo usuário.
     * 
     * @return a lista de ingressos
     */
    public List<Ingresso> getIngressos() {
        return this.Ingressos;
    }

    /**
     * Remove um ingresso da lista de ingressos do usuário se ele for cancelado.
     *
     * @param ingresso o ingresso a ser removido
     * @param evento o evento associado ao ingresso
     * @param dataAtual a data atual para comparação
     * @return {@code true} se o ingresso foi removido, {@code false} caso contrário
     */
    public boolean removeIngresso(Ingresso ingresso, Evento evento, Date dataAtual) {
        if (evento.getData().before(dataAtual)) {
            Ingressos.remove(ingresso);
            ingresso = null; // Excluir o objeto ingresso
            return true;
        }
        return false;
    }

    // ATUALIZAÇÃO DE CÓDIGO
    
    /**
     * Atualiza os dados do usuário.
     * 
     * @param username o novo nome de usuário
     * @param password a nova senha
     * @param fullName o novo nome completo
     * @param email o novo e-mail
     */
    public void atualizarDados(String username, String password, String fullName, String email) {
        this.setLogin(username);
        this.setSenha(password);
        this.setNome(fullName);
        this.setEmail(email);
    }

    /**
     * Adiciona um recibo à lista de recibos do usuário.
     * 
     * @param recibo o recibo a ser adicionado
     */
    public void adicionarRecibo(Recibo recibo) {
        this.Recibos.add(recibo);
    }

    /**
     * Retorna a lista de recibos do usuário.
     * 
     * @return a lista de recibos
     */
    public List<Recibo> getRecibos() {
        return this.Recibos;
    }

    /**
     * Gera o código hash baseado no CPF do usuário.
     * 
     * @return o código hash
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
        return result;
    }

    /**
     * Verifica se este usuário é igual a outro objeto.
     * 
     * @param obj o objeto a ser comparado
     * @return {@code true} se os objetos forem iguais, {@code false} caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Usuario other = (Usuario) obj;
        if (cpf == null) {
            if (other.cpf != null)
                return false;
        } else if (!cpf.equals(other.cpf))
            return false;
        return true;
    }
}
