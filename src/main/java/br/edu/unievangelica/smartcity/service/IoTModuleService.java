package br.edu.unievangelica.smartcity.service;

import br.edu.unievangelica.smartcity.model.IoTModule;
import br.edu.unievangelica.smartcity.repository.IoTModuleRepository;
import br.edu.unievangelica.smartcity.repository.SensorReadingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IoTModuleService {

    private final IoTModuleRepository moduleRepository;
    private final SensorReadingRepository readingRepository;

    public IoTModuleService(IoTModuleRepository moduleRepository, SensorReadingRepository readingRepository) {
        this.moduleRepository = moduleRepository;
        this.readingRepository = readingRepository;
    }

    public List<IoTModule> findAll() {
        return moduleRepository.findAllByOrderByTypeAscNameAsc();
    }

    public List<IoTModule> findAllActive() {
        return moduleRepository.findByActiveTrueOrderByTypeAscNameAsc();
    }

    public IoTModule findByIdOrThrow(Long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Módulo IoT não encontrado: " + id));
    }

    @Transactional
    public IoTModule save(IoTModule module) {
        return moduleRepository.save(module);
    }

    @Transactional
    public void toggleActive(Long id) {
        IoTModule module = findByIdOrThrow(id);
        module.setActive(!module.isActive());
        moduleRepository.save(module);
    }

    @Transactional
    public void delete(Long id) {
        IoTModule module = findByIdOrThrow(id);
        readingRepository.deleteByModule(module);
        moduleRepository.delete(module);
    }

    public long countReadings(IoTModule module) {
        return readingRepository.countByModule(module);
    }
}
