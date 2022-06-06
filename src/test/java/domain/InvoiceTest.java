package domain;

import org.junit.jupiter.api.Test;
import sharedrmi.domain.enums.PaymentMethod;
import sharedrmi.domain.valueobjects.Address;
import sharedrmi.domain.valueobjects.CustomerData;
import sharedrmi.domain.valueobjects.InvoiceId;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InvoiceTest {

    @Test
    void given_customerData_when_newInvoice_then_expectCustomerData() {
        // given
        CustomerData expectedCustomerData = new CustomerData(
                "firstName", "lastName", "email",
                new Address("street", "zip", "city", "country"));

        // when
        Invoice invoice = new Invoice(
                new InvoiceId(),
                Collections.emptyList(),
                PaymentMethod.CASH,
                LocalDate.now(),
                expectedCustomerData
        );

        // then
        assertEquals(expectedCustomerData, invoice.getCustomerData());
    }
}