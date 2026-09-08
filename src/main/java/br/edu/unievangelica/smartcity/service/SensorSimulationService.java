package br.edu.unievangelica.smartcity.service;

import br.edu.unievangelica.smartcity.model.IoTModule;
import br.edu.unievangelica.smartcity.model.SensorReading;
import br.edu.unievangelica.smartcity.repository.IoTModuleRepository;
import br.edu.unievangelica.smartcity.repository.SensorReadingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Coração da simulação: a cada poucos segundos, gera uma leitura
 * aleatória para cada módulo IoT ativo cadastrado no sistema,
 * persiste no H2 e transmite via SSE para os dashboards conectados.
 */
@Service
public class SensorSimulationService {

    private static final Logger log = LoggerFactory.getLogger(SensorSimulationService.class);
    private static final DateTimeFormatter TS_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final IoTModuleRepository moduleRepository;
    private final SensorReadingRepository readingRepository;
    private final SseBroadcastService sseBroadcastService;
    private final ObjectMapper objectMapper;

    public SensorSimulationService(IoTModuleRepository moduleRepository,
                                    SensorReadingRepository readingRepository,
                                    SseBroadcastService sseBroadcastService,
                                    ObjectMapper objectMapper) {
        this.moduleRepository = moduleRepository;
        this.readingRepository = readingRepository;
        this.sseBroadcastService = sseBroadcastService;
        this.objectMapper = objectMapper;
    }

    /** Gera uma nova leitura para todos os módulos ativos a cada 4 segundos. */
    @Scheduled(fixedRate = 4000)
    @Transactional
    public void generateReadings() {
        List<IoTModule> activeModules = moduleRepository.findByActiveTrueOrderByTypeAscNameAsc();
        if (activeModules.isEmpty()) {
            return;
        }

        for (IoTModule module : activeModules) {
            try {
                SensorDataGenerator.Result result = SensorDataGenerator.generate(module.getType());
                String json = objectMapper.writeValueAsString(result.data);

                SensorReading reading = new SensorReading(module, json, result.status);
                readingRepository.save(reading);

                ReadingEvent event = new ReadingEvent(
                        module.getId(),
                        module.getName(),
                        module.getType(),
                        module.getLocation(),
                        reading.getTimestamp().format(TS_FORMAT),
                        result.status,
                        result.data
                );
                sseBroadcastService.broadcast("reading", event);
            } catch (Exception e) {
                log.warn("Falha ao gerar leitura para o módulo {}: {}", module.getId(), e.getMessage());
            }
        }
    }
}
