package poov.praticavacinajavafx.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import poov.praticavacinajavafx.VacinaApplication;
import poov.praticavacinajavafx.modelo.Aplicacao;
import poov.praticavacinajavafx.modelo.Pessoa;
import poov.praticavacinajavafx.modelo.Situacao;
import poov.praticavacinajavafx.modelo.dao.AplicacaoDAO;
import poov.praticavacinajavafx.modelo.dao.PessoaDAO;
import poov.praticavacinajavafx.modelo.dao.core.GenericJDBCDAO;
import poov.praticavacinajavafx.modelo.Vacina;
import poov.praticavacinajavafx.modelo.dao.ConexaoFactoryPostgreSQL;
import poov.praticavacinajavafx.modelo.dao.VacinaDAO;
import poov.praticavacinajavafx.modelo.dao.core.ConnectionFactory;
import poov.praticavacinajavafx.modelo.dao.core.DAOFactory;

import java.io.IOException;
import java.sql.SQLException;

public class HomeController implements Initializable {

    public HomeController() {
        System.out.println("HomeController criado");
    }

    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("HomeController inicializado");

        aplicacao = new Aplicacao();

        buttonPesquisarPessoaClicado();
        buttonPesquisarClicado();

        FXMLLoader loader = new FXMLLoader(VacinaApplication.class.getResource("edit-window.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = new Scene(root);
            stageEdit = new Stage();
            stageEdit.setScene(scene);
            editController = loader.getController();
            stageEdit.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon.png"))));

            editController.setHomeController(this);

            System.out.println("EditController: " + editController);

        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo FXML: " + e.getMessage());
            e.printStackTrace();
        }

        TextFormatter<String> formatterApenasDigitos = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }
            String text = change.getControlNewText();
            //System.out.println(text);
            if (text.isEmpty()) {  // permite campo vazio
                return change;
            } else {  // verifica se o texto, com a mudança, é um long válido
                try {
                    Long.parseLong(text);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return change;
        });

        TextFormatter<String> formatterApenasDigitosPessoa = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }
            String text = change.getControlNewText();
            //System.out.println(text);
            if (text.isEmpty()) {  // permite campo vazio
                return change;
            } else {  // verifica se o texto, com a mudança, é um long válido
                try {
                    Long.parseLong(text);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return change;
        });

        textfieldCodigo.setTextFormatter(formatterApenasDigitos);
        textfieldCodigoPessoa.setTextFormatter(formatterApenasDigitosPessoa);

        columnDataPessoa.setCellFactory(column -> new TableCell<Pessoa, LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(formatter.format(date));
                }
            }
        });

        tableviewPessoa.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && (!tableviewPessoa.getSelectionModel().isEmpty())) {
                pessoaSelecionada = true;
                aplicacao.setPessoa(tableviewPessoa.getSelectionModel().getSelectedItem());
            }
        });

        tableviewVacina.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && (!tableviewVacina.getSelectionModel().isEmpty())) {
                Vacina selectedVacina = tableviewVacina.getSelectionModel().getSelectedItem();
                vacinaSelecionada = true;
                editController.setVacina(selectedVacina);
                aplicacao.setVacina(selectedVacina);
            }
        });
    }

    @FXML
    public void buttonPesquisarPessoaClicado() {
        pessoaSelecionada = false;
        ConnectionFactory conexaoFactory = new ConexaoFactoryPostgreSQL("localhost:5432/poov", "postgres", "postgres");
        DAOFactory factory = new DAOFactory(conexaoFactory);

        tableviewPessoa.getItems().clear();

        try {
            factory.abrirConexao();
            PessoaDAO dao = factory.getDAO(PessoaDAO.class);

            if(!textfieldCodigoPessoa.getText().isEmpty())
                dao.FIND_ALL_QUERY = dao.FIND_ALL_QUERY + " AND codigo=" + textfieldCodigoPessoa.getText();
            if(!textfieldNomePessoa.getText().isEmpty())
                dao.FIND_ALL_QUERY = dao.FIND_ALL_QUERY + " AND upper(nome) LIKE upper('%" + textfieldNomePessoa.getText() + "%')";
            if(!textFieldCpfPessoa.getText().isEmpty())
                dao.FIND_ALL_QUERY = dao.FIND_ALL_QUERY + " AND upper(cpf) LIKE upper('%" + textFieldCpfPessoa.getText() + "%')";

            List<Pessoa> pessoas = dao.findAll();

            if (datepickerInicio.getValue() != null) {
                pessoas.removeIf(pessoa -> pessoa.getDataNascimento().isBefore(datepickerInicio.getValue()));
            }

            if (datepickerFinal.getValue() != null) {
                pessoas.removeIf(pessoa -> pessoa.getDataNascimento().isAfter(datepickerFinal.getValue()));
            }



            if (pessoas.isEmpty()) {
                System.out.println("Nao existem pessoas salvas no BD");

            } else {
                for (Pessoa pessoa : pessoas) {
                    System.out.println(pessoa);
                }
                pessoas.sort(Comparator.comparing(Pessoa::getCodigo));
                columnCodigoPessoa.setCellValueFactory(new PropertyValueFactory<Pessoa, Long>("codigo"));
                columnNomePessoa.setCellValueFactory(new PropertyValueFactory<Pessoa, String>("nome"));
                columnCPFPessoa.setCellValueFactory(new PropertyValueFactory<Pessoa, String>("cpf"));
                columnDataPessoa.setCellValueFactory(new PropertyValueFactory<Pessoa, LocalDate>("dataNascimento"));
                tableviewPessoa.getItems().addAll(pessoas);
            }
        } catch (SQLException ex) {
            GenericJDBCDAO.showSQLException(ex);
        } finally {
            factory.fecharConexao();
        }
    }

    @FXML
    private void buttonCriarClicado() {
//        Long codigo
//        LocalDate data
//        Pessoa pessoa
//        Vacina vacina
//        Situacao situacao

        if(vacinaSelecionada && pessoaSelecionada) {
            aplicacao.setData(LocalDate.now());
            aplicacao.setSituacao(Situacao.ATIVO);

            ConnectionFactory conexaoFactory = new ConexaoFactoryPostgreSQL("localhost:5432/poov", "postgres", "postgres");
            DAOFactory factory = new DAOFactory(conexaoFactory);

            try {
                factory.abrirConexao();
                AplicacaoDAO dao = factory.getDAO(AplicacaoDAO.class);

                dao.create(aplicacao);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Aplicação adicionada");
                alert.setHeaderText("Sucesso!");
                alert.setContentText("Aplicação adicionada com sucesso");
                alert.showAndWait();

            } catch (SQLException ex) {
                GenericJDBCDAO.showSQLException(ex);
            } finally {
                factory.fecharConexao();
            }
            aplicacao = new Aplicacao();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Nenhuma vacina ou pessoa selecionada");
            alert.setContentText("Selecione uma vacina e uma pessoa para criar uma aplicação");
            alert.showAndWait();
        }
    }

    @FXML
    void buttonAdicionarClicado() {
        stageEdit.setTitle("Adicionar vacina");
        editController.setEditarAdicionar(EditarAdicionar.ADICIONAR);
        stageEdit.showAndWait();
        buttonPesquisarClicado();
    }

    @FXML
    void buttonEditarClicado() {
        if ((editController.getVacina() != null) && (!tableviewVacina.getSelectionModel().isEmpty())) {
            stageEdit.setTitle("Editar vacina");
            editController.setEditarAdicionar(EditarAdicionar.EDITAR);
            stageEdit.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Nenhuma vacina selecionada");
            alert.setContentText("Selecione uma vacina para editar");
            alert.showAndWait();
        }
    }

    @FXML
    public void buttonRemoverClicado() {
        if ((editController.getVacina() != null) && (!tableviewVacina.getSelectionModel().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar remoção");
            alert.setHeaderText("Tem certeza que deseja remover essa vacina?");
            alert.setContentText("Esta ação não pode ser desfeita.");

            // Botões do Alerta
            ButtonType buttonTypeSim = new ButtonType("SIM");
            ButtonType buttonTypeNao = new ButtonType("NÃO");

            alert.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

            // Exibe o alerta e espera pela resposta do usuário
            java.util.Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeSim) {
                Vacina vacina = editController.getVacina();
                ConnectionFactory conexaoFactory = new ConexaoFactoryPostgreSQL("localhost:5432/poov", "postgres", "postgres");
                DAOFactory factory = new DAOFactory(conexaoFactory);
                try {
                    factory.abrirConexao();
                    VacinaDAO dao = factory.getDAO(VacinaDAO.class);

                    dao.deleteById(vacina.getCodigo());

                    editController.setVacina(null);

                    buttonPesquisarClicado();
                } catch (SQLException ex) {
                    GenericJDBCDAO.showSQLException(ex);
                } finally {
                    factory.fecharConexao();
                }
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Nenhuma vacina selecionada");
            alert.setContentText("Selecione uma vacina para remover");
            alert.showAndWait();
        }
    }

    @FXML
    public void buttonPesquisarClicado() {
        vacinaSelecionada = false;
        ConnectionFactory conexaoFactory = new ConexaoFactoryPostgreSQL("localhost:5432/poov", "postgres", "postgres");
        DAOFactory factory = new DAOFactory(conexaoFactory);

        tableviewVacina.getItems().clear();

        try {
            factory.abrirConexao();
            VacinaDAO dao = factory.getDAO(VacinaDAO.class);

            if(!textfieldCodigo.getText().isEmpty())
                dao.FIND_ALL_QUERY = dao.FIND_ALL_QUERY + " AND codigo=" + textfieldCodigo.getText();
            if(!textfieldNome.getText().isEmpty())
                dao.FIND_ALL_QUERY = dao.FIND_ALL_QUERY + " AND upper(nome) LIKE upper('%" + textfieldNome.getText() + "%')";
            if(!textFieldDescricao.getText().isEmpty())
                dao.FIND_ALL_QUERY = dao.FIND_ALL_QUERY + " AND upper(descricao) LIKE upper('%" + textFieldDescricao.getText() + "%')";

            List<Vacina> vacinas = dao.findAll();

            if (vacinas.isEmpty()) {
                System.out.println("Nao existem vacinas salvas no BD");

            } else {
                for (Vacina vacina : vacinas) {
                    System.out.println(vacina);
                }
                vacinas.sort(Comparator.comparing(Vacina::getCodigo));
                columnCodigo.setCellValueFactory(new PropertyValueFactory<Vacina, Long>("codigo"));
                columnNome.setCellValueFactory(new PropertyValueFactory<Vacina, String>("nome"));
                ColumnDescricao.setCellValueFactory(new PropertyValueFactory<Vacina, String>("descricao"));
                tableviewVacina.getItems().addAll(vacinas);
            }
        } catch (SQLException ex) {
            GenericJDBCDAO.showSQLException(ex);
        } finally {
            factory.fecharConexao();
        }
    }

    public void limparPessoaAction() {
        textfieldCodigoPessoa.setText("");
        textfieldNomePessoa.setText("");
        textFieldCpfPessoa.setText("");
        datepickerInicio.setValue(null);
        datepickerFinal.setValue(null);
    }

    public void limparAction() {
        textfieldCodigo.setText("");
        textfieldNome.setText("");
        textFieldDescricao.setText("");
    }

    public Button limparPessoa;
    public Button limpar;
    @FXML
    private TableColumn<Pessoa, Long> columnCodigoPessoa;
    public TableColumn<Pessoa, String> columnNomePessoa;
    public TableColumn<Pessoa, String> columnCPFPessoa;
    public TableColumn<Pessoa, LocalDate> columnDataPessoa;
    public TextField textfieldCodigoPessoa;
    public TextField textfieldNomePessoa;
    public TextField textFieldCpfPessoa;
    public Button buttonPesquisarPessoa;
    public DatePicker datepickerInicio;
    public DatePicker datepickerFinal;
    public Button buttonCriar;
    public TableView<Pessoa> tableviewPessoa;
    private Aplicacao aplicacao;
    private boolean vacinaSelecionada = false;
    private boolean pessoaSelecionada = false;
    private EditController editController;
    private Stage stageEdit;
    @FXML
    public TableColumn<Vacina, Long> columnCodigo;
    @FXML
    public TableColumn<Vacina, String> columnNome;
    @FXML
    public TableColumn<Vacina, String> ColumnDescricao;
    @FXML
    private Button buttonAdicionar;
    @FXML
    private Button buttonEditar;
    @FXML
    private Button buttonPesquisar;
    @FXML
    private Button buttonRemover;
    @FXML
    private TableView<Vacina> tableviewVacina;
    @FXML
    private TextField textFieldDescricao;
    @FXML
    private TextField textfieldCodigo;
    @FXML
    private TextField textfieldNome;
}
