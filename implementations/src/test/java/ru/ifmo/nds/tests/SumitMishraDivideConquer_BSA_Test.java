package ru.ifmo.nds.tests;

import ru.ifmo.nds.NonDominatedSortingFactory;
import ru.ifmo.nds.SumitMishraDivideConquer;

public class SumitMishraDivideConquer_BSA_Test extends CorrectnessTestsBase {
    @Override
    protected NonDominatedSortingFactory getFactory() {
        return SumitMishraDivideConquer.getAlternativeImplementation(true);
    }
}
