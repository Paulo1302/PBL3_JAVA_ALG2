
package com.example;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe responsável por gerenciar o armazenamento de dados dos usuários e eventos,
 * utilizando arquivos JSON para persistência.
 */
public class Armazenamento {

    private final String baseDir;

    public Armazenamento() {
        // Obtém o diretório de trabalho do projeto (onde o código está sendo executado)
        String userHome = System.getProperty("user.home");

        // Definindo o caminho para a pasta "Data" no desktop do usuário
        this.baseDir = userHome + File.separator + "Desktop" + File.separator + "PBL3" + File.separator + "demo" + File.separator + "Data";

        // Depuração: Exibe o caminho gerado
        System.out.println("Caminho gerado: " + this.baseDir);
    }

    // Método para verificar se o diretório existe
    public boolean verificarExistencia() {
        File dir = new File(this.baseDir);
        if (!dir.exists()) {
            System.out.println("O diretório não existe: " + this.baseDir);
            return false;
        }
        System.out.println("O diretório foi encontrado: " + this.baseDir);
        return true;
    }

    /**
     * Armazena os dados de um usuário em um arquivo JSON.
     * @param usuario o objeto {@code Usuario} a ser armazenado
     */
    public void ArmazenamentoUser(Usuario usuario) {
        Gson gsonFile = new Gson();
        String jsonFile = gsonFile.toJson(usuario);
        String UserCPF = usuario.getCpf();

        String caminhoArquivo = baseDir + File.separator + "Usuarios" + File.separator + UserCPF + ".json";

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(caminhoArquivo), "UTF-8")) {
            writer.write(jsonFile);
            System.out.println("Dados do usuário armazenados com sucesso!");
        } catch (IOException erro) {
            erro.printStackTrace();
        }
    }

    /**
     * Lê os dados de um usuário a partir de um arquivo JSON.
     * @param cpf o CPF do usuário para buscar os dados
     * @return o objeto {@code Usuario} lido do arquivo, ou {@code null} se ocorrer um erro
     */
    public Usuario LerArquivoUsuario(String cpf) {
        Gson gson = new Gson();
        String caminhoArquivo = baseDir + File.separator + "Usuarios" + File.separator + cpf + ".json";

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(caminhoArquivo), "UTF-8")) {
            Usuario usuario = gson.fromJson(reader, Usuario.class);
            System.out.println("Dados do usuário lidos com sucesso!");
            return usuario;
        } catch (IOException | JsonSyntaxException erro) {
            erro.printStackTrace();
            return null;
        }
    }

    /**
     * Armazena os dados de um evento em um arquivo JSON.
     * @param evento o objeto {@code Evento} a ser armazenado
     */
    public void ArmazenarEvento(Evento evento) {
        Gson gsonFile = new Gson();
        String jsonFile = gsonFile.toJson(evento);
        String eventoID = evento.getID();

        String caminhoArquivo = baseDir + File.separator + "Eventos" + File.separator + eventoID + ".json";

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(caminhoArquivo), "UTF-8")) {
            writer.write(jsonFile);
            System.out.println("Dados do evento armazenados com sucesso");
        } catch (IOException erro) {
            erro.printStackTrace();
        }
    }

    /**
     * Lê os dados de um evento a partir de um arquivo JSON.
     * @param eventoid o ID do evento para buscar os dados
     * @return o objeto {@code Evento} lido do arquivo, ou {@code null} se ocorrer um erro
     */
    public Evento LerArquivoEvento(String eventoid) {
        Gson gson = new Gson();
        String caminhoArquivo = baseDir + File.separator + "Eventos" + File.separator + eventoid + ".json";

        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(caminhoArquivo), "UTF-8")) {
            Evento evento = gson.fromJson(reader, Evento.class);
            System.out.println("Evento lido com sucesso");
            return evento;
        } catch (IOException | JsonSyntaxException erro) {
            erro.printStackTrace();
            return null;
        }
    }

    /**
     * Lista todos os arquivos JSON na pasta de eventos que possuem uma data posterior à data atual.
     * @return uma lista com os nomes dos arquivos JSON na pasta Eventos com data posterior à data atual
     */
    public List<String> listarEventosDisponiveis() {
        List<String> arquivosJson = new ArrayList<>();
        File diretorio = new File(baseDir + File.separator + "Eventos");
        Date dataAtual = new Date();

        if (diretorio.exists() && diretorio.isDirectory()) {
            File[] arquivos = diretorio.listFiles();

            if (arquivos != null) {
                for (File arquivo : arquivos) {
                    if (arquivo.isFile() && arquivo.getName().endsWith(".json")) {
                        String nomeArquivo = arquivo.getName();
                        String nomeEvento = nomeArquivo.substring(0, nomeArquivo.lastIndexOf(".json")); // Remover extensão
                        String dataString = nomeEvento.substring(0, 6); // Pega os primeiros 6 caracteres (yymmdd)

                        try {
                            Date dataArquivo = new SimpleDateFormat("yyMMdd").parse(dataString);
                            if (dataArquivo.after(dataAtual)) {
                                arquivosJson.add(nomeEvento); // Adiciona sem extensão
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return arquivosJson;
    }
}
