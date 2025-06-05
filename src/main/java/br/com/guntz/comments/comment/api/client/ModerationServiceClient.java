package br.com.guntz.comments.comment.api.client;

import br.com.guntz.comments.comment.api.model.ModerationInput;
import br.com.guntz.comments.comment.api.model.ModerationOutput;

public interface ModerationServiceClient {

    ModerationOutput validatedComment(ModerationInput moderationInput);

}
