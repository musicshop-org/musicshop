package infrastructure;

import domain.Invoice;
import domain.repositories.InvoiceRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
//        session.persist();
//        session.flush();

        // TODO: create query to insert invoice

    }

//    public static void main(String[] args) throws RemoteException {
//        InvoiceRepositoryImpl invoiceRepository = new InvoiceRepositoryImpl();
//        List<Invoice> list = invoiceRepository.findInvoiceById(new InvoiceId());
//        System.out.println(list.get(0).toString());
//    }
}
