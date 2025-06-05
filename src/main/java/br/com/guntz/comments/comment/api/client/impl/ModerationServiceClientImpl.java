package br.com.guntz.comments.comment.api.client.impl;

import br.com.guntz.comments.comment.api.client.ModerationServiceClient;
import br.com.guntz.comments.comment.api.client.RestClientFactory;
import br.com.guntz.comments.comment.api.model.ModerationInput;
import br.com.guntz.comments.comment.api.model.ModerationOutput;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ModerationServiceClientImpl implements ModerationServiceClient {

    private final RestClient restClient;

    public ModerationServiceClientImpl(RestClientFactory factory) {
        this.restClient = factory.moderationServiceRestClient();
    }

    @Override
    public ModerationOutput validatedComment(ModerationInput moderationInput) {
        return restClient.post()
                .uri("/api/moderate")
                .body(moderationInput)
                .retrieve()
                .body(ModerationOutput.class);
    }
}
