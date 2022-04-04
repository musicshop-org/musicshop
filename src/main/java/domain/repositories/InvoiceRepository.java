package domain.repositories;

import domain.Invoice;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.rmi.RemoteException;
import java.util.Optional;

public interface InvoiceRepository {

    Optional<Invoice> findInvoiceById(InvoiceId invoiceId) throws RemoteException;

    void createInvoice(Invoice invoice) throws RemoteException;

}
