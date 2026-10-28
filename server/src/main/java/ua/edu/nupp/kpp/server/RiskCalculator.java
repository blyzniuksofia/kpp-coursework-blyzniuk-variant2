package ua.edu.nupp.kpp.server;

public class RiskCalculator {
    public static double factor1(double observedValue, double limitValue) {
        if (observedValue < 0 || limitValue <= 0)
            throw new IllegalArgumentException("Некоректні дані для F1");
        double value = (observedValue - limitValue) / limitValue * 100.0;
        return Math.max(0.0, Math.min(100.0, value));
    }
    public static double calculate(double f1, double f2, double f3) {
        check(f1);
        check(f2);
        check(f3);
        return Math.round((0.40*f1 + 0.35*f2 + 0.25*f3) * 100.0) / 100.0;
    }
    private static void check(double value) {
        if (value < 0 || value > 100)
            throw new IllegalArgumentException("Фактор має бути в діапазоні 0..100");
    }
}


