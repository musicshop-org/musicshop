package application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import sharedrmi.application.api.MessageProducerService;

@ExtendWith(MockitoExtension.class)
public class MessageProducerServiceTest {

    private MessageProducerService messageProducerService;

    @BeforeEach
    void initMockAndService() {
        messageProducerService = new MessageProducerServiceImpl();
    }

    @Test
    void given_topicAndMessage_when_publish_then_produceMessageToTopic() {
        // TODO: implement
        // given


        // when


        // then

    }
}
