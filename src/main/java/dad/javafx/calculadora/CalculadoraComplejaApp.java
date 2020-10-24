package dad.javafx.calculadora;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import javafx.collections.*;
import javafx.geometry.Pos;

public class CalculadoraComplejaApp extends Application {

	// ELEMENTOS DE LA INTERFAZ
	private VBox primerBloqueVbox;
	private ComboBox<String> operacionCombo;
	// -------------------------------------
	private VBox segundoBloqueVbox;
	private TextField primerNumeroText;
	private TextField segundoNumeroText;
	private TextField tercerNumeroText;
	private TextField cuartoNumeroText;
	private TextField resultado1Text;
	private TextField resultado2Text;
	private HBox numerosHbox1;
	private HBox numerosHbox2;
	private Separator separador;
	private HBox resultadosHbox;
	
	//PROPERTIES PARA LOS BINDINGS
	
	private DoubleProperty aNumberPro,bNumberPro;
	private DoubleProperty cNumberPro,dNumberPro;
	private DoubleProperty resultadoOperando1DoubleProperty, resultadoOperando2DoubleProperty;

	@Override
	public void start(Stage primaryStage) throws Exception {

		// PRIMER BLOQUE

		ObservableList<String> operaciones = FXCollections.observableArrayList("+", "-", "*", "/");
		operacionCombo = new ComboBox<String>(operaciones);
		

		primerBloqueVbox = new VBox(5, operacionCombo);
		primerBloqueVbox.setFillWidth(false);
		primerBloqueVbox.setAlignment(Pos.CENTER);

		// SEGUNDO BLOQUE

		primerNumeroText = new TextField();
		primerNumeroText.setAlignment(Pos.CENTER);
		primerNumeroText.setPrefWidth(60);

		segundoNumeroText = new TextField();
		segundoNumeroText.setAlignment(Pos.CENTER);
		segundoNumeroText.setPrefWidth(60);

		numerosHbox1 = new HBox(5, primerNumeroText, new Label("+"), segundoNumeroText, new Label("i"));
		numerosHbox1.setFillHeight(false);
		numerosHbox1.setAlignment(Pos.CENTER);

		// ------------------------------------------------

		tercerNumeroText = new TextField();
		tercerNumeroText.setAlignment(Pos.CENTER);
		tercerNumeroText.setPrefWidth(60);

		cuartoNumeroText = new TextField();
		cuartoNumeroText.setAlignment(Pos.CENTER);
		cuartoNumeroText.setPrefWidth(60);

		numerosHbox2 = new HBox(5, tercerNumeroText, new Label("+"), cuartoNumeroText, new Label("i"));
		numerosHbox2.setFillHeight(false);
		numerosHbox2.setAlignment(Pos.CENTER);

		// ------------------------------------------------

		resultado1Text = new TextField();
		resultado1Text.setAlignment(Pos.CENTER);
		resultado1Text.setPrefWidth(60);
		resultado1Text.setEditable(false);

		resultado2Text = new TextField();
		resultado2Text.setAlignment(Pos.CENTER);
		resultado2Text.setPrefWidth(60);
		resultado2Text.setEditable(false);

		resultadosHbox = new HBox(5, resultado1Text, new Label("+"), resultado2Text, new Label("i"));
		resultadosHbox.setFillHeight(false);
		resultadosHbox.setAlignment(Pos.CENTER);

		// ------------------------------------------------

		separador = new Separator();

		segundoBloqueVbox = new VBox(5, numerosHbox1, numerosHbox2, separador, resultadosHbox);
		
		//Bindings
		
		aNumberPro = new SimpleDoubleProperty(0);
		bNumberPro = new SimpleDoubleProperty(0);
		cNumberPro = new SimpleDoubleProperty(0);
		dNumberPro = new SimpleDoubleProperty(0);
		resultadoOperando1DoubleProperty = new SimpleDoubleProperty(0);
		resultadoOperando2DoubleProperty = new SimpleDoubleProperty(0);
		
		Bindings.bindBidirectional(primerNumeroText.textProperty(), aNumberPro, new NumberStringConverter());
		Bindings.bindBidirectional(segundoNumeroText.textProperty(), bNumberPro, new NumberStringConverter());
		Bindings.bindBidirectional(tercerNumeroText.textProperty(), cNumberPro, new NumberStringConverter());
		Bindings.bindBidirectional(cuartoNumeroText.textProperty(), dNumberPro, new NumberStringConverter());
		Bindings.bindBidirectional(resultado1Text.textProperty(), resultadoOperando1DoubleProperty, new NumberStringConverter());
		Bindings.bindBidirectional(resultado2Text.textProperty(), resultadoOperando2DoubleProperty, new NumberStringConverter());
		
		//LE AÑADO UN LISTENER AL COMBO PARA QUE SE HAGAN LOS BINDINGS DEPENDIENDO DE LA OPERACION
		operacionCombo.getSelectionModel().selectedItemProperty().addListener((o,ov,nv) -> operar());
		//AQUI PONGO UNA DESPUES DE AÑADIR EL LISTENER PARA QUE SE EMPIECEN A EJECUTAR LAS OPERACIONES
		operacionCombo.getSelectionModel().selectFirst();

		// RAIZ

		HBox raiz = new HBox(5, primerBloqueVbox, segundoBloqueVbox);
		raiz.setAlignment(Pos.CENTER);
		raiz.setFillHeight(false);

		Scene scene = new Scene(raiz,320,200);

		primaryStage.setScene(scene);
		primaryStage.setTitle("CalculadoraCompleja");
		primaryStage.show();

	}
	
	//DEPENDIENDO DE LA OPERACION SELECCIONADA CAMBIAN LOS BINDINGS
	private void operar() {
		if(operacionCombo.getValue().equals("+")) {
			resultadoOperando1DoubleProperty.bind(Bindings.add(aNumberPro, cNumberPro));
			resultadoOperando2DoubleProperty.bind(Bindings.add(bNumberPro, dNumberPro));
		}else if(operacionCombo.getValue().equals("-")) {
			resultadoOperando1DoubleProperty.bind(Bindings.subtract(aNumberPro, cNumberPro));
			resultadoOperando2DoubleProperty.bind(Bindings.subtract(bNumberPro, dNumberPro));
		}else if(operacionCombo.getValue().equals("*")) {
			resultadoOperando1DoubleProperty.bind(Bindings.subtract(aNumberPro.multiply(cNumberPro), bNumberPro.multiply(dNumberPro)));
			resultadoOperando2DoubleProperty.bind(Bindings.add(aNumberPro.multiply(dNumberPro), bNumberPro.multiply(cNumberPro)));
		}else if(operacionCombo.getValue().equals("/")) {
			//COMO ERA UNA OPERACION PESADA DE HACER, LA DIVIDI EN VARIOS METODOS
			resultadoOperando1DoubleProperty.bind(Bindings.divide(numerador1(),denominador()));
			resultadoOperando2DoubleProperty.bind(Bindings.divide(numerador2(),denominador()));
		}
	}

	private DoubleBinding numerador1() {
		
		DoubleBinding multi1 = aNumberPro.multiply(cNumberPro);
		DoubleBinding multi2 = bNumberPro.multiply(dNumberPro);
		DoubleBinding sumaMultis = multi1.add(multi2);
		
		return sumaMultis;
	}
	
	private DoubleBinding numerador2() {

		DoubleBinding multi1 = bNumberPro.multiply(cNumberPro);
		DoubleBinding multi2 = aNumberPro.multiply(dNumberPro);
		DoubleBinding restaMultis = multi1.subtract(multi2);
		
		return restaMultis;
	}

	private DoubleBinding denominador() {
		DoubleBinding cuadrado1 = cNumberPro.multiply(cNumberPro);
		DoubleBinding cuadrado2 = dNumberPro.multiply(dNumberPro);
		
		DoubleBinding sumaCuadrado = cuadrado1.add(cuadrado2);
		
		return sumaCuadrado;
	}

	public static void main(String[] args) {
		launch(args);

	}

}
