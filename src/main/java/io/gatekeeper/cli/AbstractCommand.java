package io.gatekeeper.cli;

import org.apache.commons.cli.Options;

public abstract class AbstractCommand {

    private String name = "";

    private String description = "";

    private Options options = new Options();

    public abstract void run(Input input, Output output);

    public abstract void configure();

    public void setName(String name) {
        assert null != name;
        assert name.length() > 0;

        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return description;
    }

    public Options getOptions() {
        return options;
    }

    public void setDescription(String description) {
        assert null != description;
        assert description.length() > 0;

        this.description = description;
    }

    public void withFlag(String name) {
        assert null != name;
        assert name.length() > 0;

        options.addOption(null, name, false, null);
    }

    public void withOption(String name) {
        assert null != name;
        assert name.length() > 0;

        options.addOption(null, name, true, null);
    }

}
