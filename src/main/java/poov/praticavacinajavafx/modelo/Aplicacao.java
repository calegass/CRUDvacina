package poov.praticavacinajavafx.modelo;

import java.time.LocalDate;

public class Aplicacao {
//    Long codigo
//    LocalDate data
//    Pessoa pessoa
//    Vacina vacina
//    Situacao situacao

    private Long codigo;
    private LocalDate data;
    private Pessoa pessoa;
    private Vacina vacina;
    private Situacao situacao;

    public Aplicacao() {
        codigo = -1L;
        data = LocalDate.now();
        pessoa = new Pessoa();
        vacina = new Vacina();
        situacao = Situacao.ATIVO;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Vacina getVacina() {
        return vacina;
    }

    public void setVacina(Vacina vacina) {
        this.vacina = vacina;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }

    public Long getCodigo() {
        return codigo;
    }
}
