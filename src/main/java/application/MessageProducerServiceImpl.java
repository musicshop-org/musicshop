package application;

import org.apache.activemq.ActiveMQConnectionFactory;
import sharedrmi.application.api.MessageProducerService;

import javax.jms.*;
import java.rmi.RemoteException;
import java.util.List;

public class MessageProducerServiceImpl implements MessageProducerService {

    private final String DEFAULT_BROKER_BIND_URL = "tcp://10.0.40.162:61616";

    @Override
    public void publish(List<String> topics, String messageTitle, String messageText) throws RemoteException {

        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);

            Connection connection = null;
            connection = connectionFactory.createConnection();

            connection.start();

            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            for (String topicName : topics) {

                Topic topic = session.createTopic(topicName);

                MessageProducer messageProducer = session.createProducer(topic);
                messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

                TextMessage textMessage = session.createTextMessage(messageText);
                textMessage.setJMSCorrelationID(messageTitle);
                messageProducer.send(textMessage);
            }

            session.close();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}