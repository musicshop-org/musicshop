package infrastructure;

import domain.Invoice;
import domain.repositories.InvoiceRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.rmi.RemoteException;
import java.util.Optional;

public class InvoiceRepositoryImpl implements InvoiceRepository {

    private final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public Optional<Invoice> findInvoiceById(InvoiceId invoiceId) throws RemoteException {

        Session session = sessionFactory.openSession();

        return session.createQuery("from Invoice where invoiceId = :invoiceId", Invoice.class)
                .setParameter("invoiceId", invoiceId)
                .getResultList().stream()
                .findFirst();
    }

    @Override
    public void createInvoice(Invoice invoice) throws RemoteException {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        session.persist(invoice);
        transaction.commit();
        session.close();
    }

}
