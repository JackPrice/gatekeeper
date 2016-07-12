package io.gatekeeper.model;

import java.util.Date;
import java.util.UUID;

/**
 * A certificate represents a single SSL certificate and key pair, generated by a provider for a single endpoint.
 * They are immutable, and can only be regenerated, never edited.
 */
public class CertificateModel extends AbstractModel {

    /**
     * The DN that this certificate contains.
     */
    protected String dn;

    /**
     * The date this certificate expires.
     */
    protected Date expires;

    /**
     * The UUID of the provider that generated this certificate.
     */
    protected UUID provider;

    /**
     * The date this certificate became known to gatekeeper.
     */
    protected Date created;

    /**
     * The raw PEM-encoded data in this certificate.
     */
    protected String certificate;

    /**
     * The raw PEM-encoded key for this certificate.
     */
    protected String key;

    /**
     * The PEM-encoded certificate chain for this certificate.
     */
    protected String chain;

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public UUID getProvider() {
        return provider;
    }

    public void setProvider(UUID provider) {
        this.provider = provider;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }
}
