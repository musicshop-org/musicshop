package application;

import infrastructure.ProductRepositoryImpl;
import org.apache.activemq.ActiveMQConnectionFactory;
import sharedrmi.application.api.MessageProducerService;
import sharedrmi.application.dto.MessageDTO;

import javax.jms.*;
import javax.naming.NoPermissionException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MessageProducerServiceImpl extends UnicastRemoteObject implements MessageProducerService {

    private final String DEFAULT_BROKER_BIND_URL = "tcp://10.0.40.162:61616";

    public MessageProducerServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void publish(List<String> topics, MessageDTO messageDTO) throws RemoteException {

        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(DEFAULT_BROKER_BIND_URL);

            Connection connection = connectionFactory.createConnection();

            connection.start();

            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            for (String topicName : topics) {

                Topic topic = session.createTopic(topicName);

                MessageProducer messageProducer = session.createProducer(topic);
                messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
                long ttl = TimeUnit.DAYS.toMillis(messageDTO.getExpirationDays());
                messageProducer.setTimeToLive(ttl);

                TextMessage textMessage = session.createTextMessage(messageDTO.getMessageText());
                textMessage.setJMSCorrelationID(messageDTO.getMessageTitle());

               // textMessage.setJMSExpiration(TimeUnit.DAYS.toMillis(messageDTO.getExpirationDays()));
                messageProducer.send(textMessage);
            }

            session.close();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
