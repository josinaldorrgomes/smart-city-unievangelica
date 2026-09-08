package br.edu.unievangelica.smartcity.repository;

import br.edu.unievangelica.smartcity.model.IoTModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IoTModuleRepository extends JpaRepository<IoTModule, Long> {

    List<IoTModule> findByActiveTrueOrderByTypeAscNameAsc();

    List<IoTModule> findAllByOrderByTypeAscNameAsc();
}
