package com.example.application;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.ScheduledReporter;
//import com.izettle.metrics.dw.InfluxDbReporterFactory;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.concurrent.TimeUnit;

import com.kickstarter.dropwizard.metrics.influxdb.InfluxDbMeasurementReporterFactory;

public class ExampleApplication extends Application<ExampleConfiguration>{

    public static void main(String[] args) throws Exception {
        new ExampleApplication().run(args);
    }

    @Override
    public void initialize(final Bootstrap<ExampleConfiguration> bootstrap) {
        super.initialize(bootstrap);
        bootstrap.getObjectMapper().registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
    }

    @Override
    public void run(ExampleConfiguration configuration, Environment environment) throws Exception {

        if(configuration.metricsEnabled()) {


            /*InfluxDbWriter influxDbWriter = new InfluxDbTcpWriter("localhost",90, Duration.seconds(5));
            final Sender sender = new Sender(influxDbWriter);


            final InfluxDbMeasurementReporter reporter = new InfluxDbMeasurementReporter(
                    sender,
                    new MetricRegistry(),
                    MetricFilter.ALL,
                    TimeUnit.SECONDS,
                    TimeUnit.MILLISECONDS,
                    clock,
                    transformer
            );
*/
            //InfluxDbSender influxDbSender = new InfluxDbTcpSender("", 8080, 500, "", "");
            //InfluxDbReporterFactory influxDbReporterFactory = new InfluxDbReporterFactory();
            //ScheduledReporter scheduledReporter = influxDbReporterFactory.build(environment.metrics());

            InfluxDbMeasurementReporterFactory influxDbMeasurementReporterFactory =
                    new InfluxDbMeasurementReporterFactory();

            ScheduledReporter scheduledReporter = influxDbMeasurementReporterFactory.build(environment.metrics());

            scheduledReporter.start(5, TimeUnit.SECONDS);

            final ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(environment.metrics()).build();
            consoleReporter.start(5, TimeUnit.SECONDS);

        }
        final ExampleResource exampleResource = new ExampleResource(environment.metrics());
        environment.jersey().register(exampleResource);
    }
}
