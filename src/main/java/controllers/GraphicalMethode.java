package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import main.Fraction;
import main.Matrix;
import main.SimplexMethode;

public class GraphicalMethode implements Initializable {
    @FXML
    HBox graphicArea;
    @FXML
    Label solution;

    Stage stage;
    Scene backScene;
    Scene firstScene;
    SimplexMethode simplexMethode;
    boolean exSol;

    Fraction[][] matrix;

    public GraphicalMethode(boolean exSol,SimplexMethode simplexMethode, Stage stage, Scene backScene, Scene firstScene) {
        this.simplexMethode = simplexMethode;
        this.stage = stage;
        this.backScene = backScene;
        this.firstScene = firstScene;
        this.exSol = exSol;
        createMatrix();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createLabel();
        createGraphic();


    }

    public void createGraphic() {
        try {
            createLineChart();
        } catch (Exception e) {
            solDontEx();
        }

        //graphicArea.getChildren().add(getGraph());

    }

    public void createLabel() {
        ArrayList<Integer> tmpBasis = new ArrayList<>();
        for (int i = 0; i < simplexMethode.getSimplexBasis().length; i++) {
            tmpBasis.add(simplexMethode.getSimplexBasis()[i]);
        }
        ArrayList<Fraction> tmpPoints = new ArrayList<>(Collections.nCopies(
                simplexMethode.getSimplexBasis().length + simplexMethode.getSimplexFreeVariables().length, new Fraction(0)));
        for (int i = 0; i < simplexMethode.getSimplexBasis().length; i++) {
            tmpPoints.set(simplexMethode.getSimplexBasis()[i], simplexMethode.getSimplexCore()[i][simplexMethode.getSimplexCore()[0].length - 1]);
        }
        String str = "(";
        for (Fraction fraction : tmpPoints) {
            str += fraction.toString() + ", ";
        }
        str = str.substring(0, str.length() - 2);
        str += ")";

        String str2 = simplexMethode.getSimplexCore()
                [simplexMethode.getSimplexCore().length - 1][simplexMethode.getSimplexCore()[0].length - 1]
                .multiply(new Fraction(-1)).toString();
        String str3 = "";
        for(int i=0;i<simplexMethode.getCellFunction().length-1;i++){
            str3+="+"+simplexMethode.getCellFunction()[i]+"x"+simplexMethode.getxPos()[i];
        }
        str3 = str3.substring(1,str3.length());
        str3 += "->min";
        solution.setText(str3+"\n"+str + " f="+str2);

    }

    public void solDontEx(){
        solution.setText("solution don't exist");
    }

    public void createMatrix() {
        matrix = new Fraction[simplexMethode.getMatrix().length+2][3];
        for (int i = 0; i < simplexMethode.getMatrix().length; i++) {
            matrix[i][0] = simplexMethode.getMatrix()[i][simplexMethode.getMatrix()[0].length - 3].copy();
            matrix[i][1] = simplexMethode.getMatrix()[i][simplexMethode.getMatrix()[0].length - 2].copy();
            matrix[i][2] = simplexMethode.getMatrix()[i][simplexMethode.getMatrix()[0].length - 1].copy();
        }
        matrix[matrix.length-2][0]  = new Fraction(1);
        matrix[matrix.length-2][1]  = new Fraction(0);
        matrix[matrix.length-2][2]  = new Fraction(0);

        matrix[matrix.length-1][0] = new Fraction(0);
        matrix[matrix.length-1][1] = new Fraction(1);
        matrix[matrix.length-1][2] = new Fraction(0);
    }

    public String getStringUravnenie(int i) {
        return matrix[i][0].toString() + "x" + simplexMethode.getSimplexFreeVariables()[0] + "+" +
                matrix[i][1].toString() + "x" + simplexMethode.getSimplexFreeVariables()[1] + "=" +
                matrix[i][2].toString();
    }

    /*дописать краевые случаи*/
    public double[] getPeresechenieUravn(int first, int second) throws Exception {
        Fraction[][] tmpUr = new Fraction[2][3];
        for (int i = 0; i < 3; i++) {
            tmpUr[0][i] = matrix[first][i].copy();
            tmpUr[1][i] = matrix[second][i].copy();
        }
        Matrix tmpMatrix = new Matrix(tmpUr);
        tmpMatrix.gause();
        if (tmpMatrix.getMatrix().length < 2) return new double[] {-1,-1}; //нет точки пересечения

        return new double[] {tmpUr[0][2].getNum(),tmpUr[1][2].getNum()} ;


        /*if(matrix[first][0].getNum()==0) return -1;
        if(matrix[second][0].getNum()==0) return -1;
        Fraction[] firstUr = new Fraction[3];
        Fraction[] secondUr = new Fraction[3];

        firstUr[0] = new Fraction(1);
        firstUr[1] = matrix[first][1].division(matrix[first][0]);
        firstUr[2] = matrix[first][2].division(matrix[first][0]);

        secondUr[0] = new Fraction(1);
        secondUr[1] = matrix[second][1].division(matrix[second][0]);
        secondUr[2] = matrix[second][2].division(matrix[second][0]);

        if(secondUr[1].minus(firstUr[1]).getNum()==0) return -1;

        Fraction y = (secondUr[2].minus(firstUr[2])).division((secondUr[1].minus(firstUr[1])));
        Fraction x = secondUr[2].minus(secondUr[1].multiply(y));*/
    }
    public double getPeresechenieOsX() throws Exception {
        Fraction[][] tmpUr = new Fraction[2][3];
        for (int i = 0; i < 3; i++) {
            tmpUr[0][i] = matrix[0][i].copy();
        }
        tmpUr[1][0] = new Fraction(0);
        tmpUr[1][1] = new Fraction(1);
        tmpUr[1][2] = new Fraction(0);
        Matrix tmpMatrix = new Matrix(tmpUr);
        tmpMatrix.gause();
        if (tmpMatrix.getMatrix().length < 2) return -1; //нет точки пересечения

        return tmpUr[0][2].getNum();
    }

    public double[] findMaxPereseceniePoint() throws Exception {
        double[] max = {-1,-1};
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix.length; j++) {
                double[] tmp = getPeresechenieUravn(i, j);
                if (tmp[0] > max[0]) max[0] = tmp[0];
                if (tmp[1] > max[1]) max[1] = tmp[1];
            }
        }
        return max;
    }
    /*только для нескольких точек*/
    /*нет обработки случая когда всё больше нуля*/
    public XYChart.Series createSeriesSol(int[] countPeresechenie) throws Exception {
        double value = simplexMethode.getSimplexCore()
                [simplexMethode.getSimplexCore().length-1][simplexMethode.getSimplexCore()[0].length-1].getNum()*-1;
        ObservableList<XYChart.Data> data = FXCollections.observableArrayList();
        XYChart.Series series = new XYChart.Series();
        series.setName("Solution");

        int peresechenie = -1;
        for(int i=0;i<countPeresechenie.length;i++){
            if(countPeresechenie[i]>1){
                peresechenie = i;
                break;
            }
        }
        ArrayList<Integer> xPoints = new ArrayList<>();
        for(int i=0;i<countPeresechenie.length;i++){
            if(countPeresechenie[i]==1){
                xPoints.add((int)getPeresechenieUravn(peresechenie,i)[0]);
            }
        }

        int xMin = xPoints.stream().min(Integer::compareTo).get();
        int xMax = xPoints.stream().max(Integer::compareTo).get();
        for (int i = xMin; i <= xMax; i++) {
            double yValue = 0;
            if (matrix[peresechenie][1].getNum() != 0) {
                yValue = ((matrix[peresechenie][2].minus(matrix[peresechenie][0].multiply(new Fraction(i)))).division(matrix[peresechenie][1])).getNum();
                data.add(new XYChart.Data(i, yValue));

                //System.out.println(Double.valueOf((new XYChart.Data(i,1.782)).getYValue().toString()));
            } else {
                yValue = i;
                data.add(new XYChart.Data(0, i));
            }
        }


        series.setData(data);
        return series;
    }

    public double solutionWithPoints(double x, double y){
        Fraction[] cellF = simplexMethode.createCellFunk();
        double solution = cellF[0].getNum()*x+cellF[1].getNum()*y+cellF[2].getNum();
        return solution;
    }

    public double[] getCords(){
        double[] cords = new double[2];
        ArrayList basis = new ArrayList();
        for(int i=0;i<simplexMethode.getSimplexBasis().length;i++){
            basis.add(simplexMethode.getSimplexBasis()[i]);
        }
        for(int i=0;i<2;i++){
            if(!basis.contains(simplexMethode.getxPos()[i+simplexMethode.getxPos().length-2])){
                cords[i] = 0;
            }
            else{
               cords[i] = simplexMethode.getSimplexCore()[basis.indexOf(simplexMethode.getxPos().length-2)][simplexMethode.getSimplexCore()[0].length-1].getNum();
            }
        }


        return cords;
    }

    //public double getSolutionWithPoints()

    public void backIn() {
        stage.setScene(backScene);
    }

    public void inFirstPage() {
        stage.setScene(firstScene);
    }

    public void createLineChart() throws Exception {
        if(!exSol){
            solDontEx();
            return;
        }

        double max = findMaxPereseceniePoint()[0];
        if(max<0){
            /*if(matrix.length<2){
                max = getPeresechenieOsX();
            }else {*/
            solution.setText("So solution in canonical form (all x >=0)");
            return;
            /*}*/
        }
        int maxX = (int)max+2;

        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        x.setLabel("x"+simplexMethode.getxPos()[simplexMethode.getxPos().length-2]);
        y.setLabel("x"+simplexMethode.getxPos()[simplexMethode.getxPos().length-1]);
        //Fraction[][] matrix = simplexMethode.getMatrix();

        LineChart<Number, Number> numberLineChart = new LineChart<Number, Number>(x, y);

        //AreaChart<Number, Number> numberLineChart = new AreaChart<Number, Number>(x, y);

        ArrayList<XYChart.Series> seriesList = new ArrayList<>();
        ArrayList<ObservableList<XYChart.Data>> obsDataList = new ArrayList<>();

        ObservableList<XYChart.Data> dataSolution = FXCollections.observableArrayList();
        XYChart.Series seriesSolution = new XYChart.Series();
        seriesSolution.setName("Solution");
        int[] countPeresechenie = new int[matrix.length];

        double simplexSolution = simplexMethode.getSimplexCore()
                [simplexMethode.getSimplexCore().length-1][simplexMethode.getSimplexCore()[0].length-1].getNum()*-1;
        for (int i = 0; i < matrix.length; i++) {
            XYChart.Series series = new XYChart.Series();
            series.setName(getStringUravnenie(i));
            seriesList.add(series);
            obsDataList.add(FXCollections.observableArrayList());
        }
        for (int j = 0; j < obsDataList.size(); j++) {
            for (int i = 0; i < maxX; i++) {
                double yValue = 0;
                if(matrix[j][1].getNum()!=0){
                    yValue = ((matrix[j][2].minus(matrix[j][0].multiply(new Fraction(i)))).division(matrix[j][1])).getNum();
                    obsDataList.get(j).add(new XYChart.Data(i,yValue));

                    //System.out.println(Double.valueOf((new XYChart.Data(i,1.782)).getYValue().toString()));
                }else{
                    yValue = i;
                    obsDataList.get(j).add(new XYChart.Data(0,i));
                }


                if(solutionWithPoints(i,yValue)==simplexSolution) {
                    dataSolution.add(new XYChart.Data(i,yValue));
                    countPeresechenie[j] +=1;
                }

            }
            seriesList.get(j).setData(obsDataList.get(j));
        }
        for (XYChart.Series series : seriesList) {
            numberLineChart.getData().add(series);
        }

        //System.out.println(Arrays.toString(getCords()));
        double[] cords = getCords();
        dataSolution.clear();
        dataSolution.add(new XYChart.Data(cords[0],cords[1]));

        seriesSolution.setData(dataSolution);
        if(dataSolution.size()>1){
            seriesSolution = createSeriesSol(countPeresechenie);
        }
        //XYChart.Series seriesSolution = createSeriesSol(obsDataList);

        numberLineChart.getData().add(seriesSolution);


        // numberLineChart.setCreateSymbols(false);
/*        int a = 450;
        int b = 253;

        numberLineChart.setPrefHeight(a);
        numberLineChart.setMinHeight(a);
        numberLineChart.setMaxHeight(a);
        numberLineChart.setMinWidth(b);
        numberLineChart.setMaxWidth(b);
        numberLineChart.setPrefWidth(b);
        numberLineChart.setPrefSize(b, a);
        numberLineChart.setMinSize(b, a);
        numberLineChart.setMaxSize(b, a);*/

        graphicArea.getChildren().add(numberLineChart);
        //graphicArea.getScene().getStylesheets().add("../resources/test.css");
    }

    public Group getGraph(){
        String cssLayout = "-fx-border-color: red;\n" +
                "-fx-border-insets: 5;\n" +
                "-fx-border-width: 3;\n" +
                "-fx-border-style: dashed;\n";
        int HEIGHT = 320; //высота
        int WIDTH = 400; //ширина
        Group box = new Group();
        box.setStyle(cssLayout);
        //box.setAutoSizeChildren(true);

        /*box.setMaxWidth(width);
        box.setMinWidth(width);
        box.setMaxHeight(height);
        box.setMinHeight(height);*/

        box.getChildren().addAll(
                new Line(0, HEIGHT/2, WIDTH, HEIGHT/2),
                new Line(WIDTH/2, 0, WIDTH/2, HEIGHT)
        );

        Operation operation = x -> x[0]*2;

        for(int i = -WIDTH/2; i < WIDTH/2 - 1; i++) {
            box.getChildren().add(
                    new Line(
                            i + WIDTH/2,
                            -operation.execute(i) + HEIGHT/2,
                            i+1 +WIDTH/2,
                            -operation.execute(i+1)+ HEIGHT/2
                    )
            );
        }

        return box;
    }

    @FunctionalInterface
    public interface Operation {
        double execute(double... nums);
    }



}
