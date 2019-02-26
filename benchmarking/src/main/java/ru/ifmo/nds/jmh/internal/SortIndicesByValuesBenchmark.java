package ru.ifmo.nds.jmh.internal;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import ru.ifmo.nds.util.ArrayHelper;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Timeout(time = 10)
@Warmup(time = 1, iterations = 6)
@Measurement(time = 1, iterations = 2)
@Fork(value = 5)
public class SortIndicesByValuesBenchmark {
    @Param({"1", "2", "3", "4", "5", "7", "10", "13", "17", "23", "31", "42", "56", "74", "100", "133", "177", "237", "316"})
    private int size;

    @Param({"10", "12", "14", "16", "18", "20", "22", "24", "26", "28", "30", "32", "34", "36", "38", "40"})
    private int threshold;

    private int[][] data;
    private int[] indices;

    @Setup
    public void initialize() {
        int nInstances = 100;
        Random random = new Random(size * 318325462111L);
        data = new int[nInstances][size];
        for (int i = 0; i < nInstances; ++i) {
            for (int j = 0; j < size; ++j) {
                data[i][j] = random.nextInt();
            }
        }
        indices = new int[size];
    }

    @OperationsPerInvocation(100)
    @Benchmark
    public void run(Blackhole bh) {
        for (int[] instance : data) {
            ArrayHelper.fillIdentity(indices, size);
            sortIndicesByValues(indices, instance, 0, size);
        }
    }

    private static int splitIndicesByRanks(int[] indices, int[] values, int from, int until) {
        int left = from, right = until - 1;
        int pivot = values[indices[(from + until) >>> 1]];
        int sl, sr;
        while (left <= right) {
            while (values[sl = indices[left]] < pivot) ++left;
            while (values[sr = indices[right]] > pivot) --right;
            if (left <= right) {
                indices[left] = sr;
                indices[right] = sl;
                ++left;
                --right;
            }
        }
        return left - 1 == right ? left : -left - 1;
    }

    private static void insertionSortIndicesByValues(int[] indices, int[] values, int from, int to) {
        for (int i = from, j = i; i < to; j = i) {
            int ii = indices[++i], ij;
            int ai = values[ii];
            while (ai < values[ij = indices[j]]) {
                indices[j + 1] = ij;
                if (--j < from) {
                    break;
                }
            }
            indices[j + 1] = ii;
        }
    }

    private void sortIndicesByValues(int[] indices, int[] values, int from, int until) {
        if (from + threshold > until) {
            insertionSortIndicesByValues(indices, values, from, until - 1);
        } else {
            int left = splitIndicesByRanks(indices, values, from, until);
            int right = left;
            if (left < 0) {
                left = -left - 1;
                right = left - 1;
            }
            if (from + 1 < right) {
                sortIndicesByValues(indices, values, from, right);
            }
            if (left + 1 < until) {
                sortIndicesByValues(indices, values, left, until);
            }
        }
    }
}