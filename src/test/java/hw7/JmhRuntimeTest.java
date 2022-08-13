package hw7;

import hw7.profiler.GcProfiler;
import hw7.search.Jhugle;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class JmhRuntimeTest {

  @State(Scope.Benchmark)
  public static class BenchmarkState {
    @Param({"apache.txt", "jhu.txt", "joanne.txt", "newegg.txt", "random164.txt", "urls.txt"})
    public String fileName;
  }

  @Benchmark
  @Fork(value = 1, warmups = 1)
  @Warmup(iterations = 1)
  @Measurement(iterations = 2)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void buildSearchEngine(Blackhole blackhole, BenchmarkState state) {
    Config.DATA_FILENAME = state.fileName;
    Jhugle jhUgle = new Jhugle(Config.getMap());
    jhUgle.buildSearchEngine(Config.getDataFile());
    blackhole.consume(jhUgle);
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
        .include(JmhRuntimeTest.class.getSimpleName())
        .addProfiler(GcProfiler.class)
        .build();

    new Runner(opt).run();
  }
}
