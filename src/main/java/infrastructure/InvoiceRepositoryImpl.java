package infrastructure;

import domain.Invoice;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.rmi.RemoteException;
import java.util.List;

public class InvoiceRepositoryImpl implements InvoiceRepository{
    private final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public List<Invoice> findInvoiceById(InvoiceId invoiceId) throws RemoteException {

        Session session = sessionFactory.openSession();
        List<Invoice> invoiceResults = session.createQuery("from Invoice where invoiceId = 1", Invoice.class).setParameter("invoiceId", invoiceId).list();

        return invoiceResults;
    }

    @Override
    public void createInvoice(Invoice invoice) throws RemoteException {

        Session session = sessionFactory.openSession();

        // TODO: create query to insert invoice

    }

    public static void main(String[] args) throws RemoteException {

        InvoiceRepositoryImpl invoiceRepository = new InvoiceRepositoryImpl();
        List<Invoice> list = invoiceRepository.findInvoiceById(new InvoiceId());
        System.out.println(list.get(0).toString());
    }
}
