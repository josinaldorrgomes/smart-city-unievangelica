(function () {
    const canvas = document.getElementById("liveChart");
    if (!canvas) return;

    const MAX_POINTS = 25;
    const PALETTE = ["#0e4a86", "#f2a900", "#1c9e5a", "#d7263d", "#6f42c1", "#0dcaf0"];

    function numericKeys(historyArr) {
        const keys = new Set();
        historyArr.forEach((point) => {
            Object.entries(point.data || {}).forEach(([k, v]) => {
                if (typeof v === "number") keys.add(k);
            });
        });
        return Array.from(keys);
    }

    const labels = HISTORY.map((p) => p.timestamp);
    const keys = numericKeys(HISTORY);

    const datasets = keys.map((key, idx) => ({
        label: metricLabel(key),
        data: HISTORY.map((p) => (typeof p.data[key] === "number" ? p.data[key] : null)),
        borderColor: PALETTE[idx % PALETTE.length],
        backgroundColor: PALETTE[idx % PALETTE.length] + "22",
        tension: 0.35,
        fill: false,
        pointRadius: 2,
        borderWidth: 2
    }));

    const chart = new Chart(canvas.getContext("2d"), {
        type: "line",
        data: { labels: labels, datasets: datasets },
        options: {
            responsive: true,
            animation: { duration: 300 },
            interaction: { mode: "index", intersect: false },
            plugins: { legend: { position: "bottom" } },
            scales: { y: { beginAtZero: true } }
        }
    });

    function updateDetailPanel(evt) {
        const dot = document.getElementById("detailStatusDot");
        const text = document.getElementById("detailStatusText");
        const ts = document.getElementById("detailTs");
        const metrics = document.getElementById("detailMetrics");

        dot.className = "status-dot status-" + evt.status;
        text.textContent = evt.status === "NORMAL" ? "Normal" : (evt.status === "ALERTA" ? "Alerta" : "Crítico");
        text.className = "status-" + evt.status;
        ts.textContent = evt.timestamp;

        let html = "";
        Object.keys(evt.data || {}).forEach((key) => {
            html += `<div class="metric-row">
                        <span>${metricLabel(key)}</span>
                        <span class="metric-value">${formatMetricValue(key, evt.data[key])}</span>
                     </div>`;
        });
        metrics.innerHTML = html || '<div class="text-muted small fst-italic">Aguardando dados...</div>';
    }

    function pushPoint(evt) {
        chart.data.labels.push(evt.timestamp);
        if (chart.data.labels.length > MAX_POINTS) chart.data.labels.shift();

        // garante que todo dataset já conhecido exista; cria novos datasets dinamicamente se necessário
        Object.entries(evt.data || {}).forEach(([key, value]) => {
            if (typeof value !== "number") return;
            let ds = chart.data.datasets.find((d) => d.label === metricLabel(key));
            if (!ds) {
                const idx = chart.data.datasets.length;
                ds = {
                    label: metricLabel(key),
                    data: chart.data.labels.map(() => null),
                    borderColor: PALETTE[idx % PALETTE.length],
                    backgroundColor: PALETTE[idx % PALETTE.length] + "22",
                    tension: 0.35,
                    fill: false,
                    pointRadius: 2,
                    borderWidth: 2
                };
                chart.data.datasets.push(ds);
            }
            ds.data.push(value);
            if (ds.data.length > MAX_POINTS) ds.data.shift();
        });

        // datasets sem valor nesta leitura recebem null para manter o eixo alinhado
        chart.data.datasets.forEach((ds) => {
            if (ds.data.length < chart.data.labels.length) ds.data.push(null);
            if (ds.data.length > MAX_POINTS) ds.data.shift();
        });

        chart.update("none");
    }

    function connect() {
        const source = new EventSource("/api/stream");
        source.addEventListener("reading", function (event) {
            try {
                const evt = JSON.parse(event.data);
                if (evt.moduleId !== MODULE_ID) return;
                updateDetailPanel(evt);
                pushPoint(evt);
            } catch (e) {
                console.error("Erro ao processar evento SSE", e);
            }
        });
        source.onerror = function () {
            console.warn("Conexão SSE interrompida, tentando reconectar...");
        };
    }

    connect();
})();
