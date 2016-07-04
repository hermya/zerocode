package org.jsmart.smarttester.core.logbuilder;

import org.slf4j.Logger;

import java.time.Duration;
import java.util.UUID;

import static java.lang.String.format;

public class LogCorelationshipPrinter {
    private static final String DISPLAY_DEMARCATION_ = "\n------------------ RELATIONSHIP-ID: %s ------------------";

    Logger logger;
    RequestLogBuilder requestLogBuilder = new RequestLogBuilder();
    ResponseLogBuilder responseLogBuilder = new ResponseLogBuilder();
    ScenarioLogBuilder scenarioLogBuilder = new ScenarioLogBuilder();

    public LogCorelationshipPrinter(Logger logger) {
        this.logger = logger;
    }

    public static LogCorelationshipPrinter newInstance(Logger logger) {
        return new LogCorelationshipPrinter(logger);
    }

    public RequestLogBuilder aRequestBuilder() {
        return requestLogBuilder;
    }

    public LogCorelationshipPrinter assertion(String assertionJson){
        responseLogBuilder.assertionSection(assertionJson);
        return this;
    }

    public ResponseLogBuilder aResponseBuilder() {
        return responseLogBuilder;
    }

    public ScenarioLogBuilder aScenarioBuilder() {
        return scenarioLogBuilder;
    }

    public void print() {

        logger.info(String.format("%s %s \n*Response delay:%s milli-secs \n%s \n-done-\n",
                requestLogBuilder.toString(),
                responseLogBuilder.toString(),
                Duration.between(
                        requestLogBuilder.getRequestTimeStamp(),
                        responseLogBuilder.getResponseTimeStamp())

                        /*
                         * 1000000D: Without D it does a integer division and the precision is lost
                         * Note: Java does not have a get(millisec-tem[poral) as of now, only nano
                         * or sec precision is supported
                         */
                        .getNano()/1000000D,
                "---------> Assertion: <----------\n" + responseLogBuilder.getAssertion()
                )
        );
    }

    public static String createRelationshipId() {
        return format(DISPLAY_DEMARCATION_, UUID.randomUUID().toString());
    }
}