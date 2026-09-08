package br.edu.unievangelica.smartcity.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Mantém a lista de clientes (navegadores) conectados via Server-Sent
 * Events e distribui os eventos de "nova leitura" gerados pelo
 * {@link SensorSimulationService} para todos eles em tempo real.
 */
@Service
public class SseBroadcastService {

    private static final Logger log = LoggerFactory.getLogger(SseBroadcastService.class);

    /** Timeout longo: o navegador reconecta automaticamente se cair. */
    private static final long EMITTER_TIMEOUT = 30L * 60 * 1000; // 30 minutos

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(EMITTER_TIMEOUT);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        try {
            emitter.send(SseEmitter.event().name("connected").data("ok"));
        } catch (IOException e) {
            emitters.remove(emitter);
        }
        return emitter;
    }

    public void broadcast(String eventName, Object payload) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(payload));
            } catch (Exception e) {
                emitter.complete();
                emitters.remove(emitter);
                log.debug("Removendo emitter desconectado: {}", e.getMessage());
            }
        }
    }

    public int activeClients() {
        return emitters.size();
    }
}
