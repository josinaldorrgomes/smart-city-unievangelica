package br.edu.unievangelica.smartcity.service;

import br.edu.unievangelica.smartcity.model.ModuleType;
import br.edu.unievangelica.smartcity.model.ReadingStatus;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Responsável por "fabricar" leituras aleatórias plausíveis para cada
 * um dos 7 tipos de módulo IoT de Smart City. Isso simula, para fins
 * didáticos, o que sensores reais enviariam a um gateway/broker.
 */
final class SensorDataGenerator {

    private SensorDataGenerator() {
    }

    static final class Result {
        final Map<String, Object> data;
        final ReadingStatus status;

        Result(Map<String, Object> data, ReadingStatus status) {
            this.data = data;
            this.status = status;
        }
    }

    static Result generate(ModuleType type) {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        Map<String, Object> data = new LinkedHashMap<>();
        ReadingStatus status = ReadingStatus.NORMAL;

        switch (type) {
            case WATER -> {
                double ph = round(r.nextDouble(5.5, 9.0));
                double turbidez = round(r.nextDouble(0, 60));       // NTU
                double poluicao = round(r.nextDouble(0, 100));      // índice 0-100
                double oxigenioDissolvido = round(r.nextDouble(2, 10)); // mg/L
                data.put("ph", ph);
                data.put("turbidezNTU", turbidez);
                data.put("indicePoluicao", poluicao);
                data.put("oxigenioDissolvido", oxigenioDissolvido);
                if (ph < 6.0 || ph > 8.5 || poluicao > 75) {
                    status = ReadingStatus.CRITICO;
                } else if (turbidez > 40 || poluicao > 45) {
                    status = ReadingStatus.ALERTA;
                }
            }
            case AIR -> {
                double pm25 = round(r.nextDouble(0, 150));
                double pm10 = round(r.nextDouble(0, 200));
                double co2 = round(r.nextDouble(350, 1200));
                double indiceQualidade = round(r.nextDouble(0, 300));
                data.put("pm25", pm25);
                data.put("pm10", pm10);
                data.put("co2ppm", co2);
                data.put("indiceQualidadeAr", indiceQualidade);
                if (indiceQualidade > 200 || pm25 > 100) {
                    status = ReadingStatus.CRITICO;
                } else if (indiceQualidade > 100 || pm25 > 55) {
                    status = ReadingStatus.ALERTA;
                }
            }
            case UV -> {
                double indiceUv = round(r.nextDouble(0, 13));
                String risco;
                if (indiceUv < 3) risco = "Baixo";
                else if (indiceUv < 6) risco = "Moderado";
                else if (indiceUv < 8) risco = "Alto";
                else if (indiceUv < 11) risco = "Muito Alto";
                else risco = "Extremo";
                data.put("indiceUV", indiceUv);
                data.put("classificacaoRisco", risco);
                if (indiceUv >= 11) status = ReadingStatus.CRITICO;
                else if (indiceUv >= 8) status = ReadingStatus.ALERTA;
            }
            case MOBILE_TRACKING -> {
                double velocidade = round(r.nextDouble(0, 90));
                double distancia = round(r.nextDouble(0, 5));
                double latVar = round(r.nextDouble(-0.002, 0.002));
                double lonVar = round(r.nextDouble(-0.002, 0.002));
                int dispositivosAtivos = r.nextInt(1, 40);
                data.put("velocidadeKmh", velocidade);
                data.put("distanciaPercorridaKm", distancia);
                data.put("deltaLat", latVar);
                data.put("deltaLon", lonVar);
                data.put("dispositivosAtivos", dispositivosAtivos);
                if (velocidade > 80) status = ReadingStatus.ALERTA;
            }
            case SENSOR_TRACKING -> {
                int fluxoVeiculos = r.nextInt(0, 120);
                int fluxoPessoas = r.nextInt(0, 300);
                boolean produtoInflamavelDetectado = r.nextInt(0, 100) < 4; // ~4% de chance
                data.put("fluxoVeiculosPorMin", fluxoVeiculos);
                data.put("fluxoPessoasPorMin", fluxoPessoas);
                data.put("produtoInflamavelDetectado", produtoInflamavelDetectado);
                if (produtoInflamavelDetectado) status = ReadingStatus.CRITICO;
                else if (fluxoVeiculos > 100) status = ReadingStatus.ALERTA;
            }
            case CCTV -> {
                int pessoasDetectadas = r.nextInt(0, 50);
                int veiculosDetectados = r.nextInt(0, 30);
                boolean cameraOnline = r.nextInt(0, 100) < 97; // ~3% chance de ficar offline
                data.put("pessoasDetectadas", pessoasDetectadas);
                data.put("veiculosDetectados", veiculosDetectados);
                data.put("cameraOnline", cameraOnline);
                if (!cameraOnline) status = ReadingStatus.ALERTA;
            }
            case SMOKE_NOISE -> {
                double fumacaPpm = round(r.nextDouble(0, 400));
                double ruidoDb = round(r.nextDouble(30, 130));
                boolean alertaIncendio = fumacaPpm > 300;
                boolean alertaDisparo = ruidoDb > 115 && r.nextInt(0, 100) < 15;
                data.put("fumacaPPM", fumacaPpm);
                data.put("nivelRuidoDB", ruidoDb);
                data.put("alertaIncendio", alertaIncendio);
                data.put("alertaDisparo", alertaDisparo);
                if (alertaIncendio || alertaDisparo) status = ReadingStatus.CRITICO;
                else if (fumacaPpm > 150 || ruidoDb > 90) status = ReadingStatus.ALERTA;
            }
        }

        return new Result(data, status);
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
