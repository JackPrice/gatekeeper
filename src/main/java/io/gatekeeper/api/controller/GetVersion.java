package io.gatekeeper.api.controller;

import io.gatekeeper.api.AbstractController;
import io.gatekeeper.api.model.Version;
import io.gatekeeper.node.ServiceContainer;
import io.vertx.core.http.HttpServerRequest;

import static io.gatekeeper.Version.CURRENT;

public class GetVersion extends AbstractController {

    public GetVersion(ServiceContainer container) {
        super(container);
    }

    @Override
    protected Object handle(HttpServerRequest request) throws Exception {
        Version version = new Version();

        version.version(CURRENT.toString())
            .major((int) CURRENT.major)
            .minor((int) CURRENT.minor)
            .revision((int) CURRENT.revision);

        return version;
    }
}
