package br.edu.unievangelica.smartcity.config;

import br.edu.unievangelica.smartcity.model.AppUser;
import br.edu.unievangelica.smartcity.model.IoTModule;
import br.edu.unievangelica.smartcity.model.ModuleType;
import br.edu.unievangelica.smartcity.repository.AppUserRepository;
import br.edu.unievangelica.smartcity.repository.IoTModuleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final IoTModuleRepository moduleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AppUserRepository appUserRepository,
                            IoTModuleRepository moduleRepository,
                            PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.moduleRepository = moduleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        criarUsuariosPadrao();
        criarModulosDemonstracao();
    }

    private void criarUsuariosPadrao() {
        if (!appUserRepository.existsByUsername("admin")) {
            appUserRepository.save(new AppUser(
                    "admin",
                    passwordEncoder.encode("admin123"),
                    "Administrador da Plataforma",
                    "ROLE_ADMIN"
            ));
        }
        if (!appUserRepository.existsByUsername("operador")) {
            appUserRepository.save(new AppUser(
                    "operador",
                    passwordEncoder.encode("operador123"),
                    "Operador de Monitoramento",
                    "ROLE_USER"
            ));
        }
    }

    private void criarModulosDemonstracao() {
        if (moduleRepository.count() > 0) {
            return;
        }

        moduleRepository.save(new IoTModule(
                "Estação Fluvial - Rio das Antas", ModuleType.WATER,
                "Campus Anápolis - Setor Sul", "Monitora pH, turbidez e poluição da água."));

        moduleRepository.save(new IoTModule(
                "Estação de Qualidade do Ar - Centro", ModuleType.AIR,
                "Praça Bom Jesus, Anápolis/GO", "Coleta dados de PM2.5, PM10 e CO2."));

        moduleRepository.save(new IoTModule(
                "Sensor UV - Parque Ambiental", ModuleType.UV,
                "Parque Ambiental Godofredo Rodrigues", "Mede índice de radiação ultravioleta."));

        moduleRepository.save(new IoTModule(
                "Malha de Rastreamento Móvel - Zona Leste", ModuleType.MOBILE_TRACKING,
                "Zona Leste, Anápolis/GO", "Rastreia deslocamento e velocidade via dispositivos móveis."));

        moduleRepository.save(new IoTModule(
                "Sensores de Tráfego - Av. Universitária", ModuleType.SENSOR_TRACKING,
                "Av. Universitária, próx. UniEVANGÉLICA", "Monitora fluxo de veículos/pessoas e produtos inflamáveis."));

        moduleRepository.save(new IoTModule(
                "CFTV - Terminal Central", ModuleType.CCTV,
                "Terminal Central de Ônibus", "Circuito de câmeras para monitoramento de área pública."));

        moduleRepository.save(new IoTModule(
                "Detector de Fumaça/Ruído - Distrito Industrial", ModuleType.SMOKE_NOISE,
                "Distrito Industrial, Anápolis/GO", "Identifica princípios de incêndio e ruídos anômalos (disparos)."));
    }
}
