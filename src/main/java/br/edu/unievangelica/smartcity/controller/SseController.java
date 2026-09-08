package br.edu.unievangelica.smartcity.controller;

import br.edu.unievangelica.smartcity.service.SseBroadcastService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class SseController {

    private final SseBroadcastService sseBroadcastService;

    public SseController(SseBroadcastService sseBroadcastService) {
        this.sseBroadcastService = sseBroadcastService;
    }

    /** Endpoint consumido pelo JavaScript (EventSource) dos dashboards. */
    @GetMapping(path = "/api/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream() {
        return sseBroadcastService.subscribe();
    }
}
