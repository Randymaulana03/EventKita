# EventKita
3 Design Pattern Yang diTerapkan
1. Factory Pattern (TicketFactory)
   - Factory: src/main/java/com/eventkita/factory/TicketFactory.java
   - Digunakan di: src/main/java/com/eventkita/service/impl/BookingServiceImpl.java
   alurnya
      -  User buat booking
      -  function BookingServiceImpl.createBookingFromRequest() dipanggil
      -  Sistem cek EventTicketType (jenis tiket dari organizer)
      -  function TicketFactory.createTicketFromEventTicketType() dipanggil → bikin objek Ticket
     - >  icket berisi
       > Event
       > Jenis tiket (REGULAR/VIP/VVIP)
       > Harga
       > Quota
       > QR code

2. Strategy Pattern (Payment Method)
   - Interface: src/main/java/com/eventkita/strategy/PaymentStrategy.jav
   alurnya
      -  User pilih metode pembayaran (hanya masih QR Code bisanya)
      -  function PaymentServiceImpl.initiatePayment() dipanggil
      -  electStrategy(method) pilih strategy sesuai metode => hanya support QR Code masih
      -  Strategy dipanggil: QRIS → generate QR code, panggil gateway
      -  Strategy kembalikan hasil (PaymentResult)
    
3. Observer Pattern (Payment Notification)
   - Event: src/main/java/com/eventkita/event/PaymentEvent.java
   - Listener: src/main/java/com/eventkita/listener/PaymentNotificationListener.ja
   - Publisher: PaymentServiceImpl.java
   - Konfigurasi: @EventListener (Spring)
   alurnya
      -  User bayar tiket
      -  PaymentServiceImpl.processPayment() dipanggil otomatis update status booking
      -  PaymentServiceImpl.publishPaymentEvent() dipanggil = kirim event PaymentEvent
      -  Listener PaymentNotificationListener otomatis menangkap event (@EventListener)
    Notifikasi dikirim ke: User = pembayaran berhasil
