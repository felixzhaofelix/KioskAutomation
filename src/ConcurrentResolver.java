import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConcurrentResolver {

    public static synchronized void resolveConcurrently(List<Scenario> scenarios, List<Rule> rules) {
        ExecutorService executorService = Executors.newFixedThreadPool(scenarios.size());
        List<Future<?>> futures = new ArrayList<>();

        for (Scenario scenario : scenarios) {
            Future<?> future = executorService.submit(() -> {
                Resolver resolver = new Resolver(rules);
                resolver.resolve(scenario);
            });
            futures.add(future);
        }

        // Wait for all tasks to finish
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
    }

}

