package br.edu.unievangelica.smartcity.repository;

import br.edu.unievangelica.smartcity.model.IoTModule;
import br.edu.unievangelica.smartcity.model.SensorReading;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

    Optional<SensorReading> findFirstByModuleOrderByTimestampDesc(IoTModule module);

    List<SensorReading> findByModuleOrderByTimestampDesc(IoTModule module, Pageable pageable);

    List<SensorReading> findByModule_IdOrderByTimestampDesc(Long moduleId, Pageable pageable);

    long countByModule(IoTModule module);

    void deleteByModule(IoTModule module);
}
