(function () {
    if (!document.getElementById("cardsContainer")) {
        return; // não estamos na página de dashboard
    }

    function renderMetrics(container, data) {
        const keys = Object.keys(data || {});
        if (keys.length === 0) {
            container.innerHTML = '<div class="text-muted small fst-italic">Aguardando primeira leitura...</div>';
            return;
        }
        let html = "";
        keys.forEach((key) => {
            html += `<div class="metric-row">
                        <span>${metricLabel(key)}</span>
                        <span class="metric-value">${formatMetricValue(key, data[key])}</span>
                     </div>`;
        });
        container.innerHTML = html;
    }

    function updateCard(evt) {
        const col = document.getElementById("col-module-" + evt.moduleId);
        if (!col) return; // módulo cadastrado depois do carregamento da página

        const card = document.getElementById("module-card-" + evt.moduleId);
        const statusDot = document.getElementById("status-dot-" + evt.moduleId);
        const statusText = document.getElementById("status-text-" + evt.moduleId);
        const ts = document.getElementById("ts-" + evt.moduleId);
        const metrics = document.getElementById("metrics-" + evt.moduleId);

        statusDot.className = "status-dot status-" + evt.status;
        statusText.textContent = evt.status === "NORMAL" ? "Normal" : (evt.status === "ALERTA" ? "Alerta" : "Crítico");
        statusText.className = "status-" + evt.status;
        ts.textContent = evt.timestamp;

        renderMetrics(metrics, evt.data);

        card.classList.remove("flash-update");
        void card.offsetWidth; // força reflow para reiniciar a animação
        card.classList.add("flash-update");

        if (evt.status === "CRITICO") {
            card.classList.add("pulse");
        } else {
            card.classList.remove("pulse");
        }
    }

    function connect() {
        const source = new EventSource("/api/stream");

        source.addEventListener("reading", function (event) {
            try {
                const data = JSON.parse(event.data);
                updateCard(data);
            } catch (e) {
                console.error("Erro ao processar evento SSE", e);
            }
        });

        source.onerror = function () {
            // EventSource tenta reconectar automaticamente; apenas registramos.
            console.warn("Conexão SSE interrompida, tentando reconectar...");
        };
    }

    connect();
})();
