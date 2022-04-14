package application;

import org.apache.activemq.ActiveMQConnectionFactory;
import sharedrmi.application.api.MessageProducerService;

import javax.jms.*;
import java.rmi.RemoteException;
import java.util.List;

public class MessageProducerServiceImpl implements MessageProducerService {

    @Override
    public void publish(List<String> topics, String messageTitle, String messageText) throws RemoteException, JMSException {

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_BROKER_BIND_URL);

        Connection connection = connectionFactory.createConnection();
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
    }
}
