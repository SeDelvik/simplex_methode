package main;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class Fraction {
    private int numerator; //числитель
    private int denominator; //знаменатель
    private static boolean simpleFraction = true;

    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Fraction(int num) {
        this.numerator = num;
        this.denominator = 1;
    }

    public double getNum() {
        return (double) numerator / denominator;
    }

    public Fraction sum(Fraction fraction) {
        Fraction newFraction = new Fraction(this.numerator * fraction.getDenominator() + fraction.getNumerator() * this.denominator,
                this.denominator * fraction.getDenominator());
        if (newFraction.getNumerator() != 0)
            newFraction.simplify();
        return newFraction;
    }

    public Fraction minus(Fraction fraction) {
        Fraction newFraction = new Fraction(fraction.getNumerator() * -1, fraction.getDenominator());
        return sum(newFraction);
    }

    public Fraction multiply(Fraction fraction) {
        Fraction newFraction = new Fraction(this.numerator * fraction.getNumerator(),
                this.denominator * fraction.getDenominator());
        if (newFraction.getNumerator() != 0)
            newFraction.simplify();
        return newFraction;
    }

    public Fraction division(Fraction fraction) {
        return multiply(new Fraction(fraction.getDenominator(), fraction.getNumerator()));
    }

    public void simplify() {
        int tmpNumerator = Math.abs(this.numerator);
        int tmpDenominator = Math.abs(this.denominator);
        ArrayList<Integer> numeratorList = new ArrayList<>();
        ArrayList<Integer> denominatorList = new ArrayList<>();
        while (tmpNumerator > 1) {
            for (int i = 2; i <= tmpNumerator; i++) {
                if (tmpNumerator % i == 0) {
                    numeratorList.add(i);
                    tmpNumerator = tmpNumerator / i;
                    break;
                }
            }
        }
        while (tmpDenominator > 1) {
            for (int i = 2; i <= tmpDenominator; i++) {
                if (tmpDenominator % i == 0) {
                    denominatorList.add(i);
                    tmpDenominator = tmpDenominator / i;
                    break;
                }
            }
        }
        //System.out.println(numeratorList);
        //System.out.println(denominatorList);
        ArrayList<Integer> numeratorListFinal = new ArrayList<>();
        numeratorListFinal.add(1);
        for (int element : numeratorList) {
            if (denominatorList.contains(element)) {
                denominatorList.remove(new Integer(element));
            } else {
                numeratorListFinal.add(element);
            }
        }
        //if(denominatorList.size()<1) denominatorList.add(1);
        int numeratorFinal = 1;
        int denominatorFinal = 1;
        for (int element : numeratorListFinal) {
            numeratorFinal = element * numeratorFinal;
        }
        for (int element : denominatorList) {
            denominatorFinal = denominatorFinal * element;
        }

        if ((this.numerator < 0 && this.denominator >= 0) || (this.numerator >= 0 && this.denominator < 0))
            numeratorFinal = numeratorFinal * -1;

        this.numerator = numeratorFinal;
        this.denominator = denominatorFinal;


    }

    public int getNumerator() {
        return this.numerator;
    }

    public int getDenominator() {
        return this.denominator;
    }

    public Fraction copy() {
        return new Fraction(this.numerator, this.denominator);
    }

    public static boolean getSimpleFraction(){
        return simpleFraction;
    }

    public static void setSimpleFraction(boolean bol){
        simpleFraction = bol;
    }

    @Override
    public String toString() {
        String str = "";
        if (denominator == 1 || numerator == 0) {
            str += numerator;
        } else if(simpleFraction){
            str += numerator + "/" + denominator;
        }else{
            str += getNum();
        }
        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fraction fraction = (Fraction) o;
        return getNum() == fraction.getNum()/*numerator == fraction.numerator &&
                denominator == fraction.denominator*/;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }
}
