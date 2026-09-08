package br.edu.unievangelica.smartcity.model;

/**
 * Os 7 modulos de IoT para Smart Cities contemplados pelo projeto.
 * Cada tipo define: rotulo amigavel, icone (Bootstrap Icons) e cor de destaque.
 */
public enum ModuleType {

    WATER("Monitoramento da Água", "bi-droplet-half", "#0d6efd",
            "Sensores de pH, turbidez e poluição hídrica"),

    AIR("Monitoramento do Ar", "bi-wind", "#20c997",
            "Índices de poluição do ar e dados para pesquisas de doenças respiratórias"),

    UV("Índice de Radiação Ultravioleta (UV)", "bi-brightness-high", "#fd7e14",
            "Incidência de raios UV para alertas à população"),

    MOBILE_TRACKING("Rastreamento via Celular", "bi-phone-vibrate", "#6f42c1",
            "Deslocamento, rota e velocidade de dispositivos móveis"),

    SENSOR_TRACKING("Rastreamento por Sensores", "bi-signpost-split", "#0dcaf0",
            "Tráfego de veículos, pessoas e detecção de produtos inflamáveis"),

    CCTV("Monitoramento por Imagens (CFTV)", "bi-camera-video", "#495057",
            "Circuitos de câmeras em tempo real de setores e ruas"),

    SMOKE_NOISE("Detector de Fumaça e Ruído", "bi-exclamation-diamond", "#dc3545",
            "Identificação de incêndios e monitoramento de disparos/ruídos anômalos");

    private final String label;
    private final String icon;
    private final String color;
    private final String description;

    ModuleType(String label, String icon, String color, String description) {
        this.label = label;
        this.icon = icon;
        this.color = color;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getIcon() {
        return icon;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }
}
