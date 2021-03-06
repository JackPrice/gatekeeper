package io.gatekeeper.node.service.replication.consul;

public class Lock implements AutoCloseable {

    private String name;

    private Client client;

    String session;

    public Lock(String name, Client client, String session) {
        assert null != name;
        assert name.length() > 0;
        assert null != client;
        assert null != session;
        assert session.length() > 0;

        this.name = name;
        this.client = client;
        this.session = session;
    }

    @Override
    public void close() throws Exception {
        client.unlock(this);
    }

    public void release() throws Exception {
        close();
    }
}
