package application;

import domain.Invoice;
import domain.InvoiceLineItem;
import sharedrmi.application.dto.InvoiceDTO;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.rmi.RemoteException;
import java.util.List;

public interface InvoiceService {
    List<InvoiceDTO> findInvoiceById(InvoiceId invoiceId) throws RemoteException;
    void createInvoice(InvoiceDTO invoiceDTO);
}
