package poov.praticavacinajavafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import poov.praticavacinajavafx.modelo.Vacina;
import poov.praticavacinajavafx.modelo.dao.core.GenericJDBCDAO;
import poov.praticavacinajavafx.modelo.dao.ConexaoFactoryPostgreSQL;
import poov.praticavacinajavafx.modelo.dao.VacinaDAO;
import poov.praticavacinajavafx.modelo.dao.core.ConnectionFactory;
import poov.praticavacinajavafx.modelo.dao.core.DAOFactory;

import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.List;

public class EditController {
    public Button buttonCancelarEdit;
    public Button buttonOkEdit;
    private Vacina vacina;

    public static EditarAdicionar editarAdicionar;

    public HomeController homeController;

    void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    public void setEditarAdicionar(EditarAdicionar editarAdicionar) {
        EditController.editarAdicionar = editarAdicionar;

        List<Vacina> vacinas = pesquisarTodas();

        long topID = 0;
        if(vacinas != null) {
            topID = vacinas.get(0).getCodigo();
            for (Vacina vacina : vacinas) {
                if (vacina.getCodigo() > topID) {
                    topID = vacina.getCodigo();
                }
            }
        }

        textFieldCodigoEdit.setEditable(true);
        textFieldCodigoEdit.setDisable(false);
        if (vacinas == null) {
            textFieldCodigoEdit.setText("1");
        } else {
            if (editarAdicionar == EditarAdicionar.EDITAR)
                textFieldCodigoEdit.setText(String.valueOf(vacina.getCodigo()));
            else if (editarAdicionar == EditarAdicionar.ADICIONAR)
                textFieldCodigoEdit.setText(String.valueOf(topID + 1));
        }
        textFieldCodigoEdit.setEditable(false);
        textFieldCodigoEdit.setDisable(true);
        textFieldNomeEdit.requestFocus();

        if(editarAdicionar == EditarAdicionar.EDITAR) {
            textFieldNomeEdit.setText(vacina.getNome());
            textFieldDescricaoEdit.setText(vacina.getDescricao());
        } else {
            textFieldNomeEdit.clear();
            textFieldDescricaoEdit.clear();
        }
    }

    public void setVacina(Vacina vacina) {
        this.vacina = vacina;
    }

    public Vacina getVacina() {
        return vacina;
    }

    public void updateCreateAction() {
        if(editarAdicionar == EditarAdicionar.EDITAR) {
            // vacina.setCodigo(Long.parseLong(textFieldCodigoEdit.getText()));



            if (!textFieldNomeEdit.getText().isEmpty() &&
                    !textFieldDescricaoEdit.getText().isEmpty()) {

                vacina.setNome(textFieldNomeEdit.getText());
                vacina.setDescricao(textFieldDescricaoEdit.getText());

                editarVacina();
                homeController.buttonPesquisarClicado();

                Stage stage = (Stage) textFieldNomeEdit.getScene().getWindow();
                stage.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Vacina editada com sucesso!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Preencha todos os campos");
                alert.showAndWait();
            }

        } else if(editarAdicionar == EditarAdicionar.ADICIONAR) {

            if(!textFieldNomeEdit.getText().isEmpty() &&
                    !textFieldDescricaoEdit.getText().isEmpty()) {

                criarVacina();
                homeController.buttonPesquisarClicado();

                Stage stage = (Stage) textFieldNomeEdit.getScene().getWindow();
                stage.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Vacina adicionada com sucesso");
                alert.showAndWait();

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Preencha todos os campos");
                alert.showAndWait();
            }
        }

    }

    private void criarVacina() {

        vacina = new Vacina(textFieldNomeEdit.getText(), textFieldDescricaoEdit.getText());

        ConnectionFactory conexaoFactory = new ConexaoFactoryPostgreSQL("localhost:5432/poov", "postgres", "postgres");
        DAOFactory factory = new DAOFactory(conexaoFactory);

        try {
            factory.abrirConexao();
            VacinaDAO dao = factory.getDAO(VacinaDAO.class);

            dao.create(vacina);

        } catch (SQLException ex) {
            GenericJDBCDAO.showSQLException(ex);
        } finally {
            factory.fecharConexao();
        }
    }

    private void editarVacina() {
        ConnectionFactory conexaoFactory = new ConexaoFactoryPostgreSQL("localhost:5432/poov", "postgres", "postgres");
        DAOFactory factory = new DAOFactory(conexaoFactory);

        try {
            factory.abrirConexao();
            VacinaDAO dao = factory.getDAO(VacinaDAO.class);

            dao.update(vacina);

        } catch (SQLException ex) {
            GenericJDBCDAO.showSQLException(ex);
        } finally {
            factory.fecharConexao();
        }
    }

    private List<Vacina> pesquisarTodas() {
        ConnectionFactory conexaoFactory = new ConexaoFactoryPostgreSQL("localhost:5432/poov", "postgres", "postgres");
        DAOFactory factory = new DAOFactory(conexaoFactory);
        try {
            factory.abrirConexao();
            VacinaDAO dao = factory.getDAO(VacinaDAO.class);
            List<Vacina> vacinas = dao.findAllIgnoringSituation();

            if (vacinas.isEmpty()) {
                System.out.println("Nao existem vacinas salvas no BD");
            } else {
                for (Vacina vacina : vacinas) {
                    System.out.println(vacina);
                }
                return vacinas;
            }
        } catch (SQLException ex) {
            GenericJDBCDAO.showSQLException(ex);
        } finally {
            factory.fecharConexao();
        }
        return null;
    }

    public void closeAction() {
        Stage stage = (Stage) textFieldNomeEdit.getScene().getWindow();
        stage.close();
    }

    public void limpar() {
        textFieldDescricaoEdit.setText("");
        textFieldCodigoEdit.setText("");
        textFieldNomeEdit.setText("");
    }

    public EditController() {
        System.out.println("EditController criado");
    }

    @FXML
    private TextField textFieldDescricaoEdit;

    @FXML
    private TextField textFieldCodigoEdit;

    @FXML
    private TextField textFieldNomeEdit;
}
