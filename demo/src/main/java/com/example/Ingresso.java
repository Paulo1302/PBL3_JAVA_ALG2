

package com.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * A classe {@code Ingresso} representa um ingresso de evento. Cada ingresso tem 
 * informações sobre o evento, preço, e status (ativo ou cancelado).
 */
public class Ingresso {
    private String EventoID; 
    private double Preco;
    private String IngressoID; 
    private boolean Status;

    /**
     * Constrói um ingresso com preço inicial de 0.0.
     * 
     * @param evento o evento associado ao ingresso
     */
    public Ingresso(Evento evento) {
        this.EventoID = evento.getID();
        this.Preco = 0.0;
        this.Status = true;
        this.IngressoID = gerarId(evento);
    }

    /**
     * Constrói um ingresso com um preço específico.
     * 
     * @param evento o evento associado ao ingresso
     * @param Preco o preço do ingresso
     */
    public Ingresso(Evento evento, double Preco) {
        this.EventoID = evento.getID();
        this.Preco = Preco;
        this.Status = true;
        this.IngressoID = gerarId(evento);
    }

    /**
     * Gera um ID único para o ingresso, com base na data do evento e um UUID.
     * 
     * @param evento o evento associado ao ingresso
     * @return o ID gerado para o ingresso
     */
    private String gerarId(Evento evento) {
        Date data = evento.getData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String dataEventoString = sdf.format(data);
        String uuidString = UUID.randomUUID().toString();
        return dataEventoString + "-" + uuidString;
    }
    
    /**
     * Retorna o preço do ingresso.
     * 
     * @return o preço do ingresso
     */
    public double getPreco() {
        return Preco;
    }

    /**
     * Retorna o ID do evento associado ao ingresso.
     * 
     * @return o ID do evento
     */
    public String getEventoID() {
        return this.EventoID;
    }

    /**
     * Retorna o ID do ingresso.
     * 
     * @return o ID do ingresso
     */
    public String getId() {
        return this.IngressoID;
    }

    /**
     * Verifica se o ingresso está ativo.
     * 
     * @return {@code true} se o ingresso está ativo, {@code false} se está cancelado
     */
    public boolean isAtivo() {
        return Status;
    }

    /**
     * Define o status do ingresso.
     * 
     * @param status o novo status do ingresso
     * @return o status atualizado do ingresso
     */
    public Boolean setStatus(Boolean status) {
        this.Status = status;
        return status;
    }

    /**
     * Calcula o código hash do ingresso com base no ID do evento, ID do ingresso e status.
     * 
     * @return o código hash do ingresso
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((EventoID == null) ? 0 : EventoID.hashCode());
        result = prime * result + ((IngressoID == null) ? 0 : IngressoID.hashCode());
        result = prime * result + (Status ? 1231 : 1237);
        return result;
    }

    /**
     * Compara este ingresso com outro objeto para verificar se são iguais.
     * Dois ingressos são considerados iguais se tiverem o mesmo ID de evento, ID de ingresso e status.
     * 
     * @param obj o objeto a ser comparado
     * @return {@code true} se os objetos são iguais, {@code false} caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ingresso other = (Ingresso) obj;
        if (EventoID == null) {
            if (other.EventoID != null)
                return false;
        } else if (!EventoID.equals(other.EventoID))
            return false;
        if (IngressoID == null) {
            if (other.IngressoID != null)
                return false;
        } else if (!IngressoID.equals(other.IngressoID))
            return false;
        if (Status != other.Status)
            return false;
        return true;
    }
}
