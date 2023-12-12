package poov.praticavacinajavafx.modelo;

import java.time.LocalDate;
import java.util.Objects;

public class Pessoa {
//    Long codigo
//    String nome
//    String cpf
//    LocalDate dataNascimento
//    Situacao situacao
    private Long codigo;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private Situacao situacao;

    public Pessoa() {
        codigo = -1L;
        nome = "sem nome";
        cpf = "sem cpf";
        dataNascimento = LocalDate.now();
        situacao = Situacao.ATIVO;
    }

    public Pessoa(String nome, String cpf, LocalDate dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        situacao = Situacao.ATIVO;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pessoa pessoa = (Pessoa) o;
        return Objects.equals(codigo, pessoa.codigo) && Objects.equals(nome, pessoa.nome) && Objects.equals(cpf, pessoa.cpf) && Objects.equals(dataNascimento, pessoa.dataNascimento) && situacao == pessoa.situacao;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, nome, cpf, dataNascimento, situacao);
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                "codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", situacao=" + situacao +
                '}';
    }
}
