package io.gatekeeper.node.service.replication.common;

/**
 * A class for holding a node definition.
 */
public class Node {
    public final String id;

    public final String host;

    public final Integer port;

    public Node(String id, String host, Integer port) {
        assert null != id;
        assert id.length() > 0;
        assert null != host;
        assert host.length() > 0;
        assert null != port;

        this.id = id;
        this.host = host;
        this.port = port;
    }
}
