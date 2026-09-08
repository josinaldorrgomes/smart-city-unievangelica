package br.edu.unievangelica.smartcity.service;

import br.edu.unievangelica.smartcity.model.ModuleType;
import br.edu.unievangelica.smartcity.model.ReadingStatus;

import java.util.Map;

/**
 * Payload leve (serializado em JSON pelo Jackson) enviado a cada
 * navegador conectado sempre que uma nova leitura simulada é gerada.
 */
public class ReadingEvent {

    private Long moduleId;
    private String moduleName;
    private ModuleType moduleType;
    private String location;
    private String timestamp;
    private ReadingStatus status;
    private Map<String, Object> data;

    public ReadingEvent() {
    }

    public ReadingEvent(Long moduleId, String moduleName, ModuleType moduleType, String location,
                         String timestamp, ReadingStatus status, Map<String, Object> data) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.moduleType = moduleType;
        this.location = location;
        this.timestamp = timestamp;
        this.status = status;
        this.data = data;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public ModuleType getModuleType() {
        return moduleType;
    }

    public void setModuleType(ModuleType moduleType) {
        this.moduleType = moduleType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public ReadingStatus getStatus() {
        return status;
    }

    public void setStatus(ReadingStatus status) {
        this.status = status;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
