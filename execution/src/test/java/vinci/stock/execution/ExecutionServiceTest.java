package vinci.stock.execution;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import vinci.stock.execution.dto.FilledUpdateRequest;
import vinci.stock.execution.dto.Transaction;
import vinci.stock.execution.repositories.OrderProxy;
import vinci.stock.execution.repositories.PriceProxy;
import vinci.stock.execution.repositories.WalletProxy;

@SpringBootTest
public class ExecutionServiceTest {

  @Mock
  private PriceProxy priceProxy;

  @Mock
  private OrderProxy orderProxy;

  @Mock
  private WalletProxy walletProxy;

  @InjectMocks
  private ExecutionService executionService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void executeOrderSuccessfully() {
    Transaction transaction = new Transaction();
    transaction.setSeller("seller");
    transaction.setBuyer("buyer");
    transaction.setTicker("ticker");
    transaction.setPrice(100);
    transaction.setQuantity(10);
    transaction.setBuyOrderGuid("buyOrderGuid");
    transaction.setSellOrderGuid("sellOrderGuid");

    executionService.executeOrder(transaction);

    verify(walletProxy, times(1)).updateOne(eq("seller"), anyList());
    verify(walletProxy, times(1)).updateOne(eq("buyer"), anyList());
    verify(orderProxy, times(1)).updateOne(eq("buyOrderGuid"), any(FilledUpdateRequest.class));
    verify(orderProxy, times(1)).updateOne(eq("sellOrderGuid"), any(FilledUpdateRequest.class));
    verify(priceProxy, times(1)).updateOne(eq("ticker"), eq(100));
  }

  @Test
  public void executeOrderFailsWhenExceptionOccurs() {
    Transaction transaction = new Transaction();
    transaction.setSeller("seller");
    transaction.setBuyer("buyer");
    transaction.setTicker("ticker");
    transaction.setPrice(100);
    transaction.setQuantity(10);
    transaction.setBuyOrderGuid("buyOrderGuid");
    transaction.setSellOrderGuid("sellOrderGuid");

    doThrow(new RuntimeException()).when(walletProxy).updateOne(eq("seller"), anyList());

    executionService.executeOrder(transaction);

    verify(walletProxy, times(1)).updateOne(eq("seller"), anyList());
    verify(walletProxy, never()).updateOne(eq("buyer"), anyList());
    verify(orderProxy, never()).updateOne(eq("buyOrderGuid"), any(FilledUpdateRequest.class));
    verify(orderProxy, never()).updateOne(eq("sellOrderGuid"), any(FilledUpdateRequest.class));
    verify(priceProxy, never()).updateOne(eq("ticker"), eq(100));
  }
}