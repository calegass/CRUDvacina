package poov.praticavacinajavafx.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import poov.praticavacinajavafx.controller.EditController;
import poov.praticavacinajavafx.controller.EditarAdicionar;
import poov.praticavacinajavafx.modelo.Situacao;
import poov.praticavacinajavafx.modelo.Vacina;
import poov.praticavacinajavafx.modelo.dao.core.GenericJDBCDAO;

public class VacinaDAO extends GenericJDBCDAO<Vacina, Long> {

    public VacinaDAO(Connection conexao) {
        super(conexao);
    }

    public String FIND_ALL_QUERY = "SELECT codigo, nome, descricao, situacao FROM vacina WHERE situacao='ATIVO' ";
    private static final String FIND_ALL_QUERY_IGNORING_SITUATION = "SELECT codigo, nome, descricao, situacao FROM vacina ";
    private final String FIND_BY_KEY_QUERY = FIND_ALL_QUERY + "AND codigo=? ";
    private static final String UPDATE_QUERY = "UPDATE vacina SET nome=?, descricao=?, situacao=? WHERE codigo=?";
    private static final String CREATE_QUERY = "INSERT INTO vacina (nome, descricao) VALUES (?, ?)";
    private static final String REMOVE_QUERY = "DELETE FROM vacina WHERE codigo=?";
    private static final String UPDATE_QUERY_INATIVO = "UPDATE vacina SET situacao='INATIVO' WHERE codigo=?";

    @Override
    protected Vacina toEntity(ResultSet resultSet) throws SQLException {
        Vacina vacina = new Vacina();
        vacina.setCodigo(resultSet.getLong("codigo"));
        vacina.setNome(resultSet.getString("nome"));
        vacina.setDescricao(resultSet.getString("descricao"));
        if (resultSet.getString("situacao").equals("ATIVO")) {
            vacina.setSituacao(Situacao.ATIVO);
        } else {
            vacina.setSituacao(Situacao.INATIVO);
        }
        return vacina;
    }

    @Override
    protected void addParameters(PreparedStatement pstmt, Vacina entity) throws SQLException {
        pstmt.setString(1, entity.getNome());
        pstmt.setString(2, entity.getDescricao());

        /*
        adicionei esse condicional para que o campo situacao seja preenchido e o campo codigo tambem!
         */

        if(EditController.editarAdicionar == EditarAdicionar.EDITAR) {
            pstmt.setString(3, entity.getSituacao().toString());
            pstmt.setLong(4, entity.getCodigo());
        }

        /*
        comentei esses itens ja que o codigo é gerado de forma procedural
        e a situacao é padrao ATIVO, nao possuindo ate mesmo um campo no edit!

        pstmt.setString(3, entity.getSituacao().toString());
        if (entity.getCodigo() != null) {
            pstmt.setLong(4, entity.getCodigo());
        }
        */
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
        return UPDATE_QUERY;
    }

    @Override
    protected String createQuery() {
        return CREATE_QUERY;
    }

    @Override
    protected String removeQuery() {
        return REMOVE_QUERY;
    }

    public void deleteById(Long key) {
        try {
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY_INATIVO);
            setKeyInStatement(statement, key);
            statement.executeUpdate();
        } catch (SQLException e) {
            showSQLException(e);
        }
    }

    public List<Vacina> findAllIgnoringSituation() {
        try {
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY_IGNORING_SITUATION);
            ResultSet resultSet = statement.executeQuery();
            return toEntityList(resultSet);
        } catch (SQLException e) {
            showSQLException(e);
        }
        return new ArrayList<>();
    }

    @Override
    protected void setKeyInStatementFromEntity(PreparedStatement statement, Vacina entity) throws SQLException {
        statement.setLong(1, entity.getCodigo());
    }

    @Override
    protected void setKeyInStatement(PreparedStatement statement, Long key) throws SQLException {
        statement.setLong(1, key);
    }

    @Override
    protected void setKeyInEntity(ResultSet rs, Vacina entity) throws SQLException {
        entity.setCodigo(rs.getLong(1));
    }

}
