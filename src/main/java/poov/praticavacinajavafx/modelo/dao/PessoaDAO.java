package poov.praticavacinajavafx.modelo.dao;

import poov.praticavacinajavafx.modelo.Pessoa;
import poov.praticavacinajavafx.modelo.Situacao;
import poov.praticavacinajavafx.modelo.dao.core.GenericJDBCDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PessoaDAO extends GenericJDBCDAO<Pessoa, Long> {

    public PessoaDAO(Connection conexao) {
        super(conexao);
    }

    public String FIND_ALL_QUERY = "SELECT codigo, nome, cpf, nascimento, situacao FROM pessoa WHERE situacao='ATIVO'";
    private final String FIND_BY_KEY_QUERY = FIND_ALL_QUERY + "AND codigo=? ";

    @Override
    protected void setKeyInStatementFromEntity(PreparedStatement statement, Pessoa entity) throws SQLException {
        statement.setLong(1, entity.getCodigo());
    }

    @Override
    protected void setKeyInStatement(PreparedStatement statement, Long key) throws SQLException {
        statement.setLong(1, key);
    }

    @Override
    protected void setKeyInEntity(ResultSet rs, Pessoa entity) throws SQLException {
        entity.setCodigo(rs.getLong(1));
    }

    @Override
    protected Pessoa toEntity(ResultSet resultSet) throws SQLException {
        Pessoa pessoa = new Pessoa();
        pessoa.setCodigo(resultSet.getLong("codigo"));
        pessoa.setNome(resultSet.getString("nome"));
        pessoa.setCpf(resultSet.getString("cpf"));
        pessoa.setDataNascimento(resultSet.getDate("nascimento").toLocalDate());
        if (resultSet.getString("situacao").equals("ATIVO")) {
            pessoa.setSituacao(Situacao.ATIVO);
        } else {
            pessoa.setSituacao(Situacao.INATIVO);
        }
        return pessoa;
    }

    @Override
    protected void addParameters(PreparedStatement resultSet, Pessoa entity) throws SQLException {

    }

    @Override
    protected String findByKeyQuery() {
        return FIND_BY_KEY_QUERY;
    }

    @Override
    protected String findAllQuery() {
        return FIND_ALL_QUERY;
    }

    @Override
    protected String updateQuery() {
        return null;
    }

    @Override
    protected String createQuery() {
        return null;
    }

    @Override
    protected String removeQuery() {
        return null;
    }
}
