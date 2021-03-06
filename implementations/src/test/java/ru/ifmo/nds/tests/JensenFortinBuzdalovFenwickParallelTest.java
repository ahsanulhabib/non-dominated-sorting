package ru.ifmo.nds.tests;

import ru.ifmo.nds.JensenFortinBuzdalov;
import ru.ifmo.nds.NonDominatedSortingFactory;

public class JensenFortinBuzdalovFenwickParallelTest extends CorrectnessTestsBase {
    @Override
    protected NonDominatedSortingFactory getFactory() {
        return JensenFortinBuzdalov.getFenwickSweepImplementation(-1);
    }
}
