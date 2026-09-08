package br.edu.unievangelica.smartcity.controller;

import br.edu.unievangelica.smartcity.model.IoTModule;
import br.edu.unievangelica.smartcity.model.ModuleType;
import br.edu.unievangelica.smartcity.model.SensorReading;
import br.edu.unievangelica.smartcity.repository.SensorReadingRepository;
import br.edu.unievangelica.smartcity.service.IoTModuleService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class DashboardController {

    private static final DateTimeFormatter TS_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final IoTModuleService moduleService;
    private final SensorReadingRepository readingRepository;
    private final ObjectMapper objectMapper;

    public DashboardController(IoTModuleService moduleService,
                                SensorReadingRepository readingRepository,
                                ObjectMapper objectMapper) {
        this.moduleService = moduleService;
        this.readingRepository = readingRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<IoTModule> modules = moduleService.findAllActive();

        List<Map<String, Object>> cards = new ArrayList<>();
        for (IoTModule module : modules) {
            Map<String, Object> card = new LinkedHashMap<>();
            card.put("module", module);

            Optional<SensorReading> lastReading = readingRepository.findFirstByModuleOrderByTimestampDesc(module);
            lastReading.ifPresentOrElse(reading -> {
                card.put("status", reading.getStatus());
                card.put("data", parseJson(reading.getDataJson()));
                card.put("timestamp", reading.getTimestamp().format(TS_FORMAT));
            }, () -> {
                card.put("status", null);
                card.put("data", Collections.emptyMap());
                card.put("timestamp", "--:--:--");
            });

            cards.add(card);
        }

        model.addAttribute("cards", cards);
        model.addAttribute("totalModulos", modules.size());
        return "dashboard";
    }

    @GetMapping("/modules/{id}")
    public String moduleDetail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        IoTModule module = moduleService.findByIdOrThrow(id);

        List<SensorReading> history = readingRepository
                .findByModuleOrderByTimestampDesc(module, PageRequest.of(0, 25));
        Collections.reverse(history);

        List<Map<String, Object>> historyDto = new ArrayList<>();
        for (SensorReading reading : history) {
            Map<String, Object> point = new LinkedHashMap<>();
            point.put("timestamp", reading.getTimestamp().format(TS_FORMAT));
            point.put("status", reading.getStatus());
            point.put("data", parseJson(reading.getDataJson()));
            historyDto.add(point);
        }

        model.addAttribute("module", module);
        model.addAttribute("history", historyDto);
        model.addAttribute("readingCount", moduleService.countReadings(module));
        return "module-detail";
    }

    private Map<String, Object> parseJson(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }
}
