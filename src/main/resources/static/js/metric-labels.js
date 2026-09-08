// Mapeia a chave técnica gerada pelo backend para um rótulo amigável + unidade/formatação.
// Usado tanto no dashboard geral quanto na página de detalhe do módulo.
const METRIC_LABELS = {
    // Água
    ph: { label: "pH", suffix: "" },
    turbidezNTU: { label: "Turbidez", suffix: " NTU" },
    indicePoluicao: { label: "Índice de Poluição", suffix: "" },
    oxigenioDissolvido: { label: "Oxigênio Dissolvido", suffix: " mg/L" },

    // Ar
    pm25: { label: "PM2.5", suffix: " µg/m³" },
    pm10: { label: "PM10", suffix: " µg/m³" },
    co2ppm: { label: "CO₂", suffix: " ppm" },
    indiceQualidadeAr: { label: "Índice Qualidade do Ar", suffix: "" },

    // UV
    indiceUV: { label: "Índice UV", suffix: "" },
    classificacaoRisco: { label: "Risco", suffix: "", raw: true },

    // Rastreamento celular
    velocidadeKmh: { label: "Velocidade", suffix: " km/h" },
    distanciaPercorridaKm: { label: "Distância Percorrida", suffix: " km" },
    deltaLat: { label: "Δ Latitude", suffix: "" },
    deltaLon: { label: "Δ Longitude", suffix: "" },
    dispositivosAtivos: { label: "Dispositivos Ativos", suffix: "" },

    // Sensores de tráfego
    fluxoVeiculosPorMin: { label: "Fluxo de Veículos", suffix: " /min" },
    fluxoPessoasPorMin: { label: "Fluxo de Pessoas", suffix: " /min" },
    produtoInflamavelDetectado: { label: "Produto Inflamável", suffix: "", bool: true },

    // CFTV
    pessoasDetectadas: { label: "Pessoas Detectadas", suffix: "" },
    veiculosDetectados: { label: "Veículos Detectados", suffix: "" },
    cameraOnline: { label: "Status da Câmera", suffix: "", statusBool: true },

    // Fumaça / Ruído
    fumacaPPM: { label: "Fumaça", suffix: " ppm" },
    nivelRuidoDB: { label: "Nível de Ruído", suffix: " dB" },
    alertaIncendio: { label: "Alerta de Incêndio", suffix: "", bool: true },
    alertaDisparo: { label: "Alerta de Disparo", suffix: "", bool: true }
};

function formatMetricValue(key, value) {
    const meta = METRIC_LABELS[key];
    if (!meta) return String(value);
    if (meta.statusBool) {
        return value ? "🟢 Online" : "🔴 Offline";
    }
    if (meta.bool) {
        return value ? "⚠️ Sim" : "Não";
    }
    if (meta.raw) return String(value);
    return value + meta.suffix;
}

function metricLabel(key) {
    const meta = METRIC_LABELS[key];
    return meta ? meta.label : key;
}
