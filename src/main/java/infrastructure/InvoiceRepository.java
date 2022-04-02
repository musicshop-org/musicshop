package infrastructure;

import domain.Invoice;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.rmi.RemoteException;
import java.util.List;

public interface InvoiceRepository {

    List<Invoice> findInvoiceById(InvoiceId invoiceId) throws RemoteException;

    void createInvoice(Invoice invoice) throws RemoteException;

}
