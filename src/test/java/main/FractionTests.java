package main;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

// TODO: переписать в нормальный вид
public class FractionTests {
    @Test
    public void simplifyFractionTest() {
        Fraction fraction = new Fraction(6, 3);
        fraction.simplify();
        assertThat(fraction.getNumerator() == 2 && fraction.getDenominator() == 1).isTrue();
    }

    @Test
    public void simplifyFractionOtricTest() {
        Fraction fraction = new Fraction(-1, 2);
        fraction.simplify();
        assertThat(fraction.getNumerator() == -1 && fraction.getDenominator() == 2).isTrue();
    }

    @Test
    public void sumTest() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sumFraction = fraction1.sum(fraction2);
        assertThat(sumFraction.getNumerator() == 5 && sumFraction.getDenominator() == 6).isTrue();
    }

    @Test
    public void sumOtricateln() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(-1, 3);
        Fraction sumFraction = fraction1.sum(fraction2);
        assertThat(sumFraction.getNumerator() == 1 && sumFraction.getDenominator() == 6).isTrue();
    }

    @Test
    public void multiplyTest() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);
        Fraction sumFraction = fraction1.multiply(fraction2);
        assertThat(sumFraction.getNumerator() == 1 && sumFraction.getDenominator() == 6).isTrue();
    }

    @Test
    public void multiplyOtricTest() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(-1, 3);
        Fraction sumFraction = fraction1.multiply(fraction2);
        assertThat(sumFraction.getNumerator() == -1 && sumFraction.getDenominator() == 6).isTrue();
    }

    @Test
    public void minus() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 3);

        Fraction sumFraction = fraction1.minus(fraction2);
        assertThat(sumFraction.getNumerator() == 1 && sumFraction.getDenominator() == 6).isTrue();
    }
    @Test
    public void minusZero() {
        Fraction fraction1 = new Fraction(3);
        Fraction fraction2 = new Fraction(3);

        Fraction sumFraction = fraction1.minus(fraction2);
        assertThat(sumFraction.getNumerator() == 0 && sumFraction.getDenominator() == 1).isTrue();
    }
    @Test
    public void sumMinusZero() {
        Fraction fraction1 = new Fraction(3);
        Fraction fraction2 = new Fraction(-3);

        Fraction sumFraction = fraction1.sum(fraction2);
        assertThat(sumFraction.getNumerator() == 0 && sumFraction.getDenominator() == 1).isTrue();
    }
    @Test
    public void divisionOne(){
        Fraction fraction1 = new Fraction(-1,2);
        Fraction fraction2 = new Fraction(-1,2);

        Fraction sumFraction = fraction1.division(fraction2);
        assertThat(sumFraction.getNumerator() == 1 && sumFraction.getDenominator() == 1).isTrue();
    }

    @Test
    public void myltiplyByZero(){
        Fraction fraction1 = new Fraction(-1,2);
        Fraction fraction2 = new Fraction(0,1);

        Fraction sumFraction = fraction1.multiply(fraction2);
        assertThat(sumFraction.getNumerator() == 0).isTrue();
    }

    @Test
    public void test1(){
        Fraction fraction = new Fraction(1).division(new Fraction(1,3));
        assertThat(fraction.getNumerator()==3&&fraction.getDenominator()==1).isTrue();
    }


}
