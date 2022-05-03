package domain.repositories;

import domain.Invoice;
import sharedrmi.application.dto.InvoiceLineItemDTO;
import sharedrmi.application.exceptions.InvoiceNotFoundException;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Optional;

public interface InvoiceRepository extends Serializable {

    Optional<Invoice> findInvoiceById(InvoiceId invoiceId);

    void createInvoice(Invoice invoice);
    void update(Invoice invoice);
}
