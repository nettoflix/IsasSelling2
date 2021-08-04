package netinho.isasselling.Manager;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.regex.Pattern;

import static java.lang.StrictMath.sqrt;

public class MyMath {

    public static class Pair<T, U> {
        public final T t;
        public final U u;

        public Pair(T t, U u) {
            this.t = t;
            this.u = u;
        }
    }
    public static boolean matricesEqualSize(int m1[][], int m2[][])
    {
        int m1rows = m1.length; int m1cols = m1[0].length; int m2rows = m2.length; int m2cols = m2[0].length;
        if(m1rows == m2rows && m1cols==m2cols) return true;

        return false;
    }
    public static int[][] sumMatrix(int m1[][], int m2[][])
    {
        int mResult[][] = new int[m1.length][m2[0].length];
        int rows = m1.length;
        int coluns = m1[0].length;
        for(int r=0; r<rows; ++r)
        {
            for(int c=0; c<coluns; ++c)
            {
                mResult[r][c] = m1[r][c]+ m2[r][c];
            }
        }
        return mResult;
    }
    public static int[][] subtractMatrix(int m1[][], int m2[][])
    {
        int mResult[][] = new int[m1.length][m2[0].length];
        int rows = m1.length;
        int coluns = m1[0].length;
        for(int r=0; r<rows; ++r)
        {
            for(int c=0; c<coluns; ++c)
            {
                mResult[r][c] = m1[r][c] - m2[r][c];
            }
        }

        return mResult;
    }

    public static int[][] multiplyMatrix(int m1[][], int m2[][])
    {
        int mResult[][] = new int[m1.length][m2[0].length];
        //multiply every row of the m1 for every colun of m2

            for(int r=0; r<m1.length; ++r)
            {
                for(int c2=0; c2<m2[0].length; ++c2)
                {
                    int productBefore=0;
                for (int c=0; c<m1[0].length; ++c)
                {

                    int el1 = m1[r][c];
                    //row of m2 follows colun of m1
                    int el2 = m2[c][c2];
                    int product = el1 * el2;
                    productBefore += product;
                }
                mResult[r][c2] = productBefore;
            }
        }
            for(int mR=0; mR<mResult.length; ++mR)
            {
                for(int mC=0; mC<mResult[0].length; ++mC)
                {
                    Log.d("mRC", String.valueOf(mResult[mR][mC]));
                }
            }

        return mResult;
    }

    public static Pair reduceFraction(int numerator, int denominator) {
        int g;
        g = gcd(numerator, denominator);
        if (g > 1) {
            numerator = numerator / g;
            denominator = denominator / g;
        }
        return new Pair(numerator,denominator);
    }

    public int lcm(int a, int b)
    {
        return (a*b) / gcd(a,b);
    }
    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    public static int convertDpToPixels(int dp, Context context) {
        return dp * ((int) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    public static float converterPixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }
    public static int converterSpToPixels(int px, Context context) {
        int scaledDensity = (int) context.getResources().getDisplayMetrics().scaledDensity;
        return px*scaledDensity;
    }
    public static int gcd(int a, int b)
    {
        if(a==0) return b;
        return gcd(b%a, a);
    }
    public bhaskara solveQuadraticEquation(double a, double b, double c) {
        bhaskara roots = new bhaskara();
        if (a > 0) {
            roots.power_Of_B  = Math.pow(b, 2);
            roots.mult_4ac = 4*a*c;
            roots.delta = roots.power_Of_B  - roots.mult_4ac;
            if(roots.delta>0)
            {
                //2 solutions
                //first one +
                roots.sqrtDelta =sqrt(roots.delta);
                double c1 = ( -b + roots.sqrtDelta);
                double c2 = c1/(2*a);
                roots.x1 = c2;
                //second one -

                double d1 = (-b - roots.sqrtDelta);
                double d2 = d1 / (2 * a);
                roots.x2 = d2;

            }
            else if(roots.delta ==0)
            {
                double c1 = (-b + roots.sqrtDelta);
                double c2 = c1/(2*a);
                roots.x1 = c2;
                roots.singleSolution = true;
            }
            else if(roots.delta <0)
            {
                roots.complexSolution = true;
            }
        }
        else
        {
            roots.noSolution = true;
        }

        return roots;
    }

    public static int solveDt(vec3d vecX, vec3d vecY, vec3d vecZ) {
        int dt;
        //solve for 3x3
        if (vecZ != null) {  //principal diagonal , stretch it
            int main1 = vecX.x * vecY.y * vecZ.z;
            int main2 = vecY.x * vecZ.y * vecX.z;
            int main3 = vecZ.x * vecX.y * vecY.z;
            int mainTotal = main1 + main2 + main3;
            //secundária
            int second1 = vecX.z * vecY.y * vecZ.x;
            int second2 = vecY.z * vecZ.y * vecX.x;
            int second3 = vecZ.z * vecX.y * vecY.x;
            int mainSecond = second1 + second2 + second3;
            dt = mainTotal - mainSecond;
        } else
        //solve for 2x2
        {
            int principalDiagonal = vecX.x * vecY.y;
            int secondDiagonal = vecX.y * vecY.x;
            int total = principalDiagonal - secondDiagonal;
            dt = total;
        }
        return dt;

    }
    private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    //inner Classes
    public static class vec3d {
        int x;
        int y;
        int z;

        public vec3d(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public class vec2d {
        int x;
        int y;

        public vec2d(int x, int y) {
            this.x = x;
            this.y = y;

        }
    }


    public class bhaskara {
        public double power_Of_B;
        public double mult_4ac;
        public  double sqrtDelta;
        public double x1, x2;
        public  double delta;
        public boolean noSolution;
        public boolean complexSolution;
        public boolean singleSolution;

        public void bhaskara(double x1, double x2) {
            this.x1 = x1;
            this.x2 = x2;
            this.complexSolution = false;
            this.noSolution = false;
            this.singleSolution = false;
        }
    }
    public static String[] makeArraySequence(int length) {
        String[] array = new String[length];
        for(int i=0; i<length; ++i)
        {
                array[i] = String.valueOf(i+1);

        }
        return array;
    }
}


