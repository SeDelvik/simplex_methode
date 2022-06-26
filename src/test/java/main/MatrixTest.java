package main;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
// TODO: переписать. добавить тесты на удаление нулевых строк

public class MatrixTest {
    int[][] testNums = {{1,2,3,4},{4,5,6,7},{7,8,9,1}};
    int[][] testTranspos = {{1,0,3},{0,0,1},{0,1,2}};
    int[][] testZero = {{1,2,3},{1,2,5},{0,1,1}};
    int[][] transpZero = {{1,1,2},{1,1,3}};

    public Fraction[][] createMatrix(int[][] nums){
        Fraction[][] test = new Fraction[nums.length][nums[0].length];
        for(int i=0;i<test.length;i++){
            for(int j=0;j<test[0].length;j++){
                test[i][j] = new Fraction(nums[i][j]);
            }
        }
        System.out.println("matrix: "+ Arrays.deepToString(test));
        return test;
    }


    @Test
    public void MatrixTest(){
        Fraction[][] tmp = createMatrix(testNums);
        Matrix matrix =new Matrix(tmp);
        matrix.gause();
        assertThat(true).isTrue();
    }
    @Test
    public void MatrixZeroTest(){
        Fraction[][] tmp = createMatrix(testZero);
        Matrix matrix =new Matrix(tmp);
        matrix.gause();
        assertThat(true).isTrue();
    }
    @Test
    public void transposZero(){
        System.out.println("Transp");
        Fraction[][] tmp = createMatrix(transpZero);
        double[][] trueMatrix = {{1,1,2},{0,0,1}};
        Matrix matrix =new Matrix(tmp);
        matrix.gause();

        assertThat(Arrays.deepEquals(matrix.getMatrixDouble(), trueMatrix)).isTrue();
    }
    @Test
    public void transpos(){
        double[][] trueMatrix = {{1,0,3},{0,1,2},{0,0,1}};
        Fraction[][] tmp = createMatrix(testTranspos);
        Matrix matrix =new Matrix(tmp);
        matrix.triangularTransposition();
        assertThat(Arrays.deepEquals(matrix.getMatrixDouble(), trueMatrix)).isTrue();
    }
    @Test
    public void deleteZeroStrings(){
        int[][] test = {{1,2,3},{0,0,0},{3,5,6}};
        Fraction[][] tmp = createMatrix(test);
        double[][] trueMatrix = {{1,2,3},{3,5,6}};
        Matrix matrix =new Matrix(tmp);
        matrix.deleteZeroStrings();
        assertThat(Arrays.deepEquals(matrix.getMatrixDouble(), trueMatrix)).isTrue();
    }
    @Test
    public void deleteZeroStringsAfterGause(){
        int[][] test = {{1,2,3},{1,2,3},{3,5,6}};
        Fraction[][] tmp = createMatrix(test);
        double[][] trueMatrix = {{1,2,3},{3,5,6}};
        Matrix matrix =new Matrix(tmp);
        matrix.gause();
        assertThat(matrix.getMatrix().length==2).isTrue();
    }

    @Test
    public void gause1Standart(){
        int[][] test = {{1,2,3,1},{1,0,4,1},{0,3,0,1}};
        Fraction[][] tmp = createMatrix(test);
        Matrix matrix =new Matrix(tmp);
        matrix.gause();
        System.out.println(Arrays.deepToString(matrix.getMatrix()));
    }
    @Test
    public void gause1ColSwap(){
        int[][] test = {{1,2,3,4,1},{0,0,1,6,1}};
        Fraction[][] tmp = createMatrix(test);
        Matrix matrix =new Matrix(tmp);
        matrix.gause();
        System.out.println(Arrays.deepToString(matrix.getMatrix()));
        System.out.println(Arrays.toString(matrix.getxPos()));
    }
}
