package io.gatekeeper.node;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.gatekeeper.GatekeeperException;
import io.gatekeeper.configuration.Configuration;
import io.gatekeeper.logging.Loggers;
import io.gatekeeper.node.service.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Node implements Closeable {

    private final UUID name;

    private final Configuration configuration;

    private final Logger logger;

    private final ServiceContainer services = new ServiceContainer();

    private final ThreadPoolExecutor executor;

    public Node(Configuration configuration) {
        assert null != configuration;

        this.name = UUID.randomUUID();
        this.configuration = configuration;
        this.logger = Loggers.getNodeLogger();
        this.executor = (ThreadPoolExecutor) Executors.newCachedThreadPool(
            (new ThreadFactoryBuilder())
                .setNameFormat("Node %d")
                .build()
        );

        this.executor.prestartCoreThread();
        this.executor.prestartAllCoreThreads();
    }

    public CompletableFuture<Void> start() {
        this.logger.info(String.format("Starting node %s", this.name.toString()));

        this.createServices();

        CompletableFuture<Void> future = new CompletableFuture<>();

        this.executor.execute(() -> {
            this.startServices().join();

            this.logger.info("Node started");

            future.complete(null);
        });

        return future;
    }

    private CompletableFuture<Void> startServices() {
        this.logger.info("Starting services");

        List<CompletableFuture> futures = this.services
            .entrySet()
            .stream()
            .map(entry -> entry.getValue().start())
            .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @Override
    public void close() throws IOException {
        this.logger.info("Stopping services");

        List<Exception> exceptions = new ArrayList<>();

        for (Map.Entry<String, Service> entry : this.services.entrySet()) {
            try {
                entry.getValue().close();
            } catch (Exception exception) {
                exceptions.add(exception);
            }
        }

        // TODO: Report exceptions

        this.logger.info(String.format("Shutting down %d node threads", this.executor.getActiveCount()));

        this.executor.shutdown();

        try {
            this.executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            this.logger.warning(String.format(
                "Killing %d node threads that did not shut down in time",
                this.executor.getActiveCount()
            ));
        }

        this.logger.info("Node shutdown complete");
    }

    private void createServices() {
        services.service(ReplicationService.class, createReplicationService());

        if (configuration.replication.server) {
            services.service(ProviderService.class, createProviderService());
            services.service(BackendService.class, createBackendService());
            services.service(ApiService.class, createApiService());
            services.service(OutputService.class, createOutputService());
        }
    }

    @SuppressWarnings("unchecked")
    private ReplicationService createReplicationService() {
        Class<ReplicationService> clazz = (Class<ReplicationService>) this.configuration.replication.serviceClass();

        assert null != clazz;
        assert ReplicationService.class.isAssignableFrom(clazz);

        try {
            return clazz.getConstructor(Configuration.class).newInstance(this.configuration);
        } catch (Exception exception) {
            throw new GatekeeperException(
                String.format("Failed to create replication service %s", clazz.getCanonicalName()),
                exception
            );
        }
    }

    @SuppressWarnings("unchecked")
    private ProviderService createProviderService() {
        return ProviderService.build(services);
    }

    @SuppressWarnings("unchecked")
    private BackendService createBackendService() {
        Class<BackendService> clazz = (Class<BackendService>) this.configuration.backend.serviceClass();

        assert null != clazz;
        assert BackendService.class.isAssignableFrom(clazz);

        try {
            return clazz.getConstructor(Configuration.class, ReplicationService.class, ProviderService.class)
                .newInstance(
                    this.configuration,
                    services.service(ReplicationService.class),
                    services.service(ProviderService.class)
                );
        } catch (Exception exception) {
            throw new GatekeeperException(
                String.format("Failed to create backend service %s", clazz.getCanonicalName()),
                exception
            );
        }
    }

    @SuppressWarnings("unchecked")
    private ApiService createApiService() {
        return new ApiService(configuration, services);
    }

    private OutputService createOutputService() {
        return OutputService.build(configuration, services);
    }

}
