package com.project.donate.Config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

        @Bean
        public ElasticsearchClient elasticsearchClient() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            JacksonJsonpMapper mapper = new JacksonJsonpMapper(objectMapper);

            RestClient restClient = RestClient.builder(
                    new HttpHost("localhost", 9200)
            ).build();

            RestClientTransport transport = new RestClientTransport(restClient, mapper);
            return new ElasticsearchClient(transport);
        }
}

