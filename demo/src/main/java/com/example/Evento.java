

package com.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A classe {@code Evento} representa um evento disponível para a venda de ingressos.
 * Cada evento possui um nome, descrição, data e uma quantidade de ingressos disponíveis.
 * Também permite adicionar avaliações feitas pelos usuários.
 */
public class Evento {
    private String ID;
    private String Nome;
    private String Descricao;
    private Date Data;
    private int QuantidadeIngressos;
    private Map<String, String> avaliacoes;
    private double Preco;

    /**
     * Construtor da classe {@code Evento}.
     *
     * @param Nome o nome do evento
     * @param Descricao a descrição do evento
     * @param Data a data em que o evento ocorrerá
     * @param ingressos a quantidade de ingressos disponíveis para o evento
     */
    public Evento(String Nome, String Descricao, Date Data, int ingressos) {
        this.ID = gerarId(Data, Nome);
        this.Nome = Nome;
        this.Descricao = Descricao;
        this.Data = ajustarData(Data);
        this.QuantidadeIngressos = ingressos;
        this.avaliacoes = new HashMap<>();
        this.Preco = 0.0;
    }

    /**
     * Construtor da classe {@code Evento}.
     *
     * @param Nome o nome do evento
     * @param Descricao a descrição do evento
     * @param Data a data em que o evento ocorrerá
     * @param ingressos a quantidade de ingressos disponíveis para o evento
     */
    public Evento(String Nome, String Descricao, Date Data, int ingressos, double Preco) {
        this.ID = gerarId(Data, Nome);
        this.Nome = Nome;
        this.Descricao = Descricao;
        this.Data = ajustarData(Data);
        this.QuantidadeIngressos = ingressos;
        this.avaliacoes = new HashMap<>();
        this.Preco = Preco;
    }

    /**
     * Gera um ID único para o evento com base na data e no nome do evento.
     * 
     * @param data a data do evento
     * @param Nome o nome do evento
     * @return o ID gerado no formato "yymmdd-Nome"
     */
    private String gerarId(Date data, String Nome) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String dataEventoString = sdf.format(data);
        return dataEventoString + "-" + Nome;
    }

    /**
     * Ajusta a data para definir os campos de hora, minuto, segundo e milissegundo como zero.
     * 
     * @param data a data a ser ajustada
     * @return a data ajustada com hora, minuto, segundo e milissegundo zerados
     */
    private Date ajustarData(Date data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Retorna o ID do evento.
     * 
     * @return o ID do evento
     */
    public String getID() {
        return ID;
    }

    /**
     * Retorna o nome do evento.
     *
     * @return o nome do evento
     */
    public String getNome() {
        return Nome;
    }

    /**
     * Retorna a descrição do evento.
     *
     * @return a descrição do evento
     */
    public String getDescricao() {
        return Descricao;
    }

    /**
     * Retorna a data do evento.
     *
     * @return a data do evento
     */
    public Date getData() {
        return Data;
    }

    /**
     * Retorna a quantidade de ingressos disponíveis para o evento.
     * 
     * @return a quantidade de ingressos
     */
    public int getIngressos() {
        return this.QuantidadeIngressos;
    }

    /**
     * Define a quantidade de ingressos disponíveis para o evento.
     * 
     * @param ingressos a nova quantidade de ingressos
     */
    public void setIngressos(int ingressos) {
        this.QuantidadeIngressos = ingressos;
    }

    /**
     * Verifica se o evento está ativo com base na data atual.
     * O evento é considerado ativo se a data do evento for posterior à data especificada.
     *
     * @param data a data de referência para verificação
     * @return {@code true} se o evento está ativo, {@code false} caso contrário
     */
    public boolean isAtivo(Date data) {
        return !this.Data.before(data);
    }

    /**
     * Adiciona uma avaliação feita por um usuário ao evento.
     * 
     * @param usuario o nome do usuário que fez a avaliação
     * @param avaliacao o texto da avaliação
     */
    public void adicionarAvaliacao(String usuario, String avaliacao) {
        avaliacoes.put(usuario, avaliacao);
    }

    /**
     * Retorna o mapa de avaliações do evento, onde a chave é o nome do usuário
     * e o valor é o texto da avaliação.
     * 
     * @return o mapa de avaliações do evento
     */
    public Map<String, String> getAvaliacoes() {
        return avaliacoes;
    }

    /**
     * Exibe todas as avaliações do evento, imprimindo o usuário e a respectiva avaliação.
     */
    public void exibirAvaliacoes() {
        for (Map.Entry<String, String> usuario : avaliacoes.entrySet()) {
            System.out.println("Usuário: " + usuario.getKey() + " - Avaliação: " + usuario.getValue());
        }
    }

    public double getPreco(){
        return this.Preco;
    }

    public void setPreco(double preco){
        this.Preco = preco;
        System.out.println("Preço atualizado com sucesso!");
    }

    /**
     * Gera o código hash para o evento com base no seu ID.
     * 
     * @return o código hash do evento
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ID == null) ? 0 : ID.hashCode());
        return result;
    }

    /**
     * Compara este evento com outro objeto para verificar se são iguais.
     * Dois eventos são considerados iguais se tiverem o mesmo ID.
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
        Evento other = (Evento) obj;
        if (ID == null) {
            if (other.ID != null)
                return false;
        } else if (!ID.equals(other.ID))
            return false;
        return true;
    }
}
