package space.cubicworld;

public interface DiscomcModule {

    void load();

    void enable();

    void disable();

    boolean isEnabled();

    void setEnabled(boolean enabled);

    String getDescription();

    /**
     * @return DiscomcConfig instance if this class is not discomc class, returns null
     */
    DiscomcConfig getConfig();

}
