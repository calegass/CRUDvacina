package poov.praticavacinajavafx.modelo.dao;

import poov.praticavacinajavafx.modelo.Aplicacao;
import poov.praticavacinajavafx.modelo.Situacao;
import poov.praticavacinajavafx.modelo.dao.core.GenericJDBCDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AplicacaoDAO extends GenericJDBCDAO<Aplicacao, Long> {

    public AplicacaoDAO(Connection conexao) {
        super(conexao);
    }

    private static final String CREATE_QUERY = "INSERT INTO aplicacao (data, codigopessoa, codigovacina) VALUES (?, ?, ?)";

    @Override
    protected String createQuery() {
        return CREATE_QUERY;
    }

    @Override
    protected void setKeyInEntity(ResultSet rs, Aplicacao entity) throws SQLException {
        entity.setCodigo(rs.getLong(1));
    }

    @Override
    protected void addParameters(PreparedStatement resultSet, Aplicacao entity) throws SQLException {

        resultSet.setDate(1, Date.valueOf(entity.getData()));
        resultSet.setLong(2, entity.getPessoa().getCodigo());
        resultSet.setLong(3, entity.getVacina().getCodigo());

//        resultSet.setString(4, entity.getSituacao().toString());

    }

    @Override
    protected void setKeyInStatement(PreparedStatement statement, Long key) throws SQLException {

    }

    @Override
    protected void setKeyInStatementFromEntity(PreparedStatement statement, Aplicacao entity) throws SQLException {

    }

    @Override
    protected Aplicacao toEntity(ResultSet resultSet) throws SQLException {
        return null;
    }

    @Override
    protected String findByKeyQuery() {
        return null;
    }

    @Override
    protected String findAllQuery() {
        return null;
    }

    @Override
    protected String updateQuery() {
        return null;
    }

    @Override
    protected String removeQuery() {
        return null;
    }
}