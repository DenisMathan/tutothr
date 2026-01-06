package tutothr.payment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.ApplicationContext;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCaptureRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.paypal.orders.PurchaseUnitRequest;

@Service
public class PayPalService {
	private final PayPalHttpClient payPalClient;

	public PayPalService(PayPalHttpClient payPalClient) {
		this.payPalClient = payPalClient;
	}

	public String createPayment(Float amount, Long bookingId, String returnUrl, String cancelUrl) throws Exception {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.checkoutPaymentIntent("CAPTURE");
		
		// Zahlungsdetails
		PurchaseUnitRequest purchaseUnit = new PurchaseUnitRequest()
				.referenceId(bookingId.toString())
				.description("Buchung #" + bookingId)
				.amountWithBreakdown(new AmountWithBreakdown()
						.currencyCode("EUR")
						.value(String.format(java.util.Locale.US, "%.2f", amount)));
		
		List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
		purchaseUnits.add(purchaseUnit);
		orderRequest.purchaseUnits(purchaseUnits);
		
		// Redirect-URLs
		orderRequest.applicationContext(new ApplicationContext()
				.returnUrl(returnUrl)
				.cancelUrl(cancelUrl));
		
		// PayPal-Order erstellen
		OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);
		Order order = payPalClient.execute(request).result();
		
		// Approval-URL zurueckgeben (wo der Student hin muss)
		return order.links().stream()
				.filter(link -> "approve".equals(link.rel()))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Approval-Link nicht gefunden"))
				.href();
	}
		
	public boolean capturePayment(String orderId) {
		try {
			OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
			Order order = payPalClient.execute(request).result();
			return "COMPLETED".equals(order.status());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
