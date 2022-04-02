package infrastructure;

import domain.Invoice;
import domain.Song;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import sharedrmi.application.dto.InvoiceDTO;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

public class InvoiceRepositoryImpl implements InvoiceRepository{
    private final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @Override
    public List<Invoice> findInvoiceById(InvoiceId invoiceId) throws RemoteException {

        Session session = sessionFactory.openSession();
        List<Invoice> invoiceResults = session.createQuery("from Invoice where invoiceId = 1", Invoice.class).setParameter("invoiceId", invoiceId).list();

        return invoiceResults;
    }

    @Override
    public void createInvoice(Invoice invoice){
        Session session = sessionFactory.openSession();
    }

    public static void main(String[] args) throws RemoteException {
        InvoiceRepositoryImpl invoiceRepository = new InvoiceRepositoryImpl();
        List<Invoice> list = invoiceRepository.findInvoiceById(new InvoiceId());
        System.out.println(list.get(0).toString());

    }
}
