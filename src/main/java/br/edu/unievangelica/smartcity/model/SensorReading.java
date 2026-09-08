package br.edu.unievangelica.smartcity.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Leitura pontual gerada (de forma simulada/randômica) por um IoTModule.
 * Os dados numéricos específicos de cada tipo de sensor ficam armazenados
 * como JSON em {@link #dataJson}, mantendo o modelo flexível para os
 * 7 tipos de módulo sem precisar de 7 tabelas diferentes.
 */
@Entity
@Table(name = "sensor_reading", indexes = {
        @Index(name = "idx_reading_module_time", columnList = "module_id, timestamp")
})
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "module_id", nullable = false)
    private IoTModule module;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    @Lob
    @Column(name = "data_json", nullable = false)
    private String dataJson;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReadingStatus status = ReadingStatus.NORMAL;

    public SensorReading() {
    }

    public SensorReading(IoTModule module, String dataJson, ReadingStatus status) {
        this.module = module;
        this.dataJson = dataJson;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IoTModule getModule() {
        return module;
    }

    public void setModule(IoTModule module) {
        this.module = module;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    public ReadingStatus getStatus() {
        return status;
    }

    public void setStatus(ReadingStatus status) {
        this.status = status;
    }
}
