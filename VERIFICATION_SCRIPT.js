// Payment Flow Verification Script
// Run this in browser console at payment.html after clicking "Bayar" dengan QRIS method

console.log("=== QRIS CODE GENERATION VERIFICATION ===\n");

// 1. Check if paymentData global exists
console.log("1. Global paymentData:", typeof paymentData !== 'undefined' ? "✅ Exists" : "❌ Missing");
if (typeof paymentData !== 'undefined') {
  console.log("   - Payment ID:", paymentData.id);
  console.log("   - Payment Code:", paymentData.paymentCode);
  console.log("   - Amount:", paymentData.amount);
  console.log("   - Method:", paymentData.paymentMethod);
  console.log("   - Status:", paymentData.status);
}

// 2. Check QRIS code input visibility
const qrisDiv = document.getElementById('qrisCodeDiv');
const qrisInput = document.getElementById('qrisCode');
console.log("\n2. QRIS Code Input UI:");
console.log("   - Div visibility:", qrisDiv.style.display !== 'none' ? "✅ Visible" : "❌ Hidden");
console.log("   - Input value:", qrisInput.value);
console.log("   - Code length:", qrisInput.value.length, "(expected: 20)");

// 3. Check if code is 20 characters (alphanumeric uppercase)
const codeRegex = /^[A-Z0-9]{20}$/;
console.log("\n3. QRIS Code Format Validation:");
console.log("   - Matches pattern [A-Z0-9]{20}:", codeRegex.test(qrisInput.value) ? "✅ Valid" : "❌ Invalid");

// 4. Check copy button
const copyButton = qrisDiv.querySelector('button[onclick="copyQrisCode()"]');
console.log("\n4. Copy Button:");
console.log("   - Exists:", copyButton ? "✅ Yes" : "❌ No");

// 5. Check payment method selected
const paymentMethod = document.getElementById('payMethod').value;
console.log("\n5. Payment Method:");
console.log("   - Selected:", paymentMethod);
console.log("   - Is QRIS:", paymentMethod === 'QRIS' ? "✅ Yes" : "❌ No");

// 6. API Endpoint check
const apiBase = (typeof API_BASE !== 'undefined' ? API_BASE : 'http://localhost:8888/api');
console.log("\n6. API Configuration:");
console.log("   - API Base:", apiBase);
console.log("   - Payment Endpoint:", apiBase + `/payments/${paymentId}/process`);

// 7. Manual backend verification
console.log("\n=== BACKEND VERIFICATION ===");
console.log("Check these after clicking Bayar:");
console.log("1. Backend console should show: 'Payment processed: {payment object}'");
console.log("2. Payment.paymentCode should be 20-char uppercase alphanumeric");
console.log("3. Alert should show generated code");

// 8. Database check
console.log("\n=== DATABASE VERIFICATION ===");
console.log("Run this SQL to verify QRIS code persisted:");
console.log("SELECT id, payment_code, status, payment_method FROM payment WHERE id = " + paymentId + ";");
console.log("\nExpected result:");
console.log("- id: " + paymentId);
console.log("- payment_code: [20-char uppercase alphanumeric]");
console.log("- status: SUCCESS");
console.log("- payment_method: QRIS");

// 9. Email verification
console.log("\n=== EMAIL VERIFICATION ===");
if (typeof paymentData !== 'undefined' && paymentData.booking) {
  console.log("Check email sent to:", paymentData.booking.userId ? "[User email]" : "[Check payment.booking structure]");
  console.log("Email should contain: E-Ticket attachment with QR code");
}

// 10. Summary
console.log("\n=== SUMMARY ===");
console.log("If all checks show ✅:");
console.log("✅ QRIS code generation working end-to-end");
console.log("✅ Frontend UI correctly displays code");
console.log("✅ Backend persists code to database");
console.log("✅ Observer pattern executing (check email received)");
