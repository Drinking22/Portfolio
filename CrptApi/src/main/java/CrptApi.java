import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.*;

/**
 * Класс для работы с API Честного знака.
 */
public class CrptApi {

    private static final Logger logger = LogManager.getLogger(CrptApi.class);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private final BlockingQueue<Runnable> taskQueue;

    private final int requestLimit;
    private final Duration interval;
    private long lastResetTime;
    private int requestsInInterval;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.requestLimit = requestLimit;
        this.interval = Duration.ofSeconds(timeUnit.toSeconds(1));
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.taskQueue = new LinkedBlockingQueue<>();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        this.lastResetTime = System.currentTimeMillis();
        this.requestsInInterval = 0;

        executorService.submit(() -> {
            try {
                while (true) {
                    taskQueue.take().run();
                }
            } catch (InterruptedException ex) {
                logger.error("Task execution interrupted", ex);
                Thread.currentThread().interrupt();
            }
        });
    }

    /**
     * Метод для создания документа.
     *
     * @param url Путь к ресурсу
     * @param document Документ
     * @param signature Подпись
     */
    public void createDocument(String url, Document document, String signature) {
        submitTask(() -> {
            try {
                String requestBody = objectMapper.writeValueAsString(document);
                HttpRequest httpRequest = createHttpRequest(url, signature, requestBody);

                HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    logger.info("Response " + response.body());
                } else {
                    logger.error("Error response " + response.body());
                }

                synchronized (this) {
                    requestsInInterval++;
                    resetRequestLimitIfNeeded();
                }

            } catch (Exception ex) {
                logger.error("Error creating document", ex);
            }
        });
    }

    /**
     * Метод для сброса счетчика запросов по истечении интервала времени или ожидания до сброса
     */
    private synchronized void resetRequestLimitIfNeeded() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastResetTime >= interval.toMillis()) {
            requestsInInterval = 0;
            lastResetTime = currentTime;
        } else if (requestsInInterval >= requestLimit) {
            try {
                wait(lastResetTime + interval.toMillis() - currentTime);
            } catch (InterruptedException ex) {
                logger.warn("Reset request limit interrupted", ex);
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Метод для добавления задачи в очередь
     *
     * @param task Задача
     */
    private void submitTask(Runnable task) {
        try {
            taskQueue.put(task);
        } catch (InterruptedException ex) {
            logger.error("Task submission interrupted", ex);
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        CrptApi api = new CrptApi(TimeUnit.SECONDS, 10); // Пример: 10 запросов в секунду.
        String url = "https://ismp.crpt.ru/api/v3/lk/documents/create";

        Document document = new Document();
        document.setDocId("123456");
        document.setDocStatus("CREATED");

        String signature = "signature";

        api.createDocument(url, document, signature);
    }

    /**
     * Метод для создания HTTP запроса
     *
     * @param url         Путь к ресурсу
     * @param signature   Подпись
     * @param requestBody Сериализованный в формат Json документ
     * @return HTTP запрос
     */
    private HttpRequest createHttpRequest(String url, String signature, String requestBody) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Signature", signature)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }

    /**
     * Класс с информацией о документе.
     */
    @Getter
    @Setter
    public static class Document {
        private Description description;
        private List<Product> products;
        private String docId;
        private String docStatus;
        private String docType;
        private boolean importRequest;
        private String ownerInn;
        private String participantInn;
        private String producerInn;
        private LocalDate productionDate;
        private String productionType;
        private LocalDate regDate;
        private String regNumber;
    }

    /**
     * Класс с информацией по описанию.
     */
    @Getter
    @Setter
    public static class Description {
        private String participantInn;
    }

    /**
     * Класс с информацией о продукте.
     */
    @Getter
    @Setter
    public static class Product {
        private String certificateDocument;
        private LocalDate certificateDocumentDate;
        private String certificate_DocumentNumber;
        private String ownerInn;
        private String producerInn;
        private LocalDate productionDate;
        private String tnvedCode;
        private String uitCode;
        private String uituCode;
    }
}
