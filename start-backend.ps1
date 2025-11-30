$ErrorActionPreference = "Continue"

Write-Host "Starting EventKita Backend..."
Write-Host "Building..."

cd "d:\Coding\EventKita\eventkita\eventkita"

# Clean compile
& ".\mvnw.cmd" clean compile

if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed!"
    exit 1
}

Write-Host "Build successful! Starting Spring Boot..."
Write-Host "Backend will run on http://localhost:8888"

# Start without output buffering
& ".\mvnw.cmd" spring-boot:run
