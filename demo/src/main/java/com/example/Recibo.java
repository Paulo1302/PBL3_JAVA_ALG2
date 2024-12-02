

package com.example;

import java.util.Date;

/**
 * A classe {@code Recibo} representa um recibo para a compra de um ingresso de evento.
 * Contém informações sobre o comprador, evento, pagamento e a data da transação.
 */
public class Recibo {
    private String fullName;
    private String cpf;
    private String email;
    private Ingresso ingresso;
    private String eventoID;
    private String Pagamento;
    private Date data;

    /**
     * Construtor da classe {@code Recibo}.
     *
     * @param fullName o nome completo do comprador
     * @param cpf o CPF do comprador
     * @param email o e-mail do comprador
     * @param ingresso o ingresso associado ao recibo
     * @param pagamento o método de pagamento utilizado
     * @param eventoID o ID do evento
     * @param data a data da transação
     */
    public Recibo(String fullName, String cpf, String email, Ingresso ingresso, String pagamento, String eventoID, Date data) {
        this.fullName = fullName;
        this.cpf = cpf;
        this.email = email;
        this.ingresso = ingresso;
        this.Pagamento = pagamento;
        this.eventoID = eventoID;
        this.data = data;    
    }

    /**
     * Retorna o nome completo do comprador.
     *
     * @return o nome completo do comprador
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Retorna o CPF do comprador.
     *
     * @return o CPF do comprador
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * Retorna o e-mail do comprador.
     *
     * @return o e-mail do comprador
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retorna o ingresso associado ao recibo.
     *
     * @return o ingresso associado
     */
    public Ingresso getIngresso() {
        return ingresso;
    }

    /**
     * Retorna o ID do evento associado ao recibo.
     *
     * @return o ID do evento
     */
    public String getEventoID() {
        return eventoID;
    }

    /**
     * Retorna a data da transação.
     *
     * @return a data da transação
     */
    public Date getData() {
        return data;
    }

    /**
     * Retorna o método de pagamento utilizado.
     *
     * @return o método de pagamento
     */
    public String Pagamento() {
        return Pagamento;
    }
}
