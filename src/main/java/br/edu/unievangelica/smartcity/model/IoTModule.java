package br.edu.unievangelica.smartcity.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Representa um modulo/sensor de IoT instalado em algum ponto da cidade.
 * Ex.: "Estação de Qualidade da Água - Rio Piancó", tipo WATER.
 */
@Entity
@Table(name = "iot_module")
public class IoTModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Informe um nome para o módulo")
    @Column(nullable = false, length = 150)
    private String name;

    @NotNull(message = "Selecione o tipo do módulo")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private ModuleType type;

    @NotBlank(message = "Informe a localização")
    @Column(nullable = false, length = 150)
    private String location;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean active = true;

    /** Intervalo (segundos) simulado entre leituras - usado apenas informativamente. */
    @Column(name = "sampling_interval_seconds")
    private Integer samplingIntervalSeconds = 5;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public IoTModule() {
    }

    public IoTModule(String name, ModuleType type, String location, String description) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.description = description;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ModuleType getType() {
        return type;
    }

    public void setType(ModuleType type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getSamplingIntervalSeconds() {
        return samplingIntervalSeconds;
    }

    public void setSamplingIntervalSeconds(Integer samplingIntervalSeconds) {
        this.samplingIntervalSeconds = samplingIntervalSeconds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
