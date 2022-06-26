package main;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

public class SimplexMethodeTest {
    /**
     * TODO: проверить на правильность создания
     */

    public Fraction[][] createMatrix(int[][] nums) {
        Fraction[][] test = new Fraction[nums.length][nums[0].length];
        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < test[0].length; j++) {
                test[i][j] = new Fraction(nums[i][j]);
            }
        }
        return test;
    }

    public Fraction[] createCellFuncton(int[] nums) {
        Fraction[] testCellF = new Fraction[nums.length];
        for (int i = 0; i < nums.length; i++) {
            testCellF[i] = new Fraction(nums[i]);
        }

        return testCellF;
    }

    @Test
    public void createTable() {
        int[][] testMatrix = {{1, 0, 0, 2, 1, 8}, {0, 1, 0, 7, 8, 9}, {0, 0, 1, 3, 16, 1}};
        int[] testCell = {1, 2, 1, 1, 1, 0};
        SimplexMethode simplexMethode = new SimplexMethode(createMatrix(testMatrix), createCellFuncton(testCell));
        simplexMethode.prepareSimplexMatrix();

        System.out.println(simplexMethode);
    }


    @Test
    public void simplexStep(){
        int[][] testMatrix = { {1,2,5,-1,4}, {1,-1,-1,2,1}};
        int[] testCell = {-2,-1,3,1, 0};
        int[] basis = {2,3};
        SimplexMethode simplexMethode = new SimplexMethode(createMatrix(testMatrix),createCellFuncton(testCell),basis);
        SimplexMethode newStep = simplexMethode.oneStep(-1,-1);
        SimplexMethode newnewStep = newStep.oneStep(-1,-1);
        //System.out.println(simplexMethode);
        System.out.println(newnewStep);
    }

}
