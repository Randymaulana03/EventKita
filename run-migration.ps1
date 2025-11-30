# Script untuk menjalankan migration SQL menggunakan JDBC
# Pastikan MySQL sudah running

$mysqlHost = "localhost"
$mysqlPort = "3306"
$mysqlDatabase = "eventkita_db"
$mysqlUser = "root"
$mysqlPassword = "root"  # Ganti dengan password MySQL Anda

Write-Host "Connecting to MySQL database: $mysqlDatabase..."

# SQL statements
$sql = @"
CREATE TABLE IF NOT EXISTS event_ticket_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    ticket_type VARCHAR(50) NOT NULL,
    price DOUBLE NOT NULL,
    quota INT NOT NULL,
    available_quota INT NOT NULL,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    UNIQUE KEY unique_event_ticket_type (event_id, ticket_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX IF NOT EXISTS idx_event_ticket_types_event_id ON event_ticket_types(event_id);
"@

Write-Host "`nSQL to execute:"
Write-Host $sql
Write-Host "`n=== INSTRUCTIONS ==="
Write-Host "1. Buka MySQL Workbench atau MySQL Command Line"
Write-Host "2. Connect ke database 'eventkita_db'"
Write-Host "3. Copy-paste SQL di atas dan execute"
Write-Host "4. Atau jalankan: mysql -u root -p eventkita_db < migration_event_ticket_types.sql"
Write-Host "`nSetelah itu, backend akan otomatis detect tabel baru saat restart."
