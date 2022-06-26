package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SimplexMethode extends Matrix {
    private Fraction[] cellFunction;
    private Fraction[][] simplexCore;
    private int[] simplexBasis;
    private int[] simplexFreeVariables;
    private boolean isArtificiallyBasis = false;

    public SimplexMethode(Fraction[][] matrix, Fraction[] cellFunction) throws Exception {
        super(matrix);
        this.cellFunction = cellFunction;
        simplexBasis = new int[0];
        prepareSimplexMatrix();
    }

    public SimplexMethode(Fraction[][] matrix, Fraction[] cellFunction, int[] simplexBasis) throws Exception { //с указанием базиса
        super(matrix, simplexBasis);
        this.cellFunction = swapCellFunk(cellFunction, simplexBasis);
        this.simplexBasis = simplexBasis;
        prepareSimplexMatrix();
    }

    private SimplexMethode(Fraction[][] matrix, int[] xPos, Fraction[] cellFunction,
                           Fraction[][] simplexCore,
                           int[] simplexBasis,
                           int[] simplexFreeVariables,boolean isArtificiallyBasis) {
        super(matrix.clone(), xPos.clone(), 1);
        this.cellFunction = cellFunction.clone();
        this.simplexCore = simplexCore.clone();
        this.simplexBasis = simplexBasis.clone();
        this.simplexFreeVariables = simplexFreeVariables.clone();
        this.isArtificiallyBasis = isArtificiallyBasis;

    }

//    искусственный базис

    public SimplexMethode(Fraction[][] matrix, Fraction[] cellFunction, boolean art) {
        super(matrix);
        this.cellFunction = cellFunction;
        simplexBasis = new int[getMatrix().length];
        simplexFreeVariables = new int[getMatrix()[0].length - 1];
        simplexCore = new Fraction[matrix.length+1][matrix[0].length];
        this.isArtificiallyBasis = true;
        prepareArtisticallyBasis();
    }

    public void prepareArtisticallyBasis() {
        for (int i = 0; i < simplexFreeVariables.length; i++) {
            simplexFreeVariables[i] = i;
        }
        for (int i = 0; i < simplexBasis.length; i++) {
            simplexBasis[i] = i + simplexFreeVariables.length;
        }

        for (int i = 0; i < getMatrix().length; i++) {
            for (int j = 0; j < getMatrix()[0].length; j++) {
                simplexCore[i][j] = getMatrix()[i][j].copy();
            }
        }
        Fraction[] artCellF = getCellArtificalBasisFunk();
        simplexCore[getMatrix().length] = artCellF;
    }

    public Fraction[] getCellArtificalBasisFunk() {
        Fraction[] artCell = new Fraction[getMatrix()[0].length];
        for (int j = 0; j < getMatrix()[0].length; j++) {
            Fraction fraction = new Fraction(0);
            for (int i = 0; i < getMatrix().length; i++) {
                fraction = fraction.sum(getMatrix()[i][j]);
            }
            fraction = fraction.multiply(new Fraction(-1));
            artCell[j] = fraction;
        }
        return artCell;
    }

    public int[] findArtificallyBasisPosition() {
        int[] pos = new int[2];
        pos[0] = -1;
        pos[1] = -1;

        for (int i = 0; i < simplexCore.length - 1; i++) {
            if (simplexBasis[i] >= getMatrix()[0].length-1) {
                for (int j = 0; j < simplexCore[0].length - 1; j++) {
                    if (isMinimalInCol(i, j)) {
                        pos[0] = i;
                        pos[1] = j;
                        return pos;
                    }
                }
            }
        }

        if (pos[0] < 0) pos = findPos(-1, -1);
        return pos;
    }

    public SimplexMethode artifficallyBasisNextStep(int row,int col){
        int[] pos = new int[2];
        pos[0] = row;
        pos[1] = col;
        if(row<0) pos = findArtificallyBasisPosition();
        SimplexMethode simplexMethode = oneStep(pos[0],pos[1]);
        for(int i = 0;i<simplexMethode.getSimplexFreeVariables().length;i++){
            if(simplexMethode.getSimplexFreeVariables()[i]>=simplexMethode.getMatrix()[0].length-1){
                simplexMethode.deleteCol(i);
            }
        }
        simplexMethode.setArtBasis(true);
        return simplexMethode;

    }
    public void setArtBasis(boolean sost){
        this.isArtificiallyBasis = sost;
    }

    public void deleteCol(int pos){
        Fraction[][] tmpCore = new Fraction[getSimplexCore().length][getSimplexCore()[0].length-1];
        int[] tmpFreeVars = new int[simplexFreeVariables.length-1];
        int x=0;
        for(int i=0;i< simplexCore.length;i++){
            for(int j=0;j<simplexCore[0].length;j++){
                if(j==pos) continue;
                tmpCore[i][x] = simplexCore[i][j];
                //tmpFreeVars[x] = simplexFreeVariables[j];
                x++;
            }
            x=0;
        }

        x=0;
        for(int i=0;i< simplexFreeVariables.length;i++){
            if(i==pos) continue;
            tmpFreeVars[x] = simplexFreeVariables[i];
            x++;
        }
        simplexFreeVariables = tmpFreeVars;
        simplexCore = tmpCore;

    }

    public boolean isArtificiallyBasisDone() {
        int count = 0;
        for (int i = 0; i < simplexBasis.length; i++) {
            if (simplexBasis[i] >= cellFunction.length-1) count++;
        }
        if (count > 0) return false;
        return true;
    }

    public SimplexMethode switchToNormalSimplexMethode(){
        Fraction[] newCellFunk = new Fraction[cellFunction.length-simplexBasis.length];
        for(int i =0;i<simplexFreeVariables.length;i++){
            newCellFunk[i] = cellFunction[simplexFreeVariables[i]].copy();
        }
        newCellFunk[newCellFunk.length-1] = cellFunction[cellFunction.length-1].copy();

        for(int i=0;i<simplexBasis.length;i++){
            for(int j=0;j<simplexFreeVariables.length;j++){
                newCellFunk[j] = newCellFunk[j].sum(simplexCore[i][j].multiply(new Fraction(-1)).multiply(cellFunction[simplexBasis[i]]));
            }
            newCellFunk[newCellFunk.length-1] = newCellFunk[newCellFunk.length-1].sum(simplexCore[i][simplexCore[0].length-1].multiply(cellFunction[simplexBasis[i]]));
        }
        newCellFunk[newCellFunk.length-1] = newCellFunk[newCellFunk.length-1].multiply(new Fraction(-1));
        //simplexCore[simplexCore.length-1] = newCellFunk;
        //isArtificiallyBasis = false;

        Fraction[][] newCore = new Fraction[simplexCore.length][simplexCore[0].length];
        for(int i = 0;i<simplexCore.length;i++){
            for(int j=0;j<simplexCore[0].length;j++){
                newCore[i][j] = simplexCore[i][j].copy();
            }
        }
        newCore[simplexCore.length-1] = newCellFunk;
        return new SimplexMethode(getMatrix(), getxPos(), cellFunction, newCore, simplexBasis, simplexFreeVariables,false);
    }

    public boolean isArtBasis(){
        return isArtificiallyBasis;
    }

//    искусственный базис


    public void prepareSimplexMatrix() throws Exception {

        createSimplexBasisAndFreeVars();
        Fraction[] cellFunctionAfterPreparation = createCellFunk();
        simplexCore = new Fraction[simplexBasis.length + 1][simplexFreeVariables.length + 1];
        for (int i = 0; i < simplexBasis.length; i++) {
            for (int j = 0; j <= simplexFreeVariables.length; j++) {
                simplexCore[i][j] = getMatrix()[i][simplexBasis.length + j].copy();
            }
        }
        ArrayList<Fraction> tmp = new ArrayList<>();
        for (Fraction fraction : cellFunctionAfterPreparation) {
            tmp.add(fraction.copy());
        }
        for (int i = 0; i <= simplexFreeVariables.length; i++) {
            if (i == simplexFreeVariables.length) {
                simplexCore[simplexCore.length - 1][i] = tmp.get(i).multiply(new Fraction(-1));
            } else {
                simplexCore[simplexCore.length - 1][i] = tmp.get(i);
            }
        }
    }

    /**
     * Проблемы:
     * может не найти элемент
     * если вся нижняя
     */
    public SimplexMethode oneStep(int row, int col) {
        int[] positions = findPos(row, col);
        //if(isDone()) return;
        int x = positions[0];
        int y = positions[1];
        int[] newBasis = simplexBasis.clone();
        int[] newFreeVars = simplexFreeVariables.clone();
        Fraction[][] newCore = new Fraction[simplexCore.length][simplexCore[0].length];

        newBasis[x] = simplexFreeVariables[y];
        newFreeVars[y] = simplexBasis[x];
        Fraction mainElement = new Fraction(1).division(simplexCore[x][y]);
        newCore[x][y] = mainElement;
        /*строка*/
        for (int i = 0; i < simplexCore[0].length; i++) {
            if (i == y) continue;
            newCore[x][i] = simplexCore[x][i].multiply(mainElement);
        }
        /*столбец*/
        for (int i = 0; i < simplexCore.length; i++) {
            if (i == x) continue;
            newCore[i][y] = simplexCore[i][y].multiply(new Fraction(-1)).multiply(mainElement);
        }

        for (int i = 0; i < simplexCore.length; i++) {
            if (i == x) continue;
            Fraction tmpMultiply = simplexCore[i][y];
            for (int j = 0; j < simplexCore[0].length; j++) {
                if (j == y) continue;
                newCore[i][j] = simplexCore[i][j].minus(simplexCore[i][y].multiply(newCore[x][j]));
            }
        }
        return new SimplexMethode(getMatrix(), getxPos(), cellFunction, newCore, newBasis, newFreeVars,this.isArtificiallyBasis);

    }

    public boolean isDone() {
        int[] positions = findPos(-1, -1);
        if (positions[0] == -1) return true;
        else return false;
    }

    public void createSimplexBasisAndFreeVars() throws Exception {
        gause();
        simplexBasis = new int[getMatrix().length];
        for (int i = 0; i < getMatrix().length; i++) {
            simplexBasis[i] = getxPos()[i];
        }

        simplexFreeVariables = new int[getMatrix()[0].length - 1 - getMatrix().length];
        int x = 0;
        for (int i = getMatrix().length; i < getMatrix()[0].length - 1; i++) {
            simplexFreeVariables[x] = getxPos()[i];
            x++;
        }
        /*если с заданным базисом не сойдется я не знаю че делать*/
    }

    public Fraction[] swapCellFunk(Fraction[] cellFunction, int[] basis) {
        Fraction[] newCellFunk = new Fraction[cellFunction.length];
        ArrayList<Integer> tmpArray = new ArrayList();
        ArrayList<Fraction> tmpCellArray = new ArrayList();
        for (int i = 0; i < basis.length; i++) {
            tmpArray.add(basis[i]);
            tmpCellArray.add(cellFunction[basis[i]]);
        }
        for (int i = 0; i < cellFunction.length; i++) {
            if (!tmpArray.contains(i)) {
                tmpCellArray.add(cellFunction[i]);
            }
        }
        for (int i = 0; i < cellFunction.length; i++) {
            newCellFunk[i] = tmpCellArray.get(i);
        }

        return newCellFunk;
    }

    public Fraction[] createCellFunk() {
        /*создание новой целевой функции для последней строки*/
        Fraction[] cellFunctionAfterPreparation = new Fraction[getMatrix()[0].length - getMatrix().length];
        for (int i = 0; i < cellFunctionAfterPreparation.length; i++) {
            cellFunctionAfterPreparation[i] = cellFunction[i + getMatrix().length].copy();
        }
        /*попытка сложить */
        for (int i = 0; i < getMatrix().length; i++) {
            for (int j = getMatrix().length; j < getMatrix()[0].length; j++) {
                int minus = -1;
                if (j == getMatrix()[0].length - 1) minus = 1;
                cellFunctionAfterPreparation[j - getMatrix().length] = cellFunctionAfterPreparation[j - getMatrix().length].sum(
                        getMatrix()[i][j].multiply(new Fraction(minus)).multiply(cellFunction[i])
                );

            }
        }
        return cellFunctionAfterPreparation;
    }


    public int[] findPos(int row, int col) {

        int[] retArr = new int[2];
        if (row > -1 && col > -1) {
            retArr[0] = row;
            retArr[1] = col;
            return retArr;
        }
        retArr[0] = -1;
        retArr[1] = -1;

        for (int i = 0; i < simplexCore[0].length - 1; i++) {
            if (simplexCore[simplexCore.length - 1][i].getNum() < 0) {
                for (int j = 0; j < simplexCore.length - 1; j++) {
                    if (simplexCore[j][i].getNum() > 0) {
                        if (retArr[0] < 0 && retArr[1] < 0) {
                            retArr[0] = j;
                            retArr[1] = i;
                        } else if (new Fraction(1).division(simplexCore[j][i]).getNum() < new Fraction(1).division(simplexCore[retArr[0]][retArr[1]]).getNum()) {
                            retArr[0] = j;
                            retArr[1] = i;
                        }


                    }
                }
                if (retArr[0] > -1 && retArr[1] > -1) {
                    return retArr;
                }
            }
        }

        return retArr;
    }

    public boolean isMinimalInCol(int row, int col) {
        for (int i = 0; i < simplexCore.length - 1; i++) {
            if(simplexCore[row][col].getNum()<=0) return false;
            if(simplexCore[i][col].getNum()<=0) continue;
            if (1 / simplexCore[i][col].getNum() < 1 / simplexCore[row][col].getNum()) return false;
        }
        if (simplexCore[simplexCore.length - 1][col].getNum() < 0) return true;
        return false;

    }

    public int[] getSimplexBasis() {
        return simplexBasis;
    }

    public int[] getSimplexFreeVariables() {
        return simplexFreeVariables;
    }

    public Fraction[][] getSimplexCore() {
        return simplexCore;
    }
    public  Fraction[] getCellFunction(){
        return cellFunction;
    }


    @Override
    public String toString() {
        String string = "";
        string += "Basis: " + Arrays.toString(simplexBasis) + "\n";
        string += "Free Variables " + Arrays.toString(simplexFreeVariables) + "\n";
        string += "Core table " + Arrays.deepToString(simplexCore) + "\n";

        return string;
    }


}
