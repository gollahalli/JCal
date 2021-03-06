/*
 * Copyright (c) 2015 Akshay Raj Gollahalli
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.gollahalli.api;


import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class return all the calculations for mortgages.
 */
public class Calculate {

    public static final Logger logger = LogManager.getLogger(Calculate.class);

    public double percentage(double rate) {
        return rate / 100;
    }

    /**
     * This function calculates simple interest as return on investment.
     * <p>
     * To use this function, do the following:
     * <p>
     * <pre>
     *     {@code
     *     Calculate calculate = new Calculate();
     *     double[] simple = calculate.simpleInterest(10000, 5, 5);
     *     System.out.println(simple[0]); // Simple interest
     *     System.out.println(simple[1]); // Interest earned
     *     }
     * </pre>
     *
     * @param principal Loan amount.
     * @param rate      Anual rate of interest.
     * @param duration  Duration in years.
     * @return double[]         An double array is returned.
     */
    public double[] simpleInterest(double principal, double rate, double duration) {
        logger.info("simple interest called with principal = " + principal + ", rate = " + rate + " and duration = " + duration);
        double[] total = new double[2];
        if (rate == 0) {
            logger.error("Rate cannot be 0");
        }
        double percentRate = percentage(rate);
        total[0] = principal * percentRate * duration;
        total[1] = total[0] - principal;
        return total;
    }

    /**
     * This function returns the return on investment compounded over years.
     * <p>
     * To use this code do the following:
     * <p>
     * <pre>
     *     {@code
     *       Calculate calculate = new Calculate();
     *       double[][] yearlyOutput = calculate.compoundInterest(10000, 5, 5, 12);
     *
     *       for (int i = 1; i < yearlyOutput[0].length; i++) {
     *           System.out.println(yearlyOutput[0][i]); // number of years
     *           System.out.println(yearlyOutput[1][i]); // principal amount over the years
     *       }
     *     }
     * </pre>
     *
     * @param principal  loan amount.
     * @param rate       Annual rate of interest.
     * @param duration   Duration in years.
     * @param compounded Compounded for.
     * @return double[][] A double array is returned.
     */
    public double[][] compoundInterest(double principal, double rate, double duration, double compounded) {
        logger.info("compound interest called with principal = " + principal + ", rate = " + rate + ", duration = " + duration +
                " and compounded for " + compounded);
        int durationInt = (int) duration;
        double[][] total = new double[2][durationInt + 1];
        double percentRate = percentage(rate);
        BigDecimal bd;
        BigDecimal bd1;
        for (int i = 0; i < duration + 1; i++) {
            total[0][i] = principal * Math.pow(1 + (percentRate / compounded), compounded * i);
            bd = new BigDecimal(total[0][i]).setScale(2, RoundingMode.HALF_DOWN);
            total[0][i] = bd.doubleValue();
            total[1][i] = total[0][i] - principal;
            bd1 = new BigDecimal(total[1][i]).setScale(2, RoundingMode.HALF_DOWN);
            total[1][i] = bd1.doubleValue();
        }
        return total;
    }

    /**
     * This function gives you the monthly mortgage payments.
     * <p>
     * To use this function do the following:
     * <p>
     * <pre>
     *     {@code
     *     Calculate calculate = New Calculate();
     *     double monthlyOutput = calculate.fixedRateMortgageMonthly(10000, 5*12, 5);
     *     System.out.println(monthlyOutput);
     *     }
     * </pre>
     *
     * @param loanAmount   Loan amount required.
     * @param termInMonths Term in months.
     * @param interestRate Rate of interest per annam.
     * @return double           Returns a double number.
     */
    public double fixedRateMortgageMonthly(double loanAmount, double termInMonths, double interestRate) {
        BigDecimal bd;
        double initalInterestRate = interestRate / 100;
        double monthlyRate = initalInterestRate / 12.0;
        bd = new BigDecimal((loanAmount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -termInMonths))).setScale(2, RoundingMode.HALF_DOWN);
        return bd.doubleValue();
    }

    public double fixedRateMortgageWeekly(double loanAmount, double termInMonths, double interestRate) {
        BigDecimal bd;
        double initalInterestRate = interestRate / 100;
        double monthlyRate = initalInterestRate / 52.0;
        bd = new BigDecimal((loanAmount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -termInMonths))).setScale(2, RoundingMode.HALF_DOWN);
        return bd.doubleValue();
    }

    public double fixedRateMortgageFortnightly(double loanAmount, double termInMonths, double interestRate) {
        BigDecimal bd;
        double initalInterestRate = interestRate / 100;
        double monthlyRate = initalInterestRate / 26.0;
        bd = new BigDecimal((loanAmount * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -termInMonths))).setScale(2, RoundingMode.HALF_DOWN);
        return bd.doubleValue();
    }

    /**
     * This function returns interest, principal and balance.
     * <p>
     * To use this function, do the following:
     * <p>
     * <pre>
     *     {@code
     *       Calculate calculate = new Calculate();
     *
     *       double[][] monthlyChart = calculate.fixedRateMortgageMonthlyChart(loanAmountText, interestText, yearsTextMonth + monthsText);
     *       System.out.println(monthlyChart[2][0]); // principal over a month
     *       System.out.println(monthlyChart[1][0]); // interest over a month
     *       System.out.println(monthlyChart[3][0]); // balance
     *       System.out.println(monthlyChart[4][0]); // Annual interest converted to one month, this will not change
     *
     *       for (int i = 1; i < yearsTextMonth + monthsText; i++) {
     *           PaymentsTable paymentsTable = new PaymentsTable();
     *           monthlyChart = calculate.fixedRateMortgageMonthlyChart(monthlyChart[3][0], interestText, yearsTextMonth + monthsText - i);
     *           System.out.println(monthlyChart[2][0]);
     *           System.out.println(monthlyChart[1][0]);
     *           System.out.println(monthlyChart[3][0]);
     *       }
     *     }
     * </pre>
     *
     * @param principal Loan amount.
     * @param interest  Annual rate of interest.
     * @param term      Term in months
     * @return double[][]       Returns a double array
     */
    public double[][] fixedRateMortgageMonthlyChart(double principal, double interest, double term) {

        double[][] total = new double[5][30];
        Calculate calculate = new Calculate();
        BigDecimal bd;
        BigDecimal bd1;
        BigDecimal bd2;
        BigDecimal bd3;
        BigDecimal bd4;
        for (int i = 0; i < 1; i++) {
            double interest1 = interest / 100.0;
            bd = new BigDecimal(interest1 / 12.0).setScale(5, RoundingMode.HALF_DOWN);
            total[4][i] = bd.doubleValue();

            bd1 = new BigDecimal(interest / 100).setScale(2, RoundingMode.HALF_DOWN);
            total[0][i] = bd1.doubleValue();

            bd2 = new BigDecimal((total[0][i] / 12) * principal).setScale(2, RoundingMode.HALF_DOWN);
            total[1][i] = bd2.doubleValue();

            bd3 = new BigDecimal(calculate.fixedRateMortgageMonthly(principal, term, interest) - total[1][i]).setScale(2, RoundingMode.HALF_DOWN);
            total[2][i] = bd3.doubleValue();

            bd4 = new BigDecimal(principal - total[2][i]).setScale(2, RoundingMode.HALF_DOWN);
            total[3][i] = bd4.doubleValue();
        }

        return total;
    }

    public double[][] fixedRateMortgageWeeklyChart(double principal, double interest, double term) {

        double[][] total = new double[5][30];
        Calculate calculate = new Calculate();
        BigDecimal bd;
        BigDecimal bd1;
        BigDecimal bd2;
        BigDecimal bd3;
        BigDecimal bd4;
        for (int i = 0; i < 1; i++) {
            double interest1 = interest / 100;
            bd = new BigDecimal(interest1 / 52).setScale(5, RoundingMode.HALF_DOWN);
            total[4][i] = bd.doubleValue();

            bd1 = new BigDecimal(interest / 100).setScale(2, RoundingMode.HALF_DOWN);
            total[0][i] = bd1.doubleValue();

            bd2 = new BigDecimal((total[0][i] / 52) * principal).setScale(2, RoundingMode.HALF_DOWN);
            total[1][i] = bd2.doubleValue();

            bd3 = new BigDecimal(calculate.fixedRateMortgageWeekly(principal, term, interest) - total[1][i]).setScale(2, RoundingMode.HALF_DOWN);
            total[2][i] = bd3.doubleValue();

            bd4 = new BigDecimal(principal - total[2][i]).setScale(2, RoundingMode.HALF_DOWN);
            total[3][i] = bd4.doubleValue();
        }

        return total;
    }
}
