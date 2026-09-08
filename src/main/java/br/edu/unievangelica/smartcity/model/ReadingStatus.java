package br.edu.unievangelica.smartcity.model;

public enum ReadingStatus {
    NORMAL("Normal", "success"),
    ALERTA("Alerta", "warning"),
    CRITICO("Crítico", "danger");

    private final String label;
    private final String bootstrapClass;

    ReadingStatus(String label, String bootstrapClass) {
        this.label = label;
        this.bootstrapClass = bootstrapClass;
    }

    public String getLabel() {
        return label;
    }

    public String getBootstrapClass() {
        return bootstrapClass;
    }
}
