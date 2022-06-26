package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Matrix {
    private Fraction[][] matrix;
    private int[] xPos;

    public Matrix(Fraction[][] matrix) {
        this.matrix = matrix;
        xPos = new int[matrix[0].length - 1];
        for (int i = 0; i < matrix[0].length - 1; i++) {
            xPos[i] = i;
        }
    }

    // TODO: протестировать!!!
    public Matrix(Fraction[][] matrix, int[] Basis) {
        this.matrix = matrix;
        xPos = new int[matrix[0].length - 1];
        for (int i = 0; i < matrix[0].length - 1; i++) {
            xPos[i] = i;
        }
        ArrayList<Integer> tmpArrayList = new ArrayList<>();
        for (int xPosBasis : Basis) {
            tmpArrayList.add(xPosBasis);
        }

        /*перестановка колонок*/
        Fraction[][] tmpMatrix = new Fraction[matrix.length][matrix[0].length];
        int[] tmpXPos = new int[xPos.length];
        for (int i = 0; i < tmpArrayList.size(); i++) {
            for(int j=0;j< matrix.length;j++){
                tmpMatrix[j][i] = matrix[j][tmpArrayList.get(i)].copy();
            }
            tmpXPos[i] = tmpArrayList.get(i);
        }
        int x = 0;
        for(int i = 0;i<matrix[0].length;i++){

            if(!tmpArrayList.contains(i)){
                for(int j = 0;j< matrix.length;j++){
                    tmpMatrix[j][tmpArrayList.size()+x] = matrix[j][i].copy();
                }
                if(i!= matrix[0].length-1) tmpXPos[tmpArrayList.size()+x] = i;
                x++;

            }

        }
        this.xPos = tmpXPos;
        this.matrix = tmpMatrix;


       /* while (tmpArrayList.size() > 0) {
            for (int j = 0; j < matrix[0].length - 1; j++) {
                if (!tmpArrayList.contains(j) && tmpArrayList.size() > 0) {
                    swapCol(tmpArrayList.remove(0), j);
                }
            }
        }*/
    }

    public Matrix(Fraction[][] matrix, int[] xPos, int i) {
        this.matrix = matrix.clone();
        this.xPos = xPos.clone();
    }

    public Fraction[][] getMatrix() {
        return this.matrix;
    }

    public double[][] getMatrixDouble() {
        double[][] retMatrix = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                retMatrix[i][j] = matrix[i][j].getNum();
            }
        }

        return retMatrix;
    }

    public int[] getxPos() {
        return xPos;
    }

/*    public void gause() {
        for (int i = 0; i < matrix.length; i++) {
            triangularTransposition();
            Fraction delFirst = matrix[i][i].copy();
            if (delFirst.getNumerator() == 0) continue;
            for (int j = i; j < matrix[0].length; j++) {
                matrix[i][j] = matrix[i][j].division(delFirst);
            }
            for (int j = 0; j < matrix.length; j++) {
                //if(matrix[j][i].getNumerator()==0) break;
                if (j == i) continue;
                Fraction del = matrix[j][i].copy();

                for (int k = i; k < matrix[0].length; k++) {
                    Fraction tmp = matrix[i][k].multiply(del);
                    matrix[j][k] = matrix[j][k].minus(tmp);
                }
            }
        }
        deleteZeroStrings();

    }*/

    /**
     * тестовый вариант гауса
     */
    public void gause() throws Exception {
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][i].getNum() == 0) { //попытка найти ненулевой элемент для гауса. если не удается скорее всего гаус окончен
                int rowPos = findNotNulRow(i);
                if (rowPos < 0) {
                    int colPos = findNotNulCol(i, i);
                    if (colPos < 0) break;
                    swapCol(i, colPos);
                } else {
                    swapRow(i, rowPos);
                }
            }

            Fraction delFirst = matrix[i][i].copy();
            for (int j = i; j < matrix[0].length; j++) {
                matrix[i][j] = matrix[i][j].division(delFirst);
            }
            for (int j = 0; j < matrix.length; j++) {
                if (j == i) continue;
                Fraction del = matrix[j][i].copy();

                for (int k = i; k < matrix[0].length; k++) {
                    Fraction tmp = matrix[i][k].multiply(del);
                    matrix[j][k] = matrix[j][k].minus(tmp);
                }
            }
        }
        deleteZeroStrings();
    }

    public void deleteZeroStrings() throws Exception {
        for (int i = 0; i < matrix.length; i++){
            boolean isErr = true;
            for (int j = 0; j < matrix[0].length-1; j++){
                if (matrix[i][j].getNum() != 0)
                    isErr = false;
            }
            if(isErr && matrix[i][matrix[0].length-1].getNum() != 0){
                throw new Exception("it can't be solved");
            }
        }
        ArrayList<Integer> zeroStrings = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            if (Arrays.stream(matrix[i]).filter(new Fraction(0)::equals).count() == matrix[0].length) {
                zeroStrings.add(i);
            }
        }
        Fraction[][] tmpMatrix = new Fraction[matrix.length - zeroStrings.size()][matrix[0].length];
        int t = 0;
        for (int i = 0; i < matrix.length; i++) {
            if (!zeroStrings.contains(i)) {
                tmpMatrix[t] = matrix[i];
                t++;
            }
        }
        matrix = tmpMatrix;
    }

    /**
     * ищет строку с ненулевым элементом в нужном столбце
     */
    public int findNotNulRow(int colPos) {
        int rowPos = -1;
        for (int i = 0; i < matrix.length; i++) {
            int firstNotNul = 0;
            for (int j = 0; j < colPos; j++) {
                if (matrix[i][j].getNum() != 0) firstNotNul = 1;
            }
            if (firstNotNul == 0 && matrix[i][colPos].getNum() != 0) {
                rowPos = i;
                break;
            }
        }
        return rowPos;
    }

    /**
     * ищет столбец с ненулевым элементом в нужном столбце
     */
    public int findNotNulCol(int colPosStart, int rowPos) {
        int colPos = -1;
        for (int i = colPosStart; i < matrix[0].length - 1; i++) {
            if (matrix[rowPos][i].getNum() != 0) {
                colPos = i;
                break;
            }
        }
        return colPos;
    }

    /**
     * меняет строки местами
     */
    public void swapRow(int row1, int row2) {
        Fraction[] tmpRow1 = matrix[row1].clone();
        Fraction[] tmpRow2 = matrix[row2].clone();
        matrix[row1] = tmpRow2;
        matrix[row2] = tmpRow1;
    }

    /**
     * меняет столбцы местами с переписыванием индексов
     */
    public void swapCol(int col1, int col2) {
        for (int i = 0; i < matrix.length; i++) {
            Fraction tmp1 = matrix[i][col1].copy();
            Fraction tmp2 = matrix[i][col2].copy();
            matrix[i][col1] = tmp2;
            matrix[i][col2] = tmp1;
        }
        int tmp1 = xPos[col1];
        int tmp2 = xPos[col2];
        xPos[col1] = tmp2;
        xPos[col2] = tmp1;

    }

    public void triangularTransposition() {
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][i].getNumerator() == 0) {


                Fraction[] line1 = new Fraction[matrix[0].length];
                Fraction[] line2 = new Fraction[matrix[0].length];
                int notNulPos = -1;
                for (int j = 0; j < matrix.length; j++) {
                    /**/
                    if (matrix[j][i].getNumerator() == 0) continue;
                    int flag = 0;
                    for (int k = 0; k < j; k++) {
                        if (matrix[j][k].getNumerator() != 0) {
                            flag = 1;
                            break;
                        }
                    }
                    if (flag != 0) notNulPos = j;

                    /**/
                    //if(matrix[j][i].getNumerator()!=0) notNulPos = j;
                }
                if (notNulPos == -1) continue;

                for (int j = 0; j < matrix[0].length; j++) {
                    line1[j] = matrix[i][j];
                    line2[j] = matrix[notNulPos][j];
                }
                matrix[i] = line2;
                matrix[notNulPos] = line1;

            }
        }
    }

//    public boolean
}
