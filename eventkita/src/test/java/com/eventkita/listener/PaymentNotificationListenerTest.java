package com.eventkita.listener;

import com.eventkita.entity.Booking;
import com.eventkita.entity.Payment;
import com.eventkita.entity.User;
import com.eventkita.event.PaymentSuccessEvent;
import com.eventkita.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentNotificationListenerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PaymentNotificationListener listener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandlePaymentSuccess_CreatesNotification() {
        // Arrange
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getFullName()).thenReturn("Test User");

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setUser(user);

        Payment payment = new Payment();
        payment.setId(5L);
        payment.setPaymentCode("ABC123");
        payment.setBooking(booking);

        PaymentSuccessEvent event = new PaymentSuccessEvent(this, payment);

        // Act
        listener.handlePaymentSuccess(event);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<String> titleCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(notificationService, times(1)).createNotification(
                userCaptor.capture(),
                titleCaptor.capture(),
                messageCaptor.capture()
        );

        assertEquals(user.getId(), userCaptor.getValue().getId());
        assertEquals("Pembayaran Berhasil", titleCaptor.getValue());
        assertTrue(messageCaptor.getValue().contains("ABC123"));
        assertTrue(messageCaptor.getValue().contains("10"));
    }

    @Test
    void testHandlePaymentSuccess_NoBooking_DoesNothing() {
        // Arrange
        Payment payment = new Payment();
        payment.setId(5L);
        payment.setBooking(null);

        PaymentSuccessEvent event = new PaymentSuccessEvent(this, payment);

        // Act
        listener.handlePaymentSuccess(event);

        // Assert
        verify(notificationService, never()).createNotification(any(), any(), any());
    }

    @Test
    void testHandlePaymentSuccess_ExceptionHandled() {
        // Arrange
        User user = mock(User.class);
        Booking booking = new Booking();
        booking.setUser(user);

        Payment payment = new Payment();
        payment.setBooking(booking);

        PaymentSuccessEvent event = new PaymentSuccessEvent(this, payment);

        doThrow(new RuntimeException("DB error")).when(notificationService).createNotification(any(), any(), any());

        // Act & Assert - should not throw
        assertDoesNotThrow(() -> listener.handlePaymentSuccess(event));
    }
}
